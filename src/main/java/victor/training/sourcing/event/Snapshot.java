package victor.training.sourcing.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.hibernate.annotations.Immutable;
import org.springframework.context.event.EventListener;

import java.util.HashMap;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PROTECTED;

@Immutable
@Entity
@Data
public class Snapshot {
  @Id
  @GeneratedValue
  @Setter(NONE)
  private Long id;
  @OneToOne
  @Setter(NONE)
  @ToString.Exclude
  private AbstractEvent event;
  @JsonIgnore
  @Setter(NONE)
  protected String snapshotJson;

  protected Snapshot() {
  } // for hibernate only

  public Snapshot(AbstractEvent event, String snapshotJson) {
    this.event = event;
    this.snapshotJson = snapshotJson;
  }
}
