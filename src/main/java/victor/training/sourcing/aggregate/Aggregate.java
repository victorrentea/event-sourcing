package victor.training.sourcing.aggregate;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.Assert;
import victor.training.sourcing.event.AbstractEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@MappedSuperclass
public abstract class Aggregate<E extends AbstractEvent> {
  @Id
  @Getter
  @Setter
  protected String id;

  @Transient
  private transient final List<Object> domainEvents = new ArrayList<>();

  @DomainEvents
  protected Collection<Object> domainEvents() {
    return Collections.unmodifiableList(domainEvents);
  }

  @AfterDomainEventPublication
  protected void clearDomainEvents() {
    this.domainEvents.clear();
  }

  protected void registerEvent(E event) {
    Assert.notNull(event, "Domain event must not be null");
    this.domainEvents.add(event);
  }

  public abstract void apply(E event);
}
