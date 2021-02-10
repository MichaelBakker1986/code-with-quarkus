package nl.appmodel;

import lombok.*;
import lombok.EqualsAndHashCode.Include;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
@Data
@Entity
@Table(name = "tags", schema = "prosite")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@RequiredArgsConstructor
public class Tags {
    @Id @GeneratedValue
    @Include @Column long   id;
    @NonNull @Column String name;
}
