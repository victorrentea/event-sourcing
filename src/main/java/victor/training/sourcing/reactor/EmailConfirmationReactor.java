package victor.training.sourcing.reactor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.sourcing.EventProcessor;
import victor.training.sourcing.event.ConfirmationEmailSent;
import victor.training.sourcing.event.UserCreated;
import victor.training.sourcing.repo.UserRepo;

@Slf4j
@Service
@RequiredArgsConstructor
// stateless logic triggered by events that reacts to events by firing other events (aka policy)
public class EmailConfirmationReactor {
  private final EventProcessor eventProcessor;
  private final UserRepo userRepo;

  @EventListener // or on user email changed
  public void onUserCreated(UserCreated event) {
    if (event.replay()) {
      log.warn("NOOP on replay");
      return;
    }
    sendEmail(event.email());
    eventProcessor.apply(new ConfirmationEmailSent(event.aggregateId()).email(event.email()));
  }

  private void sendEmail(String email) {
    log.info("Sending confirmation email to {} ...", email);
    if (Math.random() < 0.1) {
      throw new RuntimeException("Email server down");
    }
  }
}
