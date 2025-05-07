package victor.training.sourcing;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import victor.training.sourcing.repo.EventRepo;
import victor.training.sourcing.repo.UserRepo;

@Service
@RequiredArgsConstructor
public class GenericEventProcessor {
  private final EventRepo eventRepo;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final UserRepo userRepo;

//  interface AggregateRepo {
//    <A extends Aggregate<?>> Optional<A> findById(String aggregateType, String aggregateId);
//    void save(Aggregate<?> aggregate);
//  }
//  @Component
//  public static class InMemoryAggregateRepo implements AggregateRepo {
//    private final Map<String, Map<String, Aggregate<?>>> aggregatesByType = new HashMap<>();
//    @Override
//    public <A extends Aggregate<?>> Optional<A> findById(String aggregateType, String aggregateId) {
//      Aggregate<?> existing = aggregatesByType.computeIfAbsent(aggregateType, k->Map.of()).get(aggregateId);
//      return Optional.<A>ofNullable((A) existing);
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
//
//  @Transactional
//  public <E extends AbstractEvent, A extends Aggregate<E>>
//  void apply(String aggregateId, E event, AggregateRepo aggregateRepo) {
//    event.aggregateId(aggregateId);
//    aggregate = apply(event);
//    aggregateRepo.save(aggregate);
//    eventRepo.save(event);
//    applicationEventPublisher.publishEvent(event);
//  }
//  //
//  private <E extends AbstractEvent> E apply(E event) {
//    switch (event) {
//      case AbstractUserEvent userEvent -> {
//        User user = userRepo.findById(event.aggregateId()).orElse(new User());
//        user.apply(userEvent);
////        userEvent.snapshotJson(toJson(user));
//        userRepo.save(user);
//        return (E)user;
//      }
//      default -> throw new IllegalArgumentException("Unknown aggregate type: " + event.aggregateType());
//    }
//  }
}
