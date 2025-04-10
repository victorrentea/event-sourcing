package victor.training.sourcing.aggregate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.*;
import lombok.Getter;
import victor.training.sourcing.event.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@Entity
@Getter
@Table(name = "USERS")
@JsonAutoDetect(fieldVisibility = ANY) // JSONiefied for snapshots
public class User extends Aggregate<AbstractUserEvent> {
  private String name;
  private String email;
  private Boolean emailValidated;
  private String departmentId;
  private Boolean active;
  private List<String> roles = new ArrayList<>();
  private LocalDateTime lastLogin;

  @Version
  private Long version;

  // Command handler
  public List<AbstractUserEvent> confirmEmail(String email) {
    if (!email.equals(this.email)) {
      throw new IllegalArgumentException("Email mismatch: " + email + " vs " + this.email);
    }
    // A) traditional: overwrite the old state
    // this.emailValidated = true;

    // B) event-sourcing: generate events, without changing any state
    return List.of(new UserEmailConfirmed(id).email(email));
  }

  // Apply an event to change aggregate state
  public void apply(AbstractUserEvent userEvent) {
    switch (userEvent) {
      case UserCreated event -> {
        this.id = event.aggregateId();
        this.email = event.email();
        this.name = event.name();
        this.emailValidated = false;
        this.departmentId = event.departmentId();
//        this.active = true; // TODO discuss: active before or after email confirmation?
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
        if (!event.email().equals(email)) { // TODO discuss: when to validate?
          throw new IllegalArgumentException("Email mismatch: " + event.email() + " vs " + email);
        }
        this.emailValidated = true;
        this.active = true; // TODO discuss: active before or after email confirmation?
//        registerEvent(new UserActivated(this.id));
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
