package main.com.pos.view.dashboard;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public class Dashboard {
    public static JPanel Dashboard() {
        JPanel root = new JPanel(new BorderLayout());
        root.setOpaque(false);
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);
        root.add(mainContent, BorderLayout.CENTER);
        return root;
    }
}