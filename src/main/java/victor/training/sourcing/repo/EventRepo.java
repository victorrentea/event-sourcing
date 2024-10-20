package victor.training.sourcing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.sourcing.event.AbstractEvent;
import victor.training.sourcing.event.AbstractUserEvent;

import java.util.List;
import java.util.stream.Stream;

public interface EventRepo extends JpaRepository<AbstractEvent, Long> {
  List<AbstractEvent> findByAggregateTypeIgnoreCaseAndAggregateIdAndEventIdBetween(
      String aggregateType, String aggregateId, Long fromEventId, Long toEventId);
}
