package victor.training.sourcing.event.order;

import lombok.Getter;

@Getter
public final class OrderDelivered extends OrderEvent {
  private final String trackingNumber;

  public OrderDelivered(String orderId, String trackingNumber) {
    super(orderId);
    this.trackingNumber = trackingNumber;
  }
}
