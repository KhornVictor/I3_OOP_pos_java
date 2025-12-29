
package test.java.com.pos;

import javax.swing.*;
import java.awt.*;

public class POSDashboard extends JFrame {

    public POSDashboard() {
        setTitle("POS System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== Sidebar =====
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(33, 150, 243));
        sidebar.setLayout(new GridLayout(9, 1, 0, 10));

        sidebar.add(menuButton("Dashboard"));
        sidebar.add(menuButton("New Sale"));
        sidebar.add(menuButton("Products"));
        sidebar.add(menuButton("Customers"));
        sidebar.add(menuButton("Inventory"));
        sidebar.add(menuButton("Reports"));
        sidebar.add(menuButton("Settings"));
        sidebar.add(menuButton("Logout"));

        // ===== Main Panel =====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(cardsPanel(), BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    // ===== Menu Button =====
    private JButton menuButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(33, 150, 243));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        return btn;
    }

    // ===== Cards Panel =====
    private JPanel cardsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setOpaque(false);

        panel.add(card("Sales Today", "$12,450"));
        panel.add(card("Transactions", "142"));
        panel.add(card("Products", "1,234"));
        panel.add(card("Customers", "856"));

        return panel;
    }

    // ===== Card =====
    private JPanel card(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // ===== Main Method =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new POSDashboard().setVisible(true);
        });
    }
}
