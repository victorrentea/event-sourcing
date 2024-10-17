package victor.training.sourcing.event.order;

import lombok.Getter;
import victor.training.sourcing.common.ProductId;

import java.util.Map;

// POST /order/{orderId}/ship {items: {productId: quantity}, trackingNumber}
@Getter
public final class OrderShipped extends OrderEvent {
  private final Map<ProductId, Integer> items;
  private final String trackingNumber;
  private final String shippingAddress;

  public OrderShipped(String orderId, Map<ProductId, Integer> items, String trackingNumber, String shippingAddress) {
    super(orderId);
    this.items = items;
    this.trackingNumber = trackingNumber;
    this.shippingAddress = shippingAddress;
  }
}
