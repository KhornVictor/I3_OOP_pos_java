package main.com.pos.view.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import main.com.pos.components.layout.Layout;
import main.com.pos.components.ui.UI;
import main.com.pos.components.ui.UI.ImageBackgroundPanel;
import main.com.pos.controller.LoginController;
import main.com.pos.model.User;
import main.com.pos.service.AuthService;

public class LoginFrame extends JFrame {

    private final LoginController loginController;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLayeredPane layeredPane;
    private ImageBackgroundPanel background;
    

    public LoginFrame() { this(new LoginController(new AuthService())); }

    public LoginFrame(LoginController loginController) {
        this.loginController = loginController;
        initializeUI();
    }

    private void initializeUI() {

        // initialize components
        background = UI.setBackgroundImage("main/com/pos/resources/images/LoginBackground.png");
        JPanel card = UI.cardPanel(18, new Color(255, 255, 255, 235), new Color(216, 224, 233), new Dimension(500, 480), BorderFactory.createEmptyBorder(100, 100, 100, 100));
        GridBagConstraints gridBagConstraints = UI.setGridBagConstraints(0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 10, 0), 1.0, 0.0);
        JLabel labels[] = {
            UI.setLabel("Point of Sale", new Font("JetBrains Mono SemiBold", Font.PLAIN, 24), new Color(30, 41, 59), SwingConstants.CENTER),
            UI.setLabel("Welcome back! Please sign in.", new Font("JetBrains Mono", Font.PLAIN, 14), new Color(100, 116, 139), SwingConstants.CENTER),
        };
        JPanel formPanel = UI.spacerPanel(new Dimension(400, 200), false);
        txtUsername = UI.setTextInput(new Font("JetBrains Mono", Font.PLAIN, 14), new Color(255, 255, 255), new Color(31, 41, 55), new Color(31, 41, 55), BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(214, 226, 245), 1),BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        txtPassword = UI.setPasswordInput(new Font("JetBrains Mono", Font.PLAIN, 14), new Color(255, 255, 255), new Color(31, 41, 55), new Color(31, 41, 55), BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(214, 226, 245), 1), BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        GridBagConstraints fGridBagConstraints = UI.setGridBagConstraints( 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(8, 0, 8, 0), 1.0, 0.0 );
        JLabel[] inputLabels = {
            UI.setLabel("Username", new Font("JetBrains Mono SemiBold", Font.PLAIN, 14), new Color(55, 65, 81), SwingConstants.LEFT),
            UI.setLabel("Password", new Font("JetBrains Mono SemiBold", Font.PLAIN, 14), new Color(55, 65, 81), SwingConstants.LEFT)
        };
        btnLogin = UI.setButton("Login", new Font("JetBrains Mono SemiBold", Font.PLAIN, 16), new Color(59, 130, 246), Color.WHITE, BorderFactory.createEmptyBorder(10, 0, 10, 0), 8, this::handleLogin);
        JLabel footer = UI.setLabel("Secure access • POS Suite", new Font("JetBrains Mono", Font.PLAIN, 12), new Color(100, 116, 139), SwingConstants.CENTER);   
        
        // build UI
        setTitle("Login");
        setIconImage(UI.setApplicationIcon("main/com/pos/resources/images/AppIcon.png"));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(new BorderLayout());
        setContentPane(layeredPane);
        background.setLayout(new GridBagLayout());
        layeredPane.setLayout(new BorderLayout());
        add(background, BorderLayout.CENTER);
        
        // Center the login card in the background
        background.add(card, UI.setGridBagConstraints(0, 0, 2, 2, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0.5, 0.5));
        for (int i = 0; i < labels.length; i++) {
            gridBagConstraints.gridy = i;
            card.add(labels[i], gridBagConstraints);
        }
        gridBagConstraints.gridy = 2;
        card.add(formPanel, gridBagConstraints);
        for (int i = 0; i < 4; i ++){
            fGridBagConstraints.gridy = i;
            if (i % 2 == 0) formPanel.add(inputLabels[i / 2], fGridBagConstraints);
            else {
                if (i / 2 == 0) formPanel.add(txtUsername, fGridBagConstraints);
                else formPanel.add(txtPassword, fGridBagConstraints);
            }
        }
        gridBagConstraints.gridy = 3;
        card.add(btnLogin, gridBagConstraints);
        gridBagConstraints.gridy = 4;                                                           
        card.add(footer, gridBagConstraints);
        getRootPane().setDefaultButton(btnLogin);
    }
    
    private void showErrorPanel(JPanel panel, String message, boolean setVisible) {

        JPanel errorPanel = UI.cardPanel(20, new Color(59, 130, 246, 180), new Color(59, 130, 246), null, BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel errorLabel = UI.setLabel(message, new Font("JetBrains Mono SemiBold", Font.PLAIN, 14), Color.RED, SwingConstants.CENTER);
        Timer timer = new Timer(1000, e -> errorPanel.setVisible(false));
       
        panel.add(errorPanel, UI.setGridBagConstraints(1, 1, 1, 1, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 30, 30), 1.0, 1.0));
        errorPanel.add(errorLabel);
        errorPanel.setVisible(setVisible);
        timer.setRepeats(false);
        timer.start();
        
    }

    private void handleLogin(@SuppressWarnings("unused") ActionEvent event) {
        String username = txtUsername.getText() == null ? "" : txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        password = password.trim();
        System.out.println("🔃 Attempting login for user: " + username);
        if (username.isEmpty() || password.isEmpty()) {
            showErrorPanel(background, "Username and password cannot be empty.", true);
            if (username.isEmpty()) { txtUsername.requestFocusInWindow(); } else { txtPassword.requestFocusInWindow(); }
            return;
        }

        try {
            User user = new User(username, password);
            User authorizedUser = loginController.login(user);

            if (authorizedUser != null) {
                System.out.println("✅ Login successful for user: " + authorizedUser.getUsername());
                new Layout(authorizedUser).setVisible(true);
                dispose();
            } else {
                showErrorPanel(background, "Invalid username or password.", true);
                txtPassword.setText("");
                txtPassword.requestFocusInWindow();
            }
        } catch (IllegalArgumentException ex) {
            showErrorPanel(background, "Invalid username or password.", true);
            txtPassword.requestFocusInWindow();
        } catch (HeadlessException ex) {
            showErrorPanel(background, "An error occurred during login. Please try again.", true);
            System.err.println("❌ Error during login: " + ex.getMessage());
        }
    }
}