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

  // Step1: command (sync or async) results in event(s)
  public List<AbstractUserEvent> confirmEmail(String email) {
    if (email.equals(this.email)) {
      return List.of(new UserEmailConfirmed(id).email(email));
    } else {
      throw new IllegalArgumentException("Email mismatch: " + email + " vs " + this.email);
    }
  }

  // Step2: the event(s) are applied to the aggregate to change its state
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
        if (!event.email().equals(email)) { // TODO discuss: how to handle validation when applying the event?
          throw new IllegalArgumentException("Email mismatch: " + event.email() + " vs " + email);
        }
        this.emailValidated = true;
        this.active = true; // TODO discuss: active before or after email confirmation?
//        registerEvent(new UserActivated(this.id));
      }
      case UserDeactivated event -> active = false;
      case UserActivated event -> active = true;
      case UserRoleAssigned event -> roles.add(event.role());
      case UserRoleRevoked event -> roles.remove(event.role());
      case UserLoggedIn event -> this.lastLogin = event.observedAt();
      case ConfirmationEmailSent confirmationEmailSent -> {
      }
    }
  }

}
