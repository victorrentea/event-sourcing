package victor.training.sourcing.event.order;

import lombok.Getter;

@Getter
public final class OrderPaid extends OrderEvent {
  private final String paymentId;

  public OrderPaid(String orderId, String paymentId) {
    super(orderId);
    this.paymentId = paymentId;
  }
}
