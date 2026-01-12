package main.com.pos.view.dashboard;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.com.pos.components.ui.UI;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 248));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setVisible(true);
        add(createTopCards(), BorderLayout.NORTH);
        add(createQuickActions(), BorderLayout.CENTER);
    }

    private JPanel createTopCards() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 25, 0));

        panel.add(UI.statisticCard("$12,450", "Sales Today", "+12.5%", new Color(76, 175, 80), "images/icons/dashboard/statistic/SaleToday.png"));
        panel.add(UI.statisticCard("142", "Transactions", "+8.2%", new Color(33, 150, 243), "images/icons/dashboard/statistic/Transactions.png"));
        panel.add(UI.statisticCard("1,234", "Products", "-2.4%", new Color(12, 58, 90), "images/icons/dashboard/statistic/Products.png"));
        panel.add(UI.statisticCard("856", "Customers", "+15.3%", new Color(156, 39, 176), "images/icons/dashboard/statistic/Customers.png"));

        return panel;
    }

    private JPanel createQuickActions() {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);

        JLabel title = new JLabel("Quick Actions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel grid = new JPanel(new GridLayout(2, 3, 20, 20));
        grid.setOpaque(false);

        grid.add(UI.actionCardWithImage("New Sale", "Start a new transaction", "images/cards/dashboard/SalesOverviewCard.png", "images/icons/dashboard/actions/NewSale.png", new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleActionCardClick("New Sale");
            }
        }));
        grid.add(UI.actionCardWithImage("Inventory", "Manage stock levels", "images/cards/dashboard/InventoryCard.png", "images/icons/dashboard/actions/Inventory.png", new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleActionCardClick("Inventory");
            }
        }));
        grid.add(UI.actionCardWithImage("Customers", "View customer database", "images/cards/dashboard/CustomersCard.png", "images/icons/dashboard/actions/Customers.png", new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleActionCardClick("Customers");
            }
        }));
        grid.add(UI.actionCardWithImage("Reports", "View sales analytics", "images/cards/dashboard/ReportsCard.png", "images/icons/dashboard/actions/Reports.png", new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleActionCardClick("Reports");
            }
        }));
        grid.add(UI.actionCardWithImage("Products", "Manage product catalog", "images/cards/dashboard/ProductsCard.png", "images/icons/dashboard/actions/Products.png", new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleActionCardClick("Products");
            }
        }));
        grid.add(UI.actionCardWithImage("Settings", "System configuration", "images/cards/dashboard/SettingsCard.png", "images/icons/dashboard/actions/Settings.png", new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleActionCardClick("Settings");
            }
        }));

        container.add(title, BorderLayout.NORTH);
        container.add(grid, BorderLayout.CENTER);

        return container;
    }

    private static void handleActionCardClick(String action){
        switch (action) {
            case "New Sale" -> {
                System.out.println("New Sale action triggered.");
            }
            case "Inventory" -> System.out.println("Inventory action triggered.");
            case "Customers" -> System.out.println("Customers action triggered.");
            case "Reports" -> System.out.println("Reports action triggered.");
            case "Products" -> System.out.println("Products action triggered.");
            case "Settings" -> System.out.println("Settings action triggered.");
            default -> System.out.println("Unknown action: " + action);
        }
    }
}