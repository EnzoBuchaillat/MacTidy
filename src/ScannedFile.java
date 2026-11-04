import java.nio.file.Path;

public record ScannedFile(Path path, long sizeBytes, long inactiveDays) {
}
