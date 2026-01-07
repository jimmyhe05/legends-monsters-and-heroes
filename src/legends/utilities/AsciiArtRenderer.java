package legends.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility to load and render ASCII art banners.
 */
public class AsciiArtRenderer {

    /**
     * Load ASCII art from a file and return it as a string, or null on failure.
     */
    public static String load(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return null;
        }
        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Print ASCII art to stdout if available.
     */
    public static void render(String filePath) {
        String art = load(filePath);
        if (art != null) {
            System.out.println(art);
        }
    }
}
