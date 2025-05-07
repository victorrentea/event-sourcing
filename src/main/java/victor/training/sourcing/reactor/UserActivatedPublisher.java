package victor.training.sourcing.reactor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RestController;
import victor.training.sourcing.event.UserEvent;
import victor.training.sourcing.event.UserEvent.UserActivated;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserActivatedPublisher {
  @EventListener(UserActivated.class) // internal Event
  public void clearState(UserActivated event) {
    if (!event.replay()) {
      // YOU talk to outside WORLD TODO
      // restTemplate.call...
      // kafkaTemplate.publish.... new ExternalIntegrationEvent
      // rabbitTemplate.send.... new ExternalIntegrationEvent
    }
  }


}
