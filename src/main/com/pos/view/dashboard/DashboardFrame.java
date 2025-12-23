package main.com.pos.view.dashboard;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DashboardFrame extends JFrame {

    public DashboardFrame(String username) {
        setTitle("Dashboard");
        setSize(500, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lblWelcome = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblWelcome, BorderLayout.NORTH);

        JLabel lblInfo = new JLabel("Select a module to continue.", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        add(lblInfo, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        actionPanel.add(new JButton("Products"));
        actionPanel.add(new JButton("POS"));
        actionPanel.add(new JButton("Reports"));
        add(actionPanel, BorderLayout.SOUTH);
    }
}
