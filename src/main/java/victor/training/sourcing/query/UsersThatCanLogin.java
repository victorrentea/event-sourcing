package victor.training.sourcing.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.sourcing.ClearStateEvent;
import victor.training.sourcing.event.UserEvent;
import victor.training.sourcing.event.UserEvent.UserActivated;
import victor.training.sourcing.event.UserEvent.UserDeactivated;
import victor.training.sourcing.event.UserEvent.UserEmailConfirmed;
import victor.training.sourcing.event.UserEvent.UserEmailUpdated;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UsersThatCanLogin {
  private final Map<String, String> confirmedEmailsToId = new HashMap<>();
  private final Set<String> inactiveUserIds = new HashSet<>();

  @EventListener(ClearStateEvent.class)
  public void clearState() {
    confirmedEmailsToId.clear();
    inactiveUserIds.clear();
  }

  @EventListener
  public void onActivated(UserActivated event) {
    inactiveUserIds.remove(event.aggregateId());
  }
  @EventListener
  public void onDeactivated(UserDeactivated event) {
    inactiveUserIds.add(event.aggregateId());
  }
  @EventListener
  public void onEmailConfirmed(UserEmailConfirmed event) {
    confirmedEmailsToId.put(event.email(), event.aggregateId());
  }
  @EventListener
  public void onEmailUpdated(UserEmailUpdated event) {
    confirmedEmailsToId.remove(event.email());
  }

  // http://localhost:8080/users-to-login
  @GetMapping("/users-to-login")
  public Set<String> getUsersToLogin() {
    return confirmedEmailsToId.entrySet()
        .stream()
        .filter(entry -> !inactiveUserIds.contains(entry.getValue()))
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());
  }

}
