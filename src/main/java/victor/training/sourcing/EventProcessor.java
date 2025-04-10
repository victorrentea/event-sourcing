package victor.training.sourcing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import victor.training.sourcing.aggregate.Aggregate;
import victor.training.sourcing.aggregate.User;
import victor.training.sourcing.event.AbstractEvent;
import victor.training.sourcing.event.AbstractUserEvent;
import victor.training.sourcing.event.Snapshot;
import victor.training.sourcing.repo.EventRepo;
import victor.training.sourcing.repo.SnapshotRepo;
import victor.training.sourcing.repo.UserRepo;

import java.util.HashMap;
import java.util.Map;
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
    apply((AbstractUserEvent) event);
  }

  @Transactional
  public void apply(@Valid AbstractUserEvent event) {
    Optional<User> userOpt = userRepo.findById(event.aggregateId());
    if (userOpt.isPresent()) {
      // Safety-net: discard any changes performed by command processors to event in PersistenceContext
      //   to make sure all state changes are applied by the event handler
      entityManager.refresh(userOpt.get());
    }

    User user = userOpt.orElse(new User());

    // persist the event
    eventRepo.save(event);

    // apply the event to change the aggregate state
    user.apply(event);

    // persist the changed entity ❓in the same tx as event
    userRepo.save(user);

    // ❓ persist a snapshot each time - space is cheap
    snapshotRepo.save(new Snapshot(event, toJson(user))); // #3

    entityManager.flush();
    // trigger read-projections and reactors
    eventPublisher.publishEvent(event);
  }

  private String toJson(Aggregate<?> aggregate) {
    try {
      return objectMapper.writeValueAsString(aggregate);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to convert aggregate to JSON", e);
    }
  }
}
