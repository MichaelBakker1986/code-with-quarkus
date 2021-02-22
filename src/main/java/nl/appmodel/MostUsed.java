package nl.appmodel;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.*;
import lombok.EqualsAndHashCode.Include;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
@Data
@Entity
@Table(name = "most_used", schema = "prosite")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Cacheable
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@RequiredArgsConstructor
public class MostUsed extends PanacheEntityBase {
    @Include @Id @NonNull long   tag_id;
    @NonNull @Column      String name;
    @Column               int    used;
}
