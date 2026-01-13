package main.com.pos.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DeleteImage {
    public static boolean deleteImage(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            System.out.println(Color.RED + "💔 Image path is empty." + Color.RESET);
            return false;
        }

        try {
            String fullPath = System.getProperty("user.dir") + "/src/main/com/pos/resources" + imagePath;
            Path path = Paths.get(fullPath);

            if (!Files.exists(path)) {
                System.out.println(Color.RED +  "💔 Image not found: " + fullPath + Color.RESET);
                return false;
            }

            Files.delete(path);
            return true;

        } catch (IOException e) {
            System.out.println(Color.RED + "💔 Error deleting image: " + e.getMessage() + Color.RESET);
            return false;
        }
    }
}
