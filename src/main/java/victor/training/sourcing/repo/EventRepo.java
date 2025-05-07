package victor.training.sourcing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.sourcing.event.AbstractEvent;

import java.util.List;

public interface EventRepo extends JpaRepository<AbstractEvent, Long> {
  List<AbstractEvent> findByAggregateTypeIgnoreCaseAndAggregateIdAndEventIdBetween(
      String aggregateType, String aggregateId, Long fromEventId, Long toEventId);
}
