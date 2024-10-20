package victor.training.sourcing.event;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED) //for hibernate only
public final class UserRoleAssigned extends AbstractUserEvent {
  // TODO race with UserCreated
  private String role;

  public UserRoleAssigned(String userId) {
    super(userId);
  }
}
