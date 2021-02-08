package nl.appmodel;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
@Data
@Entity
@Table(name = "most_used", schema = "prosite")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@RequiredArgsConstructor
public class MostUsed {
    @Id @NonNull     long   tag_id;
    @NonNull @Column String name;
    @Column          int    used;
}
