package nl.appmodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
@Entity
@Table(name = "pro", schema = "prosite")
@Slf4j
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Pro {
    @Id @GeneratedValue @Include @Column(name = "id")
    private long    id;
    @Basic @Column(name = "thumbs")
    private String  thumbs;
    @Basic @Column(name = "downloaded", nullable = false)
    private boolean downloaded = true;
    @Basic @Column(name = "views")
    private int     views;
    @Basic @Column(name = "header")
    private String  header;
    @Basic @Column(name = "embed")
    private String  embed;
    public String getSuffix() {
        return ".jpg";
    }
    public String getFileName() {
        var s        = thumbs.split("[,;]")[0];
        var filename = s.substring(s.lastIndexOf("/") + 1);
        return filename.substring(0, filename.length() - 4);
    }
}
