package victor.training.sourcing;

import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import victor.training.sourcing.command.UserRestApi;
import victor.training.sourcing.command.UserRestApi.CreateUserRequest;

import java.util.List;

@SpringBootTest
@Profile("test")
//@TestInstance(PER_CLASS)
@TestMethodOrder(MethodName.class)
public class ApiTest {
  @Autowired
  UserRestApi api;

   CreateUserRequest createRequest = new CreateUserRequest(
      "a@b.com",
      "John",
      "dep1",
      List.of("app1:ADMIN"));

//  @BeforeEach
//  final void setup() {
//    api.createUser(createRequest);
//  }

  @Test
  void create() {
    api.createUser(createRequest);

  }

  @Test
  void step2_confirmEmail() {
    api.confirmEmail("userId", "a@b.com");
  }

  @Test
  void step3_create_duplicated_email() {
    api.createUser(new CreateUserRequest(
        "a@b.com",
        "John",
        "dep1",
        List.of("ADMIN"))); // fails!
  }
}
