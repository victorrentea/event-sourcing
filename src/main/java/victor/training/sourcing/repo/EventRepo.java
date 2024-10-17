package victor.training.sourcing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.sourcing.common.Event;

public interface EventRepo extends JpaRepository<Event, Long> {
}
