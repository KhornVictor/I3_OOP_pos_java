package main.com.pos.components.ui;

import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class UI extends JFrame {
    public static void setApplicationIcon(JFrame frame, String iconPath) {
        try {
            var image = ImageIO.read(UI.class.getClassLoader().getResourceAsStream(iconPath));
            frame.setIconImage(image);
        } catch (IOException e) { System.err.println("❌ Failed to load app icon."); }
    }
}
