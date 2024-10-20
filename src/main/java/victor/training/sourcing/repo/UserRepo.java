package victor.training.sourcing.repo;

import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import victor.training.sourcing.aggregate.User;

public interface UserRepo extends JpaRepository<User, String> {
  boolean existsByEmailAndActiveTrue(String email);

  @Query("select nextval ('user_id_seq')")
  Long getNewId();
}
