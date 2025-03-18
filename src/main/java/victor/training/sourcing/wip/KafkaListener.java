package victor.training.sourcing.wip;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaListener {
  public class PaymentConfirmed {
    private String paymentId;
    private String orderId;
  }


  //@KafkaListener(topics = "payments")
  public void onPayment(PaymentConfirmed event) { // event is a Kafka message


  }
}
