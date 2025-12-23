package main.com.pos.view.login;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import main.com.pos.controller.LoginController;
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
        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("POS Login", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        formPanel.add(new JLabel("Username: "));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);
        formPanel.add(new JLabel("Password: "));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);
        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        btnLogin = new JButton("Login");
        buttonPanel.add(btnLogin);
        add(buttonPanel, BorderLayout.SOUTH);

        btnLogin.addActionListener(this::handleLogin);
        getRootPane().setDefaultButton(btnLogin);
    }

    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (loginController.login(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new DashboardFrame(username.isBlank() ? "User" : username).setVisible(true);
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
    }
}