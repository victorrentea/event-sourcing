package victor.training.sourcing.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import victor.training.sourcing.event.UserLoggedIn;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Comparator.reverseOrder;
import static java.util.Map.Entry.comparingByValue;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginHistory {
  private final Map<String, Map<String,LocalDateTime>> appToLogin = new HashMap<>();
  @EventListener
  public void onUserEvent(UserLoggedIn event) {
    log.info("User {} logged in", event.getSnapshot());
    Map<String, LocalDateTime> logins = appToLogin.computeIfAbsent(event.application(),
        k -> new HashMap<>());
    logins.put(event.email(), event.observedAt());
  }

  public record Login(String email, LocalDateTime loginTime) {}
  @GetMapping("/logins/{application}")
  public List<Login> getLastLogins(@PathVariable String application) {
    Map<String, LocalDateTime> logins = appToLogin.get(application);
    return logins.entrySet().stream()
        .sorted(comparingByValue(reverseOrder()))
        .map(e -> new Login(e.getKey(), e.getValue()))
        .toList();
  }
}
