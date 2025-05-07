package victor.training.sourcing;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import victor.training.sourcing.aggregate.User;
import victor.training.sourcing.event.AbstractEvent;
import victor.training.sourcing.event.UserEvent;
import victor.training.sourcing.event.Snapshot;
import victor.training.sourcing.repo.EventRepo;
import victor.training.sourcing.repo.SnapshotRepo;
import victor.training.sourcing.repo.UserRepo;

import java.util.Optional;

@SuppressWarnings("ALL")
@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class EventProcessor {
  private final EventRepo eventRepo;
  private final ObjectMapper objectMapper;
  private final ApplicationEventPublisher eventPublisher;
  private final UserRepo userRepo;
  private final SnapshotRepo snapshotRepo;
  private final EntityManager entityManager;

  @Transactional
  public void apply(@Valid AbstractEvent event) {
    apply((UserEvent) event); // only user events ftm
  }

  @SneakyThrows
  @Transactional
  public void apply(@Valid UserEvent event) {
    Optional<User> userOpt = userRepo.findById(event.aggregateId());
    if (userOpt.isPresent()) {
      // Safety-net: discard any changes performed by command processors to event in PersistenceContext
      //   to make sure all state changes are applied by the event handler
      entityManager.refresh(userOpt.get());
    }

    User user = userOpt.orElse(new User());

    event.userAuthor("victor"); // TODO from SecurityContext
    // persist the event
    eventRepo.save(event);

    // apply the event to change the aggregate state
    user.apply(event);

    // persist the changed entity ❓in the same tx as event
    userRepo.save(user);

    // ❓ persist a snapshot each time - space is cheap
    snapshotRepo.save(new Snapshot(event, objectMapper.writeValueAsString(user))); // #3

    entityManager.flush();
    // trigger read-projections and reactors
    eventPublisher.publishEvent(event);
  }

}
