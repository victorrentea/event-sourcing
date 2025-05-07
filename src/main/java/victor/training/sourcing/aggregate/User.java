package victor.training.sourcing.aggregate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.*;
import lombok.Getter;
import victor.training.sourcing.command.UserRestApi.CreateUserRequest;
import victor.training.sourcing.event.*;
import victor.training.sourcing.event.UserEvent.UserCreated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static victor.training.sourcing.event.UserEvent.*;

@Entity
@Getter
@Table(name = "USERS")
@JsonAutoDetect(fieldVisibility = ANY) // stored as JSON in snapshots
public class User {
  @Id
  protected String id;
  private String name;
  private String email;
  private Boolean emailValidated = false;
  private String departmentId;
  private Boolean active = false;
  private List<String> roles = new ArrayList<>();
  private LocalDateTime lastLogin;

  @Version // optimistic locking
  private Long version;

  public static List<UserEvent> create(CreateUserRequest request, String userId) {
    UserCreated userCreatedEvent = new UserCreated(userId)
        .name(request.name())
        .email(request.email())
        .departmentId(Objects.requireNonNull(request.departmentId()));

    List<UserRoleAssigned> rolesAssignedEvent = request.roles().stream()
        .map(role -> new UserRoleAssigned(userId).role(role))
        .toList();

    return Stream.concat(
        Stream.of(userCreatedEvent),
        rolesAssignedEvent.stream()
    ).toList();
  }

  public List<UserEvent> confirmEmail(String email) {
    if (!email.equals(this.email)) {
      throw new IllegalArgumentException("Email mismatch: " + email + " vs " + this.email);
    }
    // A) traditional: overwrite the old state
    // this.emailValidated = true;

    // B) event-sourcing: return events, without changing any state
    UserEmailConfirmed event = new UserEmailConfirmed(id).email(email);
    return List.of(event); // a) return events => pure function
//    super.registerEvent(event); // b) add event to AbstractAggregateRoot from Spring
//    AggregateLifecycle.apply(event); // c) publish events vis a global static method
  }

  // Changes to state only happen in this method
  public void apply(UserEvent userEvent) {
    switch (userEvent) {
      case UserCreated event -> {
        this.id = event.aggregateId();
        this.email = event.email();
        this.name = event.name();
        this.emailValidated = false;
        this.departmentId = event.departmentId();
//        this.active = true; // TODO debate: can login before email is confirmed?
      }
      case UserUpdated event -> {
        this.name = event.name();
        this.departmentId = event.departmentId();
      }
      case UserEmailUpdated event -> {
        this.email = event.email();
        this.emailValidated = false;
      }
      case UserEmailConfirmed event -> {
//        if (!event.email().equals(email)) { // TODO discuss: when to validate?
//          throw new IllegalArgumentException("Email mismatch: " + event.email() + " vs " + email);
//        }
        this.emailValidated = true;
        this.active = true; // TODO debate
      }
      case UserDeactivated ignored -> active = false;
      case UserActivated ignored -> active = true;
      case UserRoleAssigned event -> roles.add(event.role());
      case UserRoleRevoked event -> roles.remove(event.role());
      case UserLoggedIn event -> this.lastLogin = event.observedAt();
      case ConfirmationEmailSent ignored -> {
      }
    }
  }

}
