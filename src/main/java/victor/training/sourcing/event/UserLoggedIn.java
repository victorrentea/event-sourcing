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
public final class UserLoggedIn extends AbstractUserEvent {
  private String application;
  private String email;

  public UserLoggedIn(String userId) {
    super(userId);
  }
}
