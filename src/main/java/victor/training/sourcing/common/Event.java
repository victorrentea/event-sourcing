package victor.training.sourcing.common;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.requireNonNull;


@Getter @Setter
@Entity
public abstract class Event {
  @Id
  protected UUID id = UUID.randomUUID();
  protected LocalDateTime observedAt = LocalDateTime.now();
  protected String type = this.getClass().getSimpleName();
  @NotNull
  protected String aggregateId;
  @NotNull
  protected String aggregateType;
}
