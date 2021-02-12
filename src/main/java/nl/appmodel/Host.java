package nl.appmodel;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
@Data
@Entity
@Table(name = "host", schema = "prosite")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@RequiredArgsConstructor
public class Host {
    @Id @NonNull String name;
    @Column      int    downloaded;
}
