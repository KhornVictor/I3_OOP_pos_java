package main.com.pos.components.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Main Panel Component
 * Manages the main content area of the application
 */
public class MainPanel extends JPanel {

    public MainPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }
}
