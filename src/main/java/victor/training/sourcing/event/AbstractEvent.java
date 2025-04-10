package victor.training.sourcing.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static jakarta.persistence.InheritanceType.SINGLE_TABLE;
import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PROTECTED;


@Getter
@Setter
@Entity
@Table(name = "events")
@Immutable
@Inheritance(strategy = SINGLE_TABLE)
@JsonAutoDetect(fieldVisibility = ANY)
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
@ToString
@NoArgsConstructor(access = PROTECTED) //for hibernate only
public abstract class AbstractEvent {
  @Id
  @GeneratedValue
  @Setter(NONE)
  protected Long eventId;
  protected LocalDateTime observedAt = LocalDateTime.now();
  @Setter(NONE)
  protected String eventType = this.getClass().getSimpleName();
  @NotNull
  protected String aggregateId;
  @NotNull
  protected String aggregateType;

//  protected String userAuthor; TODO

  protected boolean replay = false;

  @JsonIgnore
  @OneToOne(mappedBy = "event")
  @ToString.Exclude
  private Snapshot snapshot;

  @SneakyThrows
  @JsonProperty
  public Map<?,?> getSnapshot() {
    if (snapshot == null) {
      return Map.of();
    }
    return new ObjectMapper().readValue(snapshot.snapshotJson, LinkedHashMap.class);
  }

  protected AbstractEvent(String aggregateId, String aggregateType) {
    this.aggregateId = aggregateId;
    this.aggregateType = aggregateType;
  }

}
