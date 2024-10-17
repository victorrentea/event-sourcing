package victor.training.sourcing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.sourcing.event.order.OrderPlaced;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RestApi {
  private final EventProcessor eventProcessor;
  record PlaceOrderRequest(
      String customerId,
      String productId) {
  }
  @PostMapping("/order")
  public void placeOrder(@RequestBody PlaceOrderRequest orderDto) {
    log.info("Received order: {}", orderDto);
    eventProcessor.apply(null,
        new OrderPlaced(orderDto.customerId(), orderDto.productId()));
  }
}
