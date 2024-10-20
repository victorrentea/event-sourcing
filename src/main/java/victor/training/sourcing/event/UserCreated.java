
package victor.training.sourcing.event;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED) //for hibernate only
public final class UserCreated extends AbstractUserEvent {
  private String name;
  private String email;
  private String departmentId;

  public UserCreated(String userId) {
    super(userId);
  }
}