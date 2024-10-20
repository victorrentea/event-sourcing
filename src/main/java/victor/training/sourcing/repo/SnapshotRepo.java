package victor.training.sourcing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.sourcing.event.Snapshot;

public interface SnapshotRepo extends JpaRepository<Snapshot, Long> {
}
