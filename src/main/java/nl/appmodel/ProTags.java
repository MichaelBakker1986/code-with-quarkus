package nl.appmodel;

import lombok.*;
import lombok.experimental.FieldDefaults;
import nl.appmodel.ProTags.ProTagsId;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import java.io.Serializable;
@Data
@Entity
@Table(name = "pro_tags", schema = "prosite")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@IdClass(ProTagsId.class)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@RequiredArgsConstructor
public class ProTags {
    @Id @NonNull long pro;
    @Id @NonNull long tag;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProTagsId implements Serializable {
        long pro;
        long tag;
    }
}
