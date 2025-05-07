package victor.training.sourcing.event;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED) //for hibernate only
public sealed abstract class UserEvent extends AbstractEvent
    permits
    UserEvent.UserCreated,
    UserEvent.ConfirmationEmailSent,
    UserEvent.UserActivated,
    UserEvent.UserDeactivated,
    UserEvent.UserEmailConfirmed,
    UserEvent.UserEmailUpdated,
    UserEvent.UserLoggedIn,
    UserEvent.UserRoleAssigned,
    UserEvent.UserRoleRevoked,
    UserEvent.UserUpdated {
  public UserEvent(String userId) {
    super(userId, "User");
  }

  @Data
  @Entity
  public static final class UserCreated extends UserEvent {
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String departmentId;
    public UserCreated(String userId) {
      super(userId);
    }
  }

  @Entity
  @Data
  public static final class ConfirmationEmailSent extends UserEvent {
    private String email;

    public ConfirmationEmailSent(String userId) {
      super(userId);
    }
  }

  @Data
  @Entity
  public static final class UserActivated extends UserEvent {
    private String email;

    public UserActivated(String userId) {
      super(userId);
    }
  }

  @Data
  @Entity
  public static final class UserEmailUpdated extends UserEvent {
    private String email;

    public UserEmailUpdated(String userId) {
      super(userId);
    }
  }

  @Entity
  @Data
  public static final class UserEmailConfirmed extends UserEvent {
    // via click on link in email
    // TODO race: change emails twice (first: incorrect, second: correct)
    private String email;

    public UserEmailConfirmed(String userId) {
      super(userId);
    }
  }


  @Entity
  @Data
  public static final class UserLoggedIn extends UserEvent {
    private String application;

    public UserLoggedIn(String userId) {
      super(userId);
    }
  }
  @Entity
  @Data
  public static final class UserRoleAssigned extends UserEvent {
    // TODO race with UserCreated
    private String role;

    public UserRoleAssigned(String userId) {
      super(userId);
    }
  }

  @Entity
  @Data
  public static final class UserRoleRevoked extends UserEvent {
    // TODO race with UserRoleAssign (eg from bulk imports)
    private String role;

    public UserRoleRevoked(String userId) {
      super(userId);
    }
  }

  @Data
  @Entity
  public static final class UserUpdated extends UserEvent {
    private String name;
    private String departmentId;

    public UserUpdated(String userId) {
      super(userId);
    }
  }

  @Entity
  @Data
  public static final class UserDeactivated extends UserEvent {
    private String email;
    public UserDeactivated(String userId) {
      super(userId);
    }
  }
}
