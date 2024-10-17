package victor.training.sourcing.event.order;

import lombok.Getter;
import victor.training.sourcing.common.ProductId;

import java.util.Map;

@Getter
public final class OrderPlaced extends OrderEvent {
  private final String customerId;
  private final String shippingAddress;
  private final Map<ProductId, Integer> products;

  public OrderPlaced(
      String orderId,
      String customerId,
      String shippingAddress,
      Map<ProductId, Integer> products) {
    super(orderId);
    this.customerId = customerId;
    this.shippingAddress = shippingAddress;
    this.products = products;
  }
}


