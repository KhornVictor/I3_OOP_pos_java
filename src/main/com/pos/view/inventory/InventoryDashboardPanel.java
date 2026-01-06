package main.com.pos.view.inventory;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class InventoryDashboardPanel extends JPanel {

    public InventoryDashboardPanel() {
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(245, 246, 248));
        setBorder(new EmptyBorder(16, 16, 16, 16));

        add(createSummaryPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    /* ================= SUMMARY ================= */

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 16, 0));
        panel.setOpaque(false);

        panel.add(createCard("Total Products", "8", new Color(59, 130, 246)));
        panel.add(createCard("Low Stock Items", "3", new Color(249, 115, 22)));
        panel.add(createCard("Critical Stock", "2", new Color(239, 68, 68)));

        return panel;
    }

    private JPanel createCard(String title, String value, Color accent) {
        RoundedPanel card = new RoundedPanel(20);
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.GRAY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(valueLabel);

        JLabel icon = new JLabel();
        icon.setOpaque(true);
        icon.setBackground(accent);
        icon.setPreferredSize(new Dimension(48, 48));
        icon.setBorder(new EmptyBorder(12, 12, 12, 12));

        card.add(textPanel, BorderLayout.WEST);
        card.add(icon, BorderLayout.EAST);

        return card;
    }

    /* ================= MAIN ================= */

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setOpaque(false);

        panel.add(createSearchBar(), BorderLayout.NORTH);
        panel.add(createTablePanel(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSearchBar() {
        RoundedPanel panel = new RoundedPanel(16);
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 14, 10, 14));

        JTextField search = new JTextField("Search inventory...");
        search.setBorder(null);
        search.setForeground(Color.GRAY);

        panel.add(search, BorderLayout.CENTER);
        return panel;
    }

    /* ================= TABLE ================= */

    private JScrollPane createTablePanel() {
        String[] cols = {
                "Product ID", "Name", "Category",
                "Stock", "Status", "Last Restocked", "Actions"
        };

        Object[][] data = {
                {"P001", "Laptop", "Electronics", "25", "Healthy", "2025-12-01"},
                {"P002", "T-Shirt", "Clothing", "150", "Healthy", "2025-11-28"},
                {"P003", "Coffee", "Beverages", "8", "Critical", "2025-11-25"},
                {"P004", "Headphones", "Electronics", "45", "Healthy", "2025-12-03"},
                {"P005", "Jeans", "Clothing", "5", "Critical", "2025-11-20"}
        };

        JTable table = new JTable(new DefaultTableModel(data, cols));
        table.setRowHeight(46);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowGrid(false);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(Color.WHITE);

        table.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());
        table.getColumnModel().getColumn(6).setCellRenderer(new ActionRenderer());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);

        return scroll;
    }

    /* ================= STATUS BADGE ================= */

    static class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean sel,
                boolean focus, int row, int col) {

            JLabel label = new JLabel(value.toString(), SwingConstants.CENTER);
            label.setOpaque(true);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setBorder(new EmptyBorder(6, 12, 6, 12));

            if ("Healthy".equals(value)) {
                label.setBackground(new Color(220, 252, 231));
                label.setForeground(new Color(22, 163, 74));
            } else {
                label.setBackground(new Color(254, 226, 226));
                label.setForeground(new Color(220, 38, 38));
            }
            return label;
        }
    }

    /* ================= ACTION ICONS ================= */

    static class ActionRenderer extends JPanel implements TableCellRenderer {

        public ActionRenderer() {
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.CENTER, 6, 0));

            add(iconButton("➕", new Color(22, 163, 74)));
            add(iconButton("➖", new Color(220, 38, 38)));
            add(iconButton("👁", new Color(37, 99, 235)));
        }

        private JButton iconButton(String icon, Color color) {
            JButton btn = new JButton(icon);
            btn.setForeground(color);
            btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
            btn.setBorder(null);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            return btn;
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean sel,
                boolean focus, int row, int col) {
            return this;
        }
    }

    /* ================= ROUNDED PANEL ================= */

    static class RoundedPanel extends JPanel {
        private final int radius;

        RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }
}
