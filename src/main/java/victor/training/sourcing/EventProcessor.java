package victor.training.sourcing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.sourcing.common.Event;
import victor.training.sourcing.repo.EventRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventProcessor {
  private final EventRepo eventRepo;

  public void apply(String aggregateId, Event event) {

    eventRepo.save(event);
  }
}
