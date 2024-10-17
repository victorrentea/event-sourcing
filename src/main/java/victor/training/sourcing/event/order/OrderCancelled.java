package victor.training.sourcing.event.order;

public final class OrderCancelled extends OrderEvent {
  public OrderCancelled(String orderId) {
    super(orderId);
  }
}
