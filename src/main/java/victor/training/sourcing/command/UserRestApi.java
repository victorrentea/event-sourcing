package victor.training.sourcing.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.sourcing.EventProcessor;
import victor.training.sourcing.aggregate.User;
import victor.training.sourcing.event.*;
import victor.training.sourcing.repo.UserRepo;

import java.util.List;

import static victor.training.sourcing.event.UserEvent.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestApi {
  private final EventProcessor eventProcessor;
  private final UserRepo userRepo;

  public record GetUserResponse(
      String id,
      String email,
      String name,
      String departmentId,
      List<String> roles
  ) {
    public static GetUserResponse fromUser(User user) {
      return new GetUserResponse(user.id(), user.email(), user.name(), user.departmentId(), user.roles());
    }
  }

  @GetMapping("/{userId}")
  public GetUserResponse getUser(@PathVariable String userId) {
    return userRepo.findById(userId).map(GetUserResponse::fromUser).orElseThrow();
  }
  @GetMapping
  public List<GetUserResponse> getAllUsers() {
    return userRepo.findAll().stream().map(GetUserResponse::fromUser).toList();
  }

  @With
  public record CreateUserRequest(
      @Email String email,
      @NotBlank String name,
      @NotNull String departmentId,
      List<String> roles
  ) {
  }

  @PostMapping
  public String createUser(@RequestBody @Validated CreateUserRequest request) {
//    String userId = UUID.randomUUID().toString();// real
    String userId = "" + userRepo.getNewId();// demo convenience
    List<UserEvent> events = User.create(request, userId);
    events.forEach(eventProcessor::apply);
    // TODO discuss:
    //  a) UserCreated{roles} = coarse, mapped to domain action (publisher convenience) 💖
    //  b) UserRolesAssigned{roles} = coarse-grained aggregated event
    //  c) UserRoleAssigned{role} = fine-grained events (listener convenience)
    return userId;
  }

  @PutMapping("/{userId}/confirm-email")
  public void confirmEmail(@PathVariable String userId, @RequestParam @Validated  @Email String email) {
    User user = userRepo.findById(userId).orElseThrow();
    // traditional style - "burn the old data"
//    user.setEmailConfirmed(true); // you loose WHO, WHEN, IN WHAT ORDER

    // audit-opt#1 - audit columns
//    user.setLastModifiedBy(currentUser);

    // audit-opt#2 - audit table
//    auditRepo.save(new Audit("user-confirmed-email",userId, currentUser,now(),"{......}"))

    // audit-opt#3 - auto-save revisions on each change with Hibernate
    // Envers https://hibernate.org/orm/envers/
    // audit-opt#4 - time-traveling queriest: SELECT .. AS OF TIMESTAMP ...

//    userRepo.save(user);

    // audit-opt#5 - event sourcing = events are the source of truth
    // 1. a command results in domain event(s)
    List<UserEvent> events = user.confirmEmail(email);
    // 2. changes can only happen when applying events
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

  @PutMapping("/{userId}/activate")
  public void activate(@PathVariable String userId) {
    eventProcessor.apply(new UserActivated(userId));
  }

  @PutMapping("/{userId}/deactivate")
  public void deactivate(@PathVariable String userId) {
    eventProcessor.apply(new UserDeactivated(userId));
  }

}
