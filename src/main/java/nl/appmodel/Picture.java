package nl.appmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import java.util.Base64;
@Data
@AllArgsConstructor
public class Picture {
    String base64;
    @SneakyThrows
    public static Picture fromSha(java.nio.file.Path dir, String sha) {
        byte[] fileContent = FileUtils.readFileToByteArray(dir.resolve(sha).toFile());
        return new Picture(Base64.getEncoder().encodeToString(fileContent));
    }
}
