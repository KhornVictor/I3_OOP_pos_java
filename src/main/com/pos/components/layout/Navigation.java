package main.com.pos.components.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import main.com.pos.components.ui.UI;
import main.com.pos.components.ui.UI.RoundedPanel;
import main.com.pos.model.User;

public class Navigation extends JPanel {
    private JLabel dashboardLabel;

    public Navigation(User user, String tilePanel){ 
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(24, 32, 12, 32));

        removeAll();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 32, 20, 32));
        setBackground(Color.WHITE);
        setOpaque(true);

        dashboardLabel = UI.setLabel(tilePanel, new Font("Segoe UI", Font.BOLD, 20), new Color(30, 41, 59), SwingConstants.LEFT);
        add(dashboardLabel, BorderLayout.WEST);

        // Center section - Search bar
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.setOpaque(false);

        JPanel searchPanel = new RoundedPanel(20, Color.WHITE, new Color(209, 213, 219), new Dimension(400, 40), BorderFactory.createEmptyBorder(8, 12, 8, 12));

        // Search icon (emoji fallback until an image is provided)
        JLabel searchIcon = UI.setInternetIconLabel("https://icons.iconarchive.com/icons/icons8/windows-8/512/Very-Basic-Search-icon.png", 14, 14);
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchIcon.setBorder(new EmptyBorder(0, 0, 0, 8));
        searchPanel.add(searchIcon, BorderLayout.WEST);

        // Search input
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setForeground(new Color(148, 163, 184));
        searchField.setText("Search...");
        searchField.setBorder(BorderFactory.createEmptyBorder());
        searchField.setBackground(Color.WHITE);
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Search...")) {
                    searchField.setText("");
                    searchField.setForeground(new Color(30, 41, 59));
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search...");
                    searchField.setForeground(new Color(148, 163, 184));
                }
            }
        });
        searchPanel.add(searchField, BorderLayout.CENTER);

        centerPanel.add(searchPanel);
        add(centerPanel, BorderLayout.CENTER);

        // Right section - Notification and User profile
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);

        // Notification bell with badge
        JPanel notificationPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw bell icon
                g2.setColor(new Color(100, 116, 139));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                g2.drawImage(UI.internetImage("https://cdn-icons-png.flaticon.com/512/8310/8310386.png"), 0, 0, 30, 30, this);
                
                // Draw notification badge
                g2.setColor(new Color(239, 68, 68));
                g2.fillOval(22, 2, 12, 12);
                
                // Draw badge number
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
                g2.drawString("1", 26, 11);
                
                g2.dispose();
            }
        };
        notificationPanel.setOpaque(false);
        notificationPanel.setPreferredSize(new Dimension(35, 30));
        rightPanel.add(notificationPanel);

        // User profile section
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));
        userPanel.setOpaque(false);

        // User info
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setOpaque(false);
        userInfoPanel.setBorder(new EmptyBorder(0, 0, 0, 12));

        JLabel userName = new JLabel(user.getName());
        userName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userName.setForeground(new Color(30, 41, 59));
        userName.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
        userInfoPanel.add(userName);

        JLabel userRole = new JLabel(user.getRole());
        userRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userRole.setForeground(new Color(100, 116, 139));
        userRole.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
        userInfoPanel.add(userRole);

        userPanel.add(userInfoPanel);

        // User avatar
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw circle background
                g2.setColor(new Color(59, 130, 246));
                g2.fillOval(0, 0, 40, 40);
                
                // Draw user icon
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
                g2.drawImage(UI.internetImage("https://cdn-icons-png.freepik.com/512/147/147142.png"), 0, 0, 40, 40, this);
                
                g2.dispose();
            }
        };
        avatarPanel.setOpaque(false);
        avatarPanel.setPreferredSize(new Dimension(40, 40));
        userPanel.add(avatarPanel);

        rightPanel.add(userPanel);
        add(rightPanel, BorderLayout.EAST);
    }

    public void setTitle(String title) {
        dashboardLabel.setText(title);
        dashboardLabel.revalidate();
        dashboardLabel.repaint();
    }
}
