package main.com.pos.download;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import main.com.pos.util.Color;

public class ImageDownloader {

    @SuppressWarnings("CallToPrintStackTrace")
    public void download(String URL, String PATH) {
        Path filePath = Paths.get(PATH);
        if (Files.exists(filePath)) { return; }

        try {
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                try (InputStream in = connection.getInputStream()) {
                    byte[] data = in.readAllBytes();
                    boolean isJpg = URL.toLowerCase().contains(".jpg") || URL.toLowerCase().contains(".jpeg");

                    if (isJpg) {
                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
                        Path pngPath = ensurePngPath(filePath);
                        Files.createDirectories(pngPath.getParent());
                        if (image != null) ImageIO.write(image, "png", pngPath.toFile());
                        else Files.write(pngPath, data); // fallback if parsing fails
                    } else {
                        Files.createDirectories(filePath.getParent());
                        Files.write(filePath, data);
                    }
                }
            } 
            else {
                System.out.println( Color.RED + "Failed to download image. HTTP Code: " + status + Color.RESET);
                System.out.println( Color.RED + " - URL: " + URL + Color.RESET);
                System.out.println( Color.RED + " - Path: " + PATH + Color.RESET);
            }
            connection.disconnect();
        } catch (IOException e) { System.out.println(Color.RED + "An error occurred while downloading the image." + Color.RESET); }
    }

    private Path ensurePngPath(Path original) {
        String fileName = original.getFileName().toString();
        if (fileName.toLowerCase().endsWith(".png")) { return original; }
        String baseName = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
        return original.getParent() == null ? Paths.get(baseName + ".png") : original.getParent().resolve(baseName + ".png");
    }
}
