package main.com.pos.components.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class TableUser {

    // Color constants
    public static final Color HEADER_BG = new Color(45, 52, 71);
    public static final Color HEADER_FG = Color.WHITE;
    public static final Color ROW_BG_1 = Color.WHITE;
    public static final Color ROW_BG_2 = new Color(248, 249, 250);
    public static final Color ROW_HOVER = new Color(230, 240, 255);
    public static final Color ROW_FG = new Color(33, 33, 33);
    public static final Color BORDER_COLOR = new Color(220, 220, 220);
    public static final Color SELECTED_BG = new Color(70, 130, 200);
    public static final Color SELECTED_FG = Color.WHITE;

    /**
     * Apply modern styling to a JTable for User display
     */
    public static void styleTable(JTable table) {
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new ModernHeaderRenderer());
        header.setBackground(HEADER_BG);
        header.setForeground(HEADER_FG);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(0, 35));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        // Row styling
        table.setDefaultRenderer(Object.class, new ModernRowRenderer());
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.setGridColor(BORDER_COLOR);
        table.setRowMargin(0);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setForeground(ROW_FG);
        table.setSelectionBackground(SELECTED_BG);
        table.setSelectionForeground(SELECTED_FG);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);

        // Selection settings
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Modern header renderer with gradient effect
     */
    public static class ModernHeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                      boolean isSelected, boolean hasFocus,
                                                      int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            label.setBackground(HEADER_BG);
            label.setForeground(HEADER_FG);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            label.setHorizontalAlignment(JLabel.LEFT);
            label.setOpaque(true);

            return label;
        }
    }

    /**
     * Modern row renderer with alternating colors
     */
    public static class ModernRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                      boolean isSelected, boolean hasFocus,
                                                      int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                label.setBackground(SELECTED_BG);
                label.setForeground(SELECTED_FG);
            } else {
                // Alternating row colors
                if (row % 2 == 0) {
                    label.setBackground(ROW_BG_1);
                } else {
                    label.setBackground(ROW_BG_2);
                }
                label.setForeground(ROW_FG);
            }

            label.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            label.setOpaque(true);

            return label;
        }
    }

    /**
     * Role badge renderer for User table
     * Displays different colors based on user role (Admin, Manager, Cashier, etc.)
     */
    public static class RoleBadgeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                      boolean isSelected, boolean hasFocus,
                                                      int row, int column) {
            JLabel label = new JLabel(value != null ? value.toString() : "", JLabel.CENTER);
            label.setOpaque(true);
            label.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
            label.setFont(new Font("Segoe UI", Font.BOLD, 10));

            if (value != null) {
                String role = value.toString().toLowerCase();
                switch (role) {
                    case "admin" -> {
                        label.setBackground(new Color(220, 100, 100));
                        label.setForeground(Color.WHITE);
                    }
                    case "manager" -> {
                        label.setBackground(new Color(100, 150, 220));
                        label.setForeground(Color.WHITE);
                    }
                    case "cashier" -> {
                        label.setBackground(new Color(100, 200, 150));
                        label.setForeground(Color.WHITE);
                    }
                    case "staff" -> {
                        label.setBackground(new Color(180, 180, 200));
                        label.setForeground(Color.WHITE);
                    }
                    default -> {
                        label.setBackground(new Color(200, 200, 200));
                        label.setForeground(new Color(60, 60, 60));
                    }
                }
            }

            return label;
        }
    }

    /**
     * Status badge renderer for User table
     * Displays active/inactive status
     */
    public static class StatusBadgeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                      boolean isSelected, boolean hasFocus,
                                                      int row, int column) {
            JLabel label = new JLabel(value != null ? value.toString() : "", JLabel.CENTER);
            label.setOpaque(true);
            label.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
            label.setFont(new Font("Segoe UI", Font.PLAIN, 10));

            if (value != null) {
                String status = value.toString().toLowerCase();
                switch (status) {
                    case "active" -> {
                        label.setBackground(new Color(200, 240, 200));
                        label.setForeground(new Color(0, 120, 0));
                    }
                    case "inactive" -> {
                        label.setBackground(new Color(255, 200, 200));
                        label.setForeground(new Color(180, 0, 0));
                    }
                    case "suspended" -> {
                        label.setBackground(new Color(255, 235, 180));
                        label.setForeground(new Color(200, 120, 0));
                    }
                    default -> {
                        label.setBackground(new Color(240, 240, 240));
                        label.setForeground(Color.DARK_GRAY);
                    }
                }
            }

            return label;
        }
    }

    /**
     * Center-aligned text renderer for User table
     */
    public static class CenteredRenderer extends DefaultTableCellRenderer {
        public CenteredRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                      boolean isSelected, boolean hasFocus,
                                                      int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                label.setBackground(SELECTED_BG);
                label.setForeground(SELECTED_FG);
            } else {
                if (row % 2 == 0) {
                    label.setBackground(ROW_BG_1);
                } else {
                    label.setBackground(ROW_BG_2);
                }
                label.setForeground(ROW_FG);
            }

            label.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            label.setOpaque(true);

            return label;
        }
    }

    /**
     * Image renderer for displaying user avatars in table
     */
    public static class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                      boolean isSelected, boolean hasFocus,
                                                      int row, int column) {
            JLabel label = new JLabel();
            label.setOpaque(true);
            label.setHorizontalAlignment(JLabel.CENTER);

            if (isSelected) {
                label.setBackground(SELECTED_BG);
            } else {
                if (row % 2 == 0) {
                    label.setBackground(ROW_BG_1);
                } else {
                    label.setBackground(ROW_BG_2);
                }
            }

            if (value != null && value instanceof ImageIcon icon) {
                // Scale image to fit in the cell
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaledImg));
            } else if (value != null) {
                // If it's a file path string, try to load the image
                String imagePath = value.toString();
                try {
                    ImageIcon icon = new ImageIcon(imagePath);
                    if (icon.getIconWidth() > 0) {
                        Image img = icon.getImage();
                        Image scaledImg = img.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                        label.setIcon(new ImageIcon(scaledImg));
                    } else {
                        label.setText("No Image");
                        label.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                    }
                } catch (Exception e) {
                    label.setText("No Image");
                    label.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                }
            }

            return label;
        }
    }

    /**
     * Email renderer with email icon
     */
    public static class EmailRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                      boolean isSelected, boolean hasFocus,
                                                      int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                label.setBackground(SELECTED_BG);
                label.setForeground(SELECTED_FG);
            } else {
                if (row % 2 == 0)  label.setBackground(ROW_BG_1);
                else  label.setBackground(ROW_BG_2);
                label.setForeground(new Color(50, 120, 200)); 
            }

            label.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            label.setOpaque(true);

            return label;
        }
    }
}
