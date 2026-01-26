package main.com.pos.components.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import main.com.pos.components.ui.UI;
import main.com.pos.components.ui.UI.SidebarMenuButton;
import main.com.pos.model.User;
import main.com.pos.view.dashboard.DashboardPanel;
import main.com.pos.view.inventory.InventoryDashboardPanel;
import main.com.pos.view.pos.NewSale;
import main.com.pos.view.product.ProductPanel;
import main.com.pos.view.report.ReportPanel;
import main.com.pos.view.setting.SettingPanel;
import main.com.pos.view.user.UserPanel;
import main.com.pos.view.welcome.WelcomePanel;

public class SideBar extends JPanel {

    public User user;
    private JButton activButton = null;
    private final java.util.Map<String, SidebarMenuButton> menuButtons = new java.util.HashMap<>();

    public SideBar(User user, Navigation navigation, ContentPanel contentPanel) {
        this.user = user;
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
                

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                

                Image icon = UI.internetImage("https://www.possystems.com/images/company/Logo256px.png");
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

        logoPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                navigation.setTitle("Dashboard");
                contentPanel.removeAll();
                contentPanel.add(new WelcomePanel(user), BorderLayout.CENTER);
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });

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
            {"images/dashboard/icons/dashboard.png", " Dashboard"},
            {"images/dashboard/icons/sales.png", " New Sale"},
            {"images/dashboard/icons/products.png", " Products"},
            {"images/dashboard/icons/customers.png", " Users"},
            {"images/dashboard/icons/inventory.png", " Inventory"},
            {"images/dashboard/icons/reports.png", " Reports"},
            {"images/dashboard/icons/settings.png", " Settings"}
        };

        for (String[] item : menuItems) {
            // Check if the menu item should be visible for this user's role
            if (!isMenuItemVisibleForRole(item[1].trim(), user.getRole())) {
                continue;
            }
            
            SidebarMenuButton menuBtn = new SidebarMenuButton(item[0], new JLabel(item[1]), 
                Color.WHITE, Color.BLACK,
                new Color(255, 255, 255), new Color(226, 232, 240), new Color(59, 130, 246), new Color(37, 99, 235)
            );
            
            // Store button reference
            String menuKey = item[1].trim();
            menuButtons.put(menuKey, menuBtn);
            
            if (item[1].equals(" Dashboard")) {
                activButton = menuBtn;
                menuBtn.setActive(true);
            }

            menuBtn.addActionListener(e -> {

                if (activButton != null) ((SidebarMenuButton) activButton).setActive(false);
                

                activButton = menuBtn;
                menuBtn.setActive(true);

                String text = item[1].toLowerCase();
                String titleText = item[1].replaceFirst(" ", "");
                navigation.setTitle(titleText);
                
                if (text.contains("dashboard")) {
                    contentPanel.removeAll();
                    navigation.setTitle("Dashboard");
                    contentPanel.add(new DashboardPanel(contentPanel, user, navigation, this), BorderLayout.CENTER);
                    contentPanel.revalidate();
                    contentPanel.repaint();
                }
                else if (text.contains("products")) {
                    contentPanel.removeAll();
                    navigation.setTitle("Products");
                    contentPanel.add(new ProductPanel(), BorderLayout.CENTER);
                    contentPanel.revalidate();
                    contentPanel.repaint();
                }
                else if (text.contains("new sale")) {
                    navigation.setTitle("NewSale");
                    contentPanel.removeAll();
                    contentPanel.add(new NewSale(), BorderLayout.CENTER);
                    contentPanel.revalidate();
                    contentPanel.repaint();
                }
                else if (text.contains("reports")) {
                    navigation.setTitle("Reports");
                    contentPanel.removeAll();
                    contentPanel.add(new ReportPanel(), BorderLayout.CENTER);
                    contentPanel.revalidate();
                    contentPanel.repaint();
                }
                else if (text.contains("users")) {
                    navigation.setTitle("Users");
                    contentPanel.removeAll();
                    contentPanel.add(new UserPanel(), BorderLayout.CENTER);
                    contentPanel.revalidate();
                    contentPanel.repaint();
                }
                else if (text.contains("inventory")){
                    navigation.setTitle("Inventory");
                    contentPanel.removeAll();
                    contentPanel.add(new InventoryDashboardPanel(), BorderLayout.CENTER);
                    contentPanel.revalidate();
                    contentPanel.repaint();
                    contentPanel.revalidate();
                    contentPanel.repaint();
                }
                else if (text.contains("settings")) {
                    navigation.setTitle("Settings");
                    contentPanel.removeAll();
                    contentPanel.add(new SettingPanel(user), BorderLayout.CENTER);
                    contentPanel.revalidate();
                    contentPanel.repaint();
                }
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
        SidebarMenuButton logoutButton = new SidebarMenuButton(
            "images/dashboard/icons/logout.png", 
            new JLabel(" Logout"), 
            Color.black, Color.white,
            new Color(245, 38, 33),
            new Color(174, 18, 13), new Color(245, 38, 33), new Color(245, 38, 33));
        logoutButton.setPreferredSize(new Dimension(240, 40));
        
        // Add logout functionality
        logoutButton.addActionListener(e -> {
            handleLogout(navigation);
        });
        
        footerPanel.add(logoutButton);
        add(footerPanel, BorderLayout.SOUTH);
    }

    public void activateMenu(String menuName) {
        SidebarMenuButton targetButton = menuButtons.get(menuName);
        if (targetButton != null && targetButton != activButton) {
            if (activButton != null) {
                ((SidebarMenuButton) activButton).setActive(false);
            }
            activButton = targetButton;
            targetButton.setActive(true);
        }
    }

    private void handleLogout(@SuppressWarnings("unused") Navigation navigation) {
        // Clear user session
        this.user = null;
        
        // Get the root frame
        javax.swing.JFrame frame = (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
        
        if (frame != null) {
            // Dispose the current dashboard frame
            frame.dispose();
            
            // Show login frame
            main.com.pos.view.login.LoginFrame loginFrame = new main.com.pos.view.login.LoginFrame();
            loginFrame.setVisible(true);
            loginFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            
            System.out.println("✅ User logged out successfully");
        }
    }

    /**
     * Determine if a menu item should be visible based on user role
     * 
     * Rules:
     * - Admin: Can access all menu items
     * - Cashier: Can only access "New Sale" and "Settings"
     * - Other roles: Default to showing all items
     */
    private boolean isMenuItemVisibleForRole(String menuItem, String userRole) {
        if (userRole == null || userRole.isEmpty()) {
            return false;
        }

        String role = userRole.toLowerCase().trim();
        
        // Admin has full access
        if (role.equals("admin")) {
            return true;
        }
        
        // Cashier has limited access
        if (role.equals("cashier")) {
            return menuItem.equalsIgnoreCase("New Sale") || 
                   menuItem.equalsIgnoreCase("Settings");
        }
        
        // Default behavior for other roles
        return false;
    }
}