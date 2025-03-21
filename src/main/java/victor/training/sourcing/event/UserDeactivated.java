package victor.training.sourcing.event;

import jakarta.persistence.Entity;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED) //for hibernate only
public final class UserDeactivated extends AbstractUserEvent {
  private String email;
  public UserDeactivated(String userId) {
    super(userId);
  }
}
