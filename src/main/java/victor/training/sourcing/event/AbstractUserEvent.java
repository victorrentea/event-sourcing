package victor.training.sourcing.event;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED) //for hibernate only
public sealed abstract class AbstractUserEvent extends AbstractEvent
    permits
    UserCreated,
    ConfirmationEmailSent,
    UserDeactivated,
    UserEmailConfirmed,
    UserEmailUpdated,
    UserLoggedIn,
    UserActivated,
    UserRoleAssigned,
    UserRoleRevoked,
    UserUpdated {

  public AbstractUserEvent(String userId) {
    super(userId, "User");
  }

}
