package main.com.pos.view.setting;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.com.pos.components.ui.UI;
import main.com.pos.model.User;

public class SettingPanel extends JPanel {

    private User currentUser;
    private JLabel welcomeLabel;
    private JLabel userNameLabel;
    private JLabel roleLabel;
    private JLabel dateTimeLabel;

    public SettingPanel(User user) {
        this.currentUser = user;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 248));
        setBorder(new EmptyBorder(40, 40, 40, 40));

        add(createWelcomeSection(), BorderLayout.NORTH);
        add(createGreetingMessage(), BorderLayout.CENTER);
    }

    private JPanel createWelcomeSection() {
        JPanel container = new JPanel(new BorderLayout(20, 0));
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(0, 0, 40, 0));

        // Profile avatar area
        JPanel avatarPanel = createAvatarPanel();
        
        // Welcome text area
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

                // Clip to circle
                Shape circle = new Ellipse2D.Float(0, 0, SIZE, SIZE);
                g2d.setClip(circle);

                // Background
                g2d.setColor(new Color(76, 175, 80));
                g2d.fill(circle);

                // Draw avatar image if exists
                Image avatarImage = null;
                if (currentUser != null && currentUser.getImage() != null) {
                    avatarImage = UI.getImage(currentUser.getImage());
                }

                if (avatarImage != null) {
                    g2d.drawImage(avatarImage, 0, 0, SIZE, SIZE, this);
                } else {
                    // Draw initials fallback
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

                // Border
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

        roleLabel = new JLabel("Role: User");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleLabel.setForeground(new Color(117, 117, 117));

        dateTimeLabel = new JLabel(getCurrentDateTime());
        dateTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateTimeLabel.setForeground(new Color(158, 158, 158));

        panel.add(welcomeLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(userNameLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(roleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(dateTimeLabel);

        return panel;
    }

    private JPanel createGreetingMessage() {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // Main greeting card
        JPanel greetingCard = createRoundedCard(
            new Color(255, 255, 255),
            new Color(230, 230, 230),
            10
        );
        greetingCard.setLayout(new BoxLayout(greetingCard, BoxLayout.Y_AXIS));
        greetingCard.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel greeting = new JLabel("Have a Productive Day!");
        greeting.setFont(new Font("Segoe UI", Font.BOLD, 20));
        greeting.setForeground(new Color(33, 33, 33));
        greeting.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        JLabel subMessage = new JLabel(getGreetingMessage());
        subMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subMessage.setForeground(new Color(117, 117, 117));
        subMessage.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        greetingCard.add(Box.createVerticalStrut(10));
        greetingCard.add(greeting);
        greetingCard.add(Box.createVerticalStrut(10));
        greetingCard.add(subMessage);
        greetingCard.add(Box.createVerticalStrut(10));

        container.add(greetingCard);
        container.add(Box.createVerticalStrut(20));
        return container;
    }


    private JPanel createRoundedCard(Color fillColor, Color strokeColor, int radius) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(fillColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

                g2d.setColor(strokeColor);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

                super.paintComponent(g);
            }
        };
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
