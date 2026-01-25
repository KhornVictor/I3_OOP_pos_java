package main.com.pos.view.setting;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.com.pos.components.ui.UI;
import main.com.pos.dao.AddressDAO;
import main.com.pos.model.Address;
import main.com.pos.model.User;

public class SettingPanel extends JPanel {

    private User currentUser;
    private Address address;
    private final AddressDAO addressDAO = new AddressDAO();
    private JLabel welcomeLabel;
    private JLabel userNameLabel;
    private JLabel roleLabel;
    private JLabel dateTimeLabel;
    private JLabel addressLabel;
    private JLabel emailLabel;


    public SettingPanel(User user) {
        this.currentUser = user;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 248));
        setBorder(new EmptyBorder(40, 40, 40, 40));

        add(createWelcomeSection(), BorderLayout.NORTH);
        add(createRolePermissionPanel(), BorderLayout.CENTER);
        add(createGreetingMessage(), BorderLayout.SOUTH);
    }

    private JPanel createWelcomeSection() {
        JPanel container = new JPanel(new BorderLayout(20, 0));
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(0, 0, 40, 0));

     
        JPanel avatarPanel = createAvatarPanel();
        
      
        JPanel textPanel = createTextPanel();

        container.add(avatarPanel, BorderLayout.WEST);
        container.add(textPanel, BorderLayout.CENTER);

        return container;
    }

    private JPanel createAvatarPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(100, 100));

        JLabel avatarLabel = new JLabel() {

            private final int SIZE = 100;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                
                Shape circle = new Ellipse2D.Float(0, 0, SIZE, SIZE);
                g2d.setClip(circle);

                
                g2d.setColor(new Color(76, 175, 80));
                g2d.fill(circle);

                
                Image avatarImage = null;
                if (currentUser != null && currentUser.getImage() != null) {
                    avatarImage = UI.getImage(currentUser.getImage());
                }

                if (avatarImage != null) {
                    g2d.drawImage(avatarImage, 0, 0, SIZE, SIZE, this);
                } else {
                    
                    String initials = currentUser != null
                            ? String.valueOf(currentUser.getName().charAt(0)).toUpperCase()
                            : "U";

                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 40));
                    FontMetrics fm = g2d.getFontMetrics();
                    int x = (SIZE - fm.stringWidth(initials)) / 2;
                    int y = (SIZE - fm.getHeight()) / 2 + fm.getAscent();
                    g2d.drawString(initials, x, y);
                }

                g2d.setClip(null);

                
                g2d.setColor(new Color(56, 142, 60));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(1, 1, SIZE - 2, SIZE - 2);

                g2d.dispose();
            }

            @Override
            public boolean contains(int x, int y) {
                double dx = x - SIZE / 2.0;
                double dy = y - SIZE / 2.0;
                return dx * dx + dy * dy <= (SIZE / 2.0) * (SIZE / 2.0);
            }
        };

        avatarLabel.setOpaque(false);
        avatarLabel.setPreferredSize(new Dimension(100, 100));
        panel.add(avatarLabel);

        return panel;
    }


    private JPanel createTextPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        welcomeLabel = new JLabel("Welcome Back! " + currentUser.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(33, 33, 33));

        userNameLabel = new JLabel(currentUser.getName());
        userNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        userNameLabel.setForeground(new Color(76, 175, 80));

        roleLabel = new JLabel("Role: " + (currentUser.getRole() != null ? currentUser.getRole() : "User"));
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleLabel.setForeground(new Color(117, 117, 117));

        emailLabel = new JLabel("Email: " + (currentUser.getEmail() != null ? currentUser.getEmail() : "Not Provided"));
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emailLabel.setForeground(new Color(158, 158, 158));

        dateTimeLabel = new JLabel(getCurrentDateTime());
        dateTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateTimeLabel.setForeground(new Color(158, 158, 158));

        address = addressDAO.getById(currentUser.getAddressId());
        String addressString = address != null ? address.getStreet() + ", " + address.getCity() + ", " + address.getState() + ", " + address.getZipCode() + ", " + address.getCountry() : null;
        addressLabel = new JLabel("Address: " + (addressString != null ? addressString : "Not Provided"));
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        addressLabel.setForeground(new Color(158, 158, 158));
        
        panel.add(welcomeLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(userNameLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(roleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(emailLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(dateTimeLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(addressLabel);
        

        return panel;
    }

    private JPanel createGreetingMessage() {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JPanel greetingCard = new JPanel() {
            private final Image bg = new ImageIcon(UI.getImage("images/setting/motivateCardBackground.png")).getImage();
            private final int radius = 25;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                Shape clip = new java.awt.geom.RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), radius, radius);
                g2.setClip(clip);

                g2.drawImage(bg, 0, 0, getWidth(), getHeight(), this);

                // Dark overlay for readability
                g2.setColor(new Color(0, 0, 0, 90));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

                g2.dispose();
            }
        };

        greetingCard.setOpaque(false);
        greetingCard.setLayout(new BoxLayout(greetingCard, BoxLayout.Y_AXIS));
        greetingCard.setBorder(new EmptyBorder(30, 30, 30, 30));
        greetingCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        JLabel greeting = new JLabel("Have a Productive Day!");
        greeting.setFont(new Font("Segoe UI", Font.BOLD, 20));
        greeting.setForeground(Color.WHITE);
        greeting.setAlignmentX(Component.CENTER_ALIGNMENT);
        greeting.setOpaque(false);

        JLabel subMessage = new JLabel(getGreetingMessage());
        subMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subMessage.setForeground(new Color(240, 240, 240));
        subMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        subMessage.setOpaque(false);

        greetingCard.add(Box.createVerticalStrut(10));
        greetingCard.add(greeting);
        greetingCard.add(Box.createVerticalStrut(10));
        greetingCard.add(subMessage);
        greetingCard.add(Box.createVerticalStrut(10));

        container.add(greetingCard);
        container.add(Box.createVerticalStrut(20));

        return container;
    }


    private JPanel createRolePermissionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JLabel title = new JLabel("Role Permissions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel rolesPanel = new JPanel();
        rolesPanel.setLayout(new GridLayout(1, 2, 40, 0));
        rolesPanel.setOpaque(false);

        
        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
        adminPanel.setOpaque(false);
        JLabel adminLabel = new JLabel("Admin");
        adminLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        adminLabel.setForeground(new Color(56, 142, 60));
        adminPanel.add(adminLabel);
        String[] adminPermissions = {
            "Sales & Cashier",
            "Product Management",
            "Customer Management",
            "Inventory Management",
            "Reports & Analytics",
            "User & Setting"
        };
        for (String perm : adminPermissions) {
            JLabel permLabel = new JLabel("- " + perm);
            permLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            permLabel.setForeground(new Color(117, 117, 117));
            permLabel.setBorder(new EmptyBorder(0, 20, 10, 0));
            adminPanel.add(permLabel);
        }
        adminPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);

       
        JPanel cashierPanel = new JPanel();
        cashierPanel.setLayout(new BoxLayout(cashierPanel, BoxLayout.Y_AXIS));
        cashierPanel.setOpaque(false);
        JLabel cashierLabel = new JLabel("Cashier");
        cashierLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        cashierLabel.setForeground(new Color(33, 33, 33));
        cashierPanel.add(cashierLabel);
        String[] cashierPermissions = {
            "Sales & Cashier",
            "User & Setting"
        };
        for (String perm : cashierPermissions) {
            JLabel permLabel = new JLabel("- " + perm);
            permLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            permLabel.setForeground(new Color(117, 117, 117));
            permLabel.setBorder(new EmptyBorder(0, 20, 10, 0));
            cashierPanel.add(permLabel);
        }
        cashierPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);

        rolesPanel.add(adminPanel);
        rolesPanel.add(cashierPanel);
        panel.add(rolesPanel, BorderLayout.CENTER);
        return panel;
    }


    public void updateUserInfo(User user) {
        if (user != null) {
            this.currentUser = user;
            userNameLabel.setText(user.getName() != null ? user.getName() : user.getUsername());
            roleLabel.setText("Role: " + (user.getRole() != null ? user.getRole() : "User"));
            repaint();
        }
    }

    private String getCurrentDateTime() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy 'at' hh:mm a");
        return now.format(formatter);
    }

    private String getGreetingMessage() {
        int hour = java.time.LocalDateTime.now().getHour();
        
        if (hour >= 5 && hour < 12) return "Good morning! Let's have a productive day.";
        else if (hour >= 12 && hour < 17) return "Good afternoon! Keep up the great work.";
        else if (hour >= 17 && hour < 21) return "Good evening! Wrap up your day successfully.";
        else return "Good night! Stay focused and complete your tasks.";
    }
}
