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

        panel.add(UI.statisticCard("$12,450", "Sales Today", "+12.5%", new Color(76, 175, 80), "https://cdn-icons-png.flaticon.com/512/9011/9011529.png"));
        panel.add(UI.statisticCard("142", "Transactions", "+8.2%", new Color(33, 150, 243), "https://cdn-icons-png.freepik.com/512/6439/6439615.png"));
        panel.add(UI.statisticCard("1,234", "Products", "-2.4%", new Color(12, 58, 90), "https://images.vexels.com/media/users/3/200093/isolated/preview/596f0d8cb733b17268752d044976f102-shopping-bag-icon.png"));
        panel.add(UI.statisticCard("856", "Customers", "+15.3%", new Color(156, 39, 176), "https://www.pngplay.com/wp-content/uploads/7/Customer-Transparent-Image.png"));

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

        grid.add(UI.actionCardWithImage("New Sale", "Start a new transaction", "https://custom-images.strikinglycdn.com/res/hrscywv4p/image/upload/c_limit,fl_lossy,h_9000,w_1200,f_auto,q_auto/8103728/118062_699085.png", "https://static.vecteezy.com/system/resources/previews/054/607/637/non_2x/shopping-cart-icon-with-plus-sign-product-sales-increase-concept-vector.jpg"));
        grid.add(UI.actionCardWithImage("Inventory", "Manage stock levels", "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400&h=300&fit=crop", "https://cdn-icons-png.flaticon.com/512/7656/7656399.png"));
        grid.add(UI.actionCardWithImage("Customers", "View customer database", "https://images.unsplash.com/photo-1552664730-d307ca884978?w=400&h=300&fit=crop", "https://cdn-icons-png.flaticon.com/512/4814/4814961.png"));
        grid.add(UI.actionCardWithImage("Reports", "View sales analytics", "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=400&h=300&fit=crop", "https://cdn-icons-png.flaticon.com/512/1055/1055644.png"));
        grid.add(UI.actionCardWithImage("Products", "Manage product catalog", "https://images.unsplash.com/photo-1487180144351-b8472da7d491?w=400&h=300&fit=crop", "https://cdn-icons-png.freepik.com/256/6444/6444149.png"));
        grid.add(UI.actionCardWithImage("Settings", "System configuration", "https://images.unsplash.com/photo-1561070791-2526d30994b5?w=400&h=300&fit=crop", "https://cdn-icons-png.flaticon.com/512/306/306433.png"));

        container.add(title, BorderLayout.NORTH);
        container.add(grid, BorderLayout.CENTER);

        return container;
    }

    
}


