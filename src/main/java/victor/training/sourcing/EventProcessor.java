package victor.training.sourcing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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

//  interface AggregateRepo {
//    <A extends Aggregate<?>> Optional<A> findById(String aggregateType, String aggregateId);
//    void save(Aggregate<?> aggregate);
//  }
//  @Component
//  public static class InMemoryAggregateRepo implements AggregateRepo {
//    private final Map<String, Map<String, Aggregate<?>>> aggregatesByType = new HashMap<>();
//    @Override
//    public <A extends Aggregate<?>> Optional<A> findById(String aggregateType, String aggregateId) {
//      Aggregate<?> existing = aggregatesByType.getOrDefault(aggregateType, Map.of()).get(aggregateId);
//      if (existing==null) {
//        existing = new
//      }
//      return (A) existing;
//    }
//    @Override
//    public void save(Aggregate<?> aggregate) {
//    }
//  }
//  @Component
//  @RequiredArgsConstructor
//  public static class JpaAggregateRepo implements AggregateRepo {
//    private final EntityManager entityManager;
//    @Override
//    public <A extends Aggregate<?>> Optional<A> findById(String aggregateType, String aggregateId) {
//      Class<A> entityClass = switch (aggregateType) {
//        case "User" -> (Class<A>) User.class;
//        default -> throw new IllegalArgumentException("Unknown aggregate type: " + aggregateType);
//      };
//      return Optional.ofNullable(entityManager.find(entityClass, aggregateId));
//    }
//    @Override
//    public void save(Aggregate<?> aggregate) {
//      entityManager.merge(aggregate);
//    }
//  }

//  @Transactional
//  public <E extends AbstractEvent, A extends Aggregate<E>>
//  void apply(String aggregateId, E event, AggregateRepo aggregateRepo) {
//    event.aggregateId(aggregateId);
//    A aggregate = aggregateRepo.findById(event.aggregateType(), aggregateId);
//    apply(aggregate, event);
//    aggregateRepo.save(aggregate);
//    eventRepo.save(event);
//    eventPublisher.publishEvent(event);
//  }
//
//  private <E extends AbstractEvent> void apply(Aggregate<E> aggregate, E event) {
//    aggregate.apply(event);
//    switch (event) {
//      case AbstractUserEvent userEvent -> {
//        User user = userRepo.findById(event.aggregateId())
//            .orElse(new User());
//        user.apply(userEvent);
////        userEvent.snapshotJson(toJson(user));
//        userRepo.save(user);
//      }
//      default -> throw new IllegalArgumentException("Unknown aggregate type: " + event.aggregateType());
//    }
//  }

  @Transactional
  public void apply(@Valid AbstractEvent event) {
    apply((AbstractUserEvent) event);
  }

  @Transactional
  public void apply(@Valid AbstractUserEvent event) {
    User user = userRepo.findById(event.aggregateId()).orElse(new User());
    user.apply(event);
    userRepo.save(user);
    eventRepo.save(event);
    snapshotRepo.save(new Snapshot(event, toJson(user)));
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
