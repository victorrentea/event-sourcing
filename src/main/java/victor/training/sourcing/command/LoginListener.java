package victor.training.sourcing.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.sourcing.EventProcessor;
import victor.training.sourcing.event.UserEvent;

import static victor.training.sourcing.event.UserEvent.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginListener {
  private final EventProcessor eventProcessor;

  // @KafkaListener // pretend, to avoid seting up a local Kafka broker
  @GetMapping("users/{userId}/login/{application}")
  public void login(@PathVariable String userId, @PathVariable String application) {
    eventProcessor.apply(new UserLoggedIn(userId).application(application));
  }

}
