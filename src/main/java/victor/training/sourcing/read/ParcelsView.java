package victor.training.sourcing.read;

import org.springframework.context.event.EventListener;
import victor.training.sourcing.event.order.OrderShipped;

public class ParcelsView {

  @EventListener
  public void onParcelCreated(OrderShipped event) {
    System.out.println("Parcel created: " + event);
  }
}
