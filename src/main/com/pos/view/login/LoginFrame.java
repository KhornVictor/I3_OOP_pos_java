package main.com.pos.view.login;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import main.com.pos.controller.LoginController;
import main.com.pos.model.User;
import main.com.pos.service.AuthService;
import main.com.pos.view.dashboard.DashboardFrame;

public class LoginFrame extends JFrame {

    private final LoginController loginController;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        this(new LoginController(new AuthService()));
    }

    public LoginFrame(LoginController loginController) {
        this.loginController = loginController;
        initUi();
    }

    private void initUi() {
        setTitle("Login");
        try {
            var image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("main/com/pos/resources/images/AppIcon.png"));
            setIconImage(image);
        } catch (IOException e) {
            System.err.println("❌ Failed to load app icon.");
        }
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ImageBackgroundPanel background = new ImageBackgroundPanel("main/com/pos/resources/images/LoginBackground.png");
        background.setLayout(new GridBagLayout());
        add(background, BorderLayout.CENTER);

        JPanel card = new RoundedPanel(18, new Color(255, 255, 255, 235), new Color(216, 224, 233));
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(500, 480));
        card.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        background.add(card, new GridBagConstraints());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Point of Sale", SwingConstants.CENTER);
        lblTitle.setFont(new Font("JetBrains Mono SemiBold", Font.BOLD, 28));
        lblTitle.setForeground(new Color(29, 53, 87));
        gbc.gridy = 0;
        card.add(lblTitle, gbc);

        JLabel lblSubtitle = new JLabel("Welcome back! Please sign in.", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
        lblSubtitle.setForeground(new Color(76, 86, 106));
        gbc.gridy = 1;
        card.add(lblSubtitle, gbc);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        gbc.gridy = 2;
        card.add(formPanel, gbc);

        GridBagConstraints fg = new GridBagConstraints();
        fg.gridx = 0;
        fg.gridy = 0;
        fg.insets = new Insets(6, 0, 6, 0);
        fg.anchor = GridBagConstraints.WEST;
        fg.fill = GridBagConstraints.HORIZONTAL;
        fg.weightx = 1;

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("JetBrains Mono SemiBold", Font.PLAIN, 14));
        lblUsername.setForeground(new Color(55, 65, 81));
        formPanel.add(lblUsername, fg);

        fg.gridy++;
        txtUsername = new JTextField();
        styleInput(txtUsername);
        formPanel.add(txtUsername, fg);

        fg.gridy++;
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("JetBrains Mono SemiBold", Font.PLAIN, 14));
        lblPassword.setForeground(new Color(55, 65, 81));
        formPanel.add(lblPassword, fg);

        fg.gridy++;
        txtPassword = new JPasswordField();
        styleInput(txtPassword);
        formPanel.add(txtPassword, fg);

        gbc.gridy = 3;
        btnLogin = new RoundedButton("Sign In", 14);
        stylePrimaryButton(btnLogin);
        card.add(btnLogin, gbc);

        gbc.gridy = 4;
        JLabel footer = new JLabel("Secure access • POS Suite", SwingConstants.CENTER);
        footer.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        footer.setForeground(new Color(100, 116, 139));
        card.add(footer, gbc);

        btnLogin.addActionListener(this::handleLogin);
        getRootPane().setDefaultButton(btnLogin);
    }

    private void styleInput(JTextField field) {
        field.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(214, 226, 245), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
    }

    private void stylePrimaryButton(JButton button) {
        button.setFont(new Font("JetBrains Mono SemiBold", Font.BOLD, 15));
        button.setBackground(new Color(29, 78, 216));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
    }

    private static class ImageBackgroundPanel extends JPanel {
        private Image backgroundImage;
        ImageBackgroundPanel(String imagePath) {
            try { backgroundImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath)); } 
            catch (IOException | NullPointerException e) { System.err.println("❌ Failed to load background image: " + imagePath); }
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if (backgroundImage != null) {
                Graphics2D graphics2d = (Graphics2D) graphics.create();
                graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                graphics2d.dispose();
            }
        }
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color fill;
        private final Color stroke;

        RoundedPanel(int radius, Color fill, Color stroke) {
            this.radius = radius;
            this.fill = fill;
            this.stroke = stroke;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(fill);
            graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), radius * 2, radius * 2);
            graphics2D.setColor(stroke);
            graphics2D.setStroke(new BasicStroke(1f));
            graphics2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius * 2, radius * 2);
            graphics2D.dispose();
            super.paintComponent(graphics);
        }
    }

    private static class RoundedButton extends JButton {
        private final int radius;

        RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius * 2, radius * 2);
            super.paintComponent(graphics);
            g2.dispose();
        }
    }

    private void handleLogin(@SuppressWarnings("unused") ActionEvent event) {
        String username = txtUsername.getText() == null ? "" : txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        password = password == null ? "" : password.trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter both username and password.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            if (username.isEmpty()) { txtUsername.requestFocusInWindow(); } else { txtPassword.requestFocusInWindow(); }
            return;
        }

        try {
            User user = new User(username, password);
            User authorizedUser = loginController.login(user);

            if (authorizedUser != null) {
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new DashboardFrame(authorizedUser.getUsername() == null || authorizedUser.getUsername().isBlank() ? "User" : authorizedUser.getName()).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
                );
                txtPassword.setText("");
                txtPassword.requestFocusInWindow();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Input Error",
                JOptionPane.ERROR_MESSAGE
            );
            txtPassword.requestFocusInWindow();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "An unexpected error occurred. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}