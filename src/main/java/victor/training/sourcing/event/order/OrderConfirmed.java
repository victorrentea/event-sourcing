package victor.training.sourcing.event.order;

public final class OrderConfirmed extends OrderEvent {
  public OrderConfirmed(String orderId) {
    super(orderId);
  }
}
