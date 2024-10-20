package victor.training.sourcing.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import victor.training.sourcing.EventProcessor;
import victor.training.sourcing.aggregate.User;
import victor.training.sourcing.event.*;
import victor.training.sourcing.repo.UserRepo;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserApi {
  private final EventProcessor eventProcessor;
  private final UserRepo userRepo;

  @With
  public record CreateUserRequest(
      @Email String email,
      @NotBlank String name,
      @NotNull String departmentId,
      List<String> roles
  ) {
  }

  @PostMapping
  public String createUser(@RequestBody CreateUserRequest request) {
//    String userId = UUID.randomUUID().toString();// real
    String userId = "" + userRepo.getNewId();// demo convenience

    eventProcessor.apply(new UserCreated(userId)
        .name(request.name())
        .email(request.email())
        .departmentId(request.departmentId()));
    for (String role : request.roles()) {
      eventProcessor.apply(new UserRoleAssigned(userId).role(role));
    }
    // TODO discuss:
    //  a) UserCreated{roles} 💖
    //  b) UserRolesAssigned{roles}
    //  c) UserRoleAssigned{role}
    return userId;
  }

  @PutMapping("/{userId}/confirm-email")
  public void confirmEmail(@PathVariable String userId, @RequestParam String email) {
    User user = userRepo.findById(userId).orElseThrow();
    List<AbstractUserEvent> events = user.confirmEmail(email);
    events.forEach(eventProcessor::apply);
  }

  public record UpdateUserRequest(
      @Email String email,
      @NotBlank String name,
      @NotNull String departmentId
  ) {
  }

  @PutMapping("/{userId}")
  public void update(@PathVariable String userId, @RequestBody UpdateUserRequest request) {
    User user = userRepo.findById(userId).orElseThrow();
    if (!request.email().equals(user.email())) {
      eventProcessor.apply(new UserEmailUpdated(userId)
          .email(request.email()));
    }
    eventProcessor.apply(new UserUpdated(userId)
        .name(request.name())
        .departmentId(request.departmentId()));
  }

  @PutMapping("/{userId}/roles/{role}")
  public void assignRole(@PathVariable String userId, @PathVariable String role) {
    eventProcessor.apply(new UserRoleAssigned(userId).role(role));
  }

  @DeleteMapping("/{userId}/roles/{role}")
  public void revokeRole(@PathVariable String userId, @PathVariable String role) {
    eventProcessor.apply(new UserRoleRevoked(userId).role(role));
  }

  @PutMapping("/{userId}/deactivate")
  public void deactivate(@PathVariable String userId) {
    eventProcessor.apply(new UserDeactivated(userId));
  }

  @PutMapping("/{userId}/activate")
  public void activate(@PathVariable String userId) {
    eventProcessor.apply(new UserActivated(userId));
  }

  // pretend this is a @KafkaListener (to avoid having to setup Kafka on trainees' machines)
  @GetMapping("/{userId}/login")
  public void login(@PathVariable String userId) {
    eventProcessor.apply(new UserLoggedIn(userId));
  }

}
