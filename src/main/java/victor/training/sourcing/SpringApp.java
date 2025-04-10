package victor.training.sourcing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class SpringApp {

  @Bean
  public RestTemplate rest() {
    return new RestTemplate();
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringApp.class, args);
  }

}

