package victor.training.sourcing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import victor.training.sourcing.aggregate.User;
import victor.training.sourcing.event.AbstractEvent;
import victor.training.sourcing.event.AbstractUserEvent;
import victor.training.sourcing.repo.EventRepo;
import victor.training.sourcing.repo.SnapshotRepo;
import victor.training.sourcing.repo.UserRepo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.Long.MAX_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
public class EventRestApi {
  private final EventRepo eventRepo;
  private final EventProcessor eventProcessor;
  private final ApplicationEventPublisher eventPublisher;
  private final UserRepo userRepo;
  private final SnapshotRepo snapshotRepo;
  private final ObjectMapper objectMapper;

  @GetMapping("/events")
  public List<AbstractEvent> getEvents() {
    return eventRepo.findAll();
  }

  @GetMapping("/events/bare")
  public List<AbstractEvent> getEventsBare() {
    return eventRepo.findAll()
        .stream()
        .peek(e->e.snapshot(null))
        .toList();
  }
  // http://localhost:8080/events/2/snapshot

  // TODO implement time travelling to a point in time

  @GetMapping("/events/{eventId}/snapshot")
  public Map<?, ?> getUserAfterEvent(@PathVariable Long eventId) {
    AbstractEvent event = eventRepo.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
    return event.getSnapshot();
  }
  // http://localhost:8080/events/2/replay

  @GetMapping("/events/{eventId}/replay")
  public User getUserAfterEventReplay(@PathVariable Long eventId) {
    AbstractEvent lastEvent = eventRepo.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
    String aggregateType = lastEvent.aggregateType();
    String aggregateId = lastEvent.aggregateId();

    List<AbstractUserEvent> events = eventRepo.findByAggregateTypeIgnoreCaseAndAggregateIdAndEventIdBetween(
            aggregateType,
            aggregateId,
            0L, eventId)
        .stream()
        .map(AbstractUserEvent.class::cast)
        .toList();
    if (events.isEmpty()) {
      throw new IllegalArgumentException("No events found for " + aggregateType + "/" + aggregateId);
    }
    User user = new User();
    for (AbstractUserEvent event : events) {
      log.info("Replaying event: {}", event);
      user.apply(event);
    }
    return user;
  }
  // eg http://localhost:8080/rewind-to/2

  @GetMapping("/events/rewind-to/{eventId}")
  public String rewindTo(@PathVariable Long eventId) {
    userRepo.deleteAll();
    snapshotRepo.deleteAll();
    eventPublisher.publishEvent(new ClearStateEvent());

    List<AbstractEvent> allEvents = eventRepo.findAll();
    for (AbstractEvent event : allEvents) {
      if (event.eventId() > eventId) {
        break;
      }
      event.replay(true);
      eventProcessor.apply(event);
    }
    return "Rewound to event " + eventId + ". <a href='/events'>See events</a>";
  }
  @PostMapping("events/upload")
  public String uploadEvents(@RequestParam MultipartFile file) throws IOException {
    String contents = new String(file.getBytes());
    List<AbstractEvent> events = objectMapper.readValue(contents, new TypeReference<>() {});
    for (AbstractEvent e : events) e.replay(true);
    snapshotRepo.deleteAll();
    eventRepo.deleteAll();
    eventRepo.saveAll(events);
    rewindTo(MAX_VALUE);
    return "event log overwritten OK with " + events.size() + " events. <a href='/'>Home</a>";
  }
}
