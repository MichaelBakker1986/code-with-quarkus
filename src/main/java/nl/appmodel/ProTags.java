package nl.appmodel;

import lombok.*;
import lombok.experimental.FieldDefaults;
import nl.appmodel.ProTags.ProTagsId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
@Data
@Entity
@Table(name = "pro_tags", schema = "prosite")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@IdClass(ProTagsId.class)
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
