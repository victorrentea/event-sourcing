package victor.training.sourcing.aggregate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;
import java.util.Map;

@Entity
public class User {
  @Id
  private String id;
  private String username;
  private String name;
  private String email;
  private Long departmentId;
  private Boolean active;
  private Map<String, List<String>> appRoles;

  //  @Version
//  private Long version;

}
