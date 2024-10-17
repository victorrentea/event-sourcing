package victor.training.sourcing.event;

import lombok.Getter;
import lombok.Setter;
import victor.training.sourcing.common.Event;

import static victor.training.sourcing.event.UserEvent.*;

public sealed abstract class UserEvent extends Event
    permits UserCreated, UserUpdated, UserEmailUpdated,
    UserDeactivated, UserReactivated, UserEmailConfirmed,
    UserRoleAssigned, UserRoleRevoked {

  public UserEvent() {
    aggregateType = "User";
  }

  @Getter
  @Setter
  public static final class UserCreated extends UserEvent {
    private String email;
    private String name;
    private String username;
    private String departmentId;
  }

  @Getter
  @Setter
  public static final class UserUpdated extends UserEvent {
    private String email;
    private String name;
    private String username;
    private String departmentId;
  }

  @Getter
  @Setter
  public static final class UserEmailUpdated extends UserEvent {
    // TODO derived from UserUpdated
    private String email;
  }

  @Getter
  @Setter
  public static final class UserDeactivated extends UserEvent {
  }

  @Getter
  @Setter
  public static final class UserReactivated extends UserEvent {
  }

  @Getter
  @Setter
  public static final class UserEmailConfirmed extends UserEvent {
    // via click on link in email
    // TODO race: change emails twice (first: incorrect, second: correct)
    private String email;
  }

  @Getter
  @Setter
  public static final class UserRoleAssigned extends UserEvent {
    // TODO race with UserCreated
    private String application;
    private String role;
  }

  @Getter
  @Setter
  public static final class UserRoleRevoked extends UserEvent {
    // TODO race with UserRoleAssign (eg from bulk imports)
    private String application;
    private String role;
  }

}
