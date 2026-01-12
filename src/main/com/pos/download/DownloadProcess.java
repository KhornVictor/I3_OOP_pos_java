package main.com.pos.download;

import java.io.BufferedReader;
import java.io.FileReader;
import main.com.pos.util.Color;

public class DownloadProcess {
    public void startDownload() {
        String csvFile = "src/main/com/pos/resources/images.csv";
        long startTime = System.currentTimeMillis();
        ImageDownloader downloader = new ImageDownloader();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {

                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }


                String[] columns = line.split(",", 2);
                String imageUrl = columns[0].trim();
                String imagePath = columns[1].trim();

                if (imageUrl.isEmpty() || imagePath.isEmpty()) {
                    System.out.println(Color.RED + "Invalid entry in CSV file: " + line + Color.RESET);
                    continue;
                }

                downloader.download(imageUrl, imagePath);
            }
            long endTime = System.currentTimeMillis();
            System.out.println(Color.GREEN + "🖼️  All images have been processed in " + (endTime - startTime) + " ms" + Color.RESET);
        } catch (Exception e) { System.out.println(Color.RED + "An error occurred while reading the CSV file." + Color.RESET); }
    }
}
