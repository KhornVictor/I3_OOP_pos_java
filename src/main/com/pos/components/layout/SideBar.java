package main.com.pos.components.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import main.com.pos.components.ui.UI;
import main.com.pos.components.ui.UI.SidebarMenuButton;
import main.com.pos.model.User;
import main.com.pos.view.pos.POSFrame;

public class SideBar extends JPanel {

    public User user;
    private JButton activButton = null;

    public SideBar(User user) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(280, 0));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0, 0, 0, 26)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JPanel logoIconPanel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Rounded background
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Draw logo image if available; fallback to emoji
                java.awt.Image icon = UI.setApplicationIcon("main/com/pos/resources/images/AppIcon.png");
                if (icon != null) g2.drawImage(icon, 10, 8, 24, 24, this);
                else {
                    g2.setColor(new Color(59, 130, 246));
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                    g2.drawString("🛒", 12, 28);
                }
                
                g2.dispose();
            }
        };
        logoIconPanel.setOpaque(false);
        logoIconPanel.setPreferredSize(new Dimension(44, 44));

        JLabel logoText = new JLabel("POS System");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoText.setForeground(new Color(30, 41, 59));
        logoText.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));

        logoPanel.add(logoIconPanel, BorderLayout.WEST);
        logoPanel.add(logoText, BorderLayout.CENTER);
        add(logoPanel, BorderLayout.NORTH);

        // Menu Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridy = 0;

        String[][] menuItems = {
            {"main/com/pos/resources/icons/menu/Dashboard.png", " Dashboard"},
            {"main/com/pos/resources/icons/menu/Sale.png", " New Sale"},
            {"main/com/pos/resources/icons/menu/Products.png", " Products"},
            {"main/com/pos/resources/icons/menu/Customers.png", " Customers"},
            {"main/com/pos/resources/icons/menu/Inventory.png", " Inventory"},
            {"main/com/pos/resources/icons/menu/Reports.png", " Reports"},
            {"main/com/pos/resources/icons/menu/Settings.png", "Settings"}
        };

        for (String[] item : menuItems) {
            SidebarMenuButton menuBtn = new SidebarMenuButton(UI.setIconLabel(item[0], 24, 24), new JLabel(item[1]));
            if (item[1].equals("Dashboard")) {
                activButton = menuBtn;
                menuBtn.setActive(true);
            }

            menuBtn.addActionListener(e -> {

                if (activButton != null) ((SidebarMenuButton) activButton).setActive(false);

                activButton = menuBtn;
                menuBtn.setActive(true);

                String text = item[1].toLowerCase();
                if (text.contains("products")) System.out.println("Products menu clicked");
                else if (text.contains("new sale")) new POSFrame(null).setVisible(true);
                else if (text.contains("reports")) System.out.println("Reports menu clicked");
                else if (text.contains("customers")) System.out.println("Customers menu clicked");
                else if (text.contains("inventory")) System.out.println("Inventory menu clicked");
                else if (text.contains("settings")) System.out.println("Settings menu clicked");
                else System.out.println("Clicked: " + item[1]);
            });

            menuPanel.add(menuBtn, gbc);
            gbc.gridy++;
        }

        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.setOpaque(false);
        scrollWrapper.add(menuPanel, BorderLayout.NORTH);
        add(scrollWrapper, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        SidebarMenuButton logoutButton = new SidebarMenuButton(UI.setIconLabel("main/com/pos/resources/icons/menu/Dashboard.png", 24, 24), new JLabel(" Logout"));
        logoutButton.setPreferredSize(new Dimension(240, 40));
        footerPanel.add(logoutButton);
        add(footerPanel, BorderLayout.SOUTH);
    }
}
