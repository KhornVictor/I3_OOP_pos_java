package main.com.pos.util;

import java.awt.*;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.swing.*;

public class DragDropImage extends JPanel {

    public final JLabel imageLabel;
    private final ImageTransferHandler transferHandler;
    private File selectedFile; // last imported file

    public DragDropImage(int width, int height, String filename) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, height));

        imageLabel = new JLabel("Drag an image here", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        imageLabel.setBorder(BorderFactory.createDashedBorder(new Color(200, 200,200)));
        transferHandler = new ImageTransferHandler(filename);
        imageLabel.setTransferHandler(transferHandler);

        add(imageLabel);
    }

    public String saveImageToResources(String destFilename) {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "No image selected", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            String resourcesPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "com" + File.separator + "pos" + File.separator + "resources" + File.separator + "images" + File.separator + "product";
            File resourcesDir = new File(resourcesPath);
            if (!resourcesDir.exists()) resourcesDir.mkdirs();
            File destinationFile = new File(resourcesDir, destFilename);
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return "images" + File.separator + "product" + File.separator + destFilename;
        } catch (IOException e) {   
            JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /** Returns the last imported file, or null if none */
    public File getSelectedFile() { return selectedFile; }

    public class ImageTransferHandler extends TransferHandler {

        private String filename;
        private File file;

        public ImageTransferHandler(String filename) {
            this.filename = filename;
        }

        @Override
        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean importData(TransferSupport support) {
            try {
                List<File> files = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                file = files.get(0);
                // update outer reference for easier access
                selectedFile = file;
                ImageIcon icon = new ImageIcon(file.getAbsolutePath());

                Image scaled = icon.getImage().getScaledInstance(
                        imageLabel.getWidth(),
                        imageLabel.getHeight(),
                        Image.SCALE_SMOOTH
                );

                imageLabel.setIcon(new ImageIcon(scaled));
                imageLabel.setText("");

                return true;
            } catch (UnsupportedFlavorException | IOException e) { System.out.println("Error importing image: " + e.getMessage()); }
            return false;
        }

        public File getFile() {
            return file;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }
    }
}
