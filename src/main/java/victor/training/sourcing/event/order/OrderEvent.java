package victor.training.sourcing.event.order;

import victor.training.sourcing.common.Event;

public abstract sealed class OrderEvent extends Event
  permits OrderPaid, OrderConfirmed, OrderCancelled, OrderPlaced, OrderShipped,
    OrderDelivered {
  public OrderEvent(String orderId) {
    super(orderId, "Order");
  }
}
