package victor.training.sourcing.aggregate;

import victor.training.sourcing.common.Event;
import victor.training.sourcing.common.ProductId;
import victor.training.sourcing.event.order.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {
  private String id;
  private String customerId;
  private Map<ProductId, Integer> products;
  private String shippingAddress;
  private LocalDateTime placedAt;

  private boolean confirmed;
  private boolean paid;
  private boolean cancelled;

  private final Map<String, Map<ProductId, Integer>> parcels = new HashMap<>();
  private final Map<ProductId, Integer> deliveredProducts = new HashMap<>();

  public List<Event> confirm() {
    if (confirmed) {
      throw new IllegalStateException("Order already confirmed");
    }
    return List.of(new OrderConfirmed(id));
  }
  public List<Event> pay(String paymentId) {
    if (!confirmed) throw new IllegalStateException("Order not confirmed");
    if (paid) throw new IllegalStateException("Order already paid");
    return List.of(new OrderPaid(id, paymentId));
  }

  public void onEvent(OrderEvent event) {
    switch (event) {
      case OrderConfirmed orderConfirmed -> confirmed = true;
      case OrderPaid orderPaid -> paid = true;
      case OrderShipped orderShipped -> parcels.put(orderShipped.trackingNumber(), new HashMap<>(products));
      case OrderDelivered orderDelivered -> {
        Map<ProductId, Integer> products = parcels.remove(orderDelivered.trackingNumber());
        products.forEach((productId, count) -> deliveredProducts.merge(productId, count, Integer::sum));
      }
      case OrderCancelled orderCancelled -> cancelled = true;
      case OrderPlaced orderPlaced -> {
        id = orderPlaced.aggregateId();
        products = orderPlaced.products();
        customerId = orderPlaced.customerId();
        shippingAddress = orderPlaced.shippingAddress();
        placedAt = orderPlaced.observedAt();
      }
    }
  }
}

//class OrderService {
//  public void onOrderPlaced(OrderPlaced event) {
//    Order order = Order.onOrderPlaced(event);
//    orderRepository.save(order);
//  }
//
//  public void onOrderPaid(OrderPlaced event) {
//    Order order = orderRepository.findById(event.aggregateId());
////    order.setPaid(true);
//    order.onEvent(true);// sau
//  }
//}