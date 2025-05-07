package victor.training.sourcing;

import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import victor.training.sourcing.command.UserRestApi;
import victor.training.sourcing.command.UserRestApi.CreateUserRequest;

import java.util.List;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(PER_CLASS)
@TestMethodOrder(MethodName.class)
public class ApiTest {
  @Autowired
  UserRestApi api;

  String newUserId;

  @Test
  void _1_create() {
   CreateUserRequest createRequest = new CreateUserRequest(
      "a@b.com",
      "John",
      "dep1",
      List.of("app1:ADMIN"));

    newUserId = api.createUser(createRequest);
  }

  @Test
  void _2_confirmEmail() {
    api.confirmEmail(newUserId, "a@b.com");
  }

  @Test
  void _3_create_duplicated_email_fails() {
    api.createUser(new CreateUserRequest(
        "acom",
        "John",
        "dep1",
        List.of("ADMIN"))); // fails!
  }
}
