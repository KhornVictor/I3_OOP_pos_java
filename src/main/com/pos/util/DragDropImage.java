package main.com.pos.util;

import java.awt.*;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.swing.*;

public class DragDropImage extends JPanel {

    public final JLabel imageLabel;
    private File selectedFile; // last imported file
    @SuppressWarnings("unused")
    private final DropTarget dropTarget;

    public DragDropImage(int width, int height, String filename) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, height));

        imageLabel = new JLabel("Drag an image here", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        imageLabel.setBorder(BorderFactory.createDashedBorder(new Color(200, 200, 200)));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(245, 245, 245));

        // Set up drop target with proper DropTargetAdapter
        dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    
                    // Handle file drops (from local computer or browser)
                    if (dtde.getTransferable().isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        @SuppressWarnings("unchecked")
                        List<File> files = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        if (!files.isEmpty()) {
                            File file = files.get(0);
                            if (isImageFile(file)) {
                                selectedFile = file;
                                displayImage(file);
                                dtde.dropComplete(true);
                                return;
                            }
                        }
                    }
                    
                    dtde.dropComplete(false);
                } catch (UnsupportedFlavorException | IOException e) {
                    System.out.println("Error importing image: " + e.getMessage());
                    dtde.dropComplete(false);
                }
            }
        });

        add(imageLabel);
    }

    private void displayImage(File file) {
        try {
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            displayImageIcon(icon);
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }
    }

    private void displayImageIcon(ImageIcon icon) {
        Image scaled = icon.getImage().getScaledInstance(
                getWidth() > 0 ? getWidth() - 20 : 280,
                getHeight() > 0 ? getHeight() - 20 : 160,
                Image.SCALE_SMOOTH
        );
        imageLabel.setIcon(new ImageIcon(scaled));
        imageLabel.setText("");
    }

    private boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || 
               name.endsWith(".png") || name.endsWith(".gif") || 
               name.endsWith(".bmp") || name.endsWith(".webp");
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
            
            // Copy the selected file to resources
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            return "images" + File.separator + "product" + File.separator + destFilename;
        } catch (IOException e) {   
            JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error saving image: " + e.getMessage());
            return null;
        }
    }

    public String saveImageUserToResources(String destFilename) {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "No image selected", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            String resourcesPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "com" + File.separator + "pos" + File.separator + "resources" + File.separator + "images" + File.separator + "avatar";
            File resourcesDir = new File(resourcesPath);
            if (!resourcesDir.exists()) resourcesDir.mkdirs();
            File destinationFile = new File(resourcesDir, destFilename);
            
            // Copy the selected file to resources
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            return "images" + File.separator + "avatar" + File.separator + destFilename;
        } catch (IOException e) {   
            JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /** Returns the last imported file, or null if none */
    public File getSelectedFile() { 
        return selectedFile; 
    }
    
    /** Reset the image panel */
    public void resetImage() {
        selectedFile = null;
        imageLabel.setIcon(null);
        imageLabel.setText("Drag an image here");
    }

    public void setImageFromPath(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            resetImage();
            return;
        }
        
        try {
            // Handle relative paths like "/images/product/filename.png"
            String resourcesPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "com" + File.separator + "pos" + File.separator + "resources" + File.separator;
            String cleanPath = imagePath.replace("/", File.separator);
            File imageFile = new File(resourcesPath + cleanPath);
            
            if (imageFile.exists()) {
                selectedFile = imageFile;
                displayImage(imageFile);
            } else {
                System.out.println("⚠️ Image file not found: " + imageFile.getAbsolutePath());
                resetImage();
            }
        } catch (Exception e) {
            System.out.println("Error loading image from path: " + e.getMessage());
            resetImage();
        }
    }

    public String getSelectedImagePath() {
        if (selectedFile == null) return null;
        return selectedFile.getAbsolutePath();
    }
}