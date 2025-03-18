package victor.training.sourcing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import victor.training.sourcing.command.UserRestApi.CreateUserRequest;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StartupData {
  private final RestTemplate rest;

  @EventListener(ApplicationStartedEvent.class)
  public void onAppStart() {
    log.info("⭐️⭐️⭐️⭐️⭐️ Started ⭐️⭐️⭐️⭐️⭐️");
    log.info("Creating initial data");
    rest.postForEntity("http://localhost:8080/users",
        new CreateUserRequest(
            "a@b.com",
            "John Doe",
            "IT",
            List.of("app1:ADMIN", "app1:USER")),
        String.class
    );

    // confirm email
    rest.put("http://localhost:8080/users/1/confirm-email?email=a@b.com", null, String.class);

    // withdraw role
    rest.delete("http://localhost:8080/users/1/roles/app1:ADMIN");

    rest.getForObject("http://localhost:8080/users/1/login/CORE", Void.class);

    rest.put("http://localhost:8080/users/1/deactivate", null);

    rest.put("http://localhost:8080/users/1/activate", null);

    log.info("Created initial data");
  }
}
