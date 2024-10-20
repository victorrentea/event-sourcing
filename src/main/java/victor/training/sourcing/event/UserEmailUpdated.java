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
public final class UserEmailUpdated extends AbstractUserEvent {
  // TODO derived from UserUpdated
  private String email;

  public UserEmailUpdated(String userId) {
    super(userId);
  }
}
