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
public final class UserEmailConfirmed extends AbstractUserEvent {
  // via click on link in email
  // TODO race: change emails twice (first: incorrect, second: correct)
  private String email;

  public UserEmailConfirmed(String userId) {
    super(userId);
  }
}
