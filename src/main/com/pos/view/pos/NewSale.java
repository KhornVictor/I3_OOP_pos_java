package main.com.pos.view.pos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class NewSale extends JPanel {
    // Product and cart data (now with category)
    private final String[][] products = {
        {"Laptop", "$899.99", "Electronics"},
        {"T-Shirt", "$24.99", "Clothing"},
        {"Coffee", "$4.99", "Food"},
        {"Headphones", "$79.99", "Electronics"},
        {"Jeans", "$49.99", "Clothing"},
        {"Water Bottle", "$12.99", "Beverages"},
        {"Smartphone", "$699.99", "Electronics"},
        {"Sneakers", "$89.99", "Sports"},
        {"Sandwich", "$7.99", "Food"},
        {"Juice", "$3.99", "Beverages"},
        {"Backpack", "$39.99", "Sports"},
        {"Tablet", "$449.99", "Electronics"}
    };
    private final Map<String, CartItem> cart = new LinkedHashMap<>();

    // UI components that need to be accessed
    private JPanel cartPanel;
    private JLabel subtotalValue, totalValue;
    private JPanel productGrid;
    private String selectedCategory = "All";
    private JTextField searchField;
    private String searchText = "";

    public NewSale() {
        setLayout(new BorderLayout());
        setBackground(new Color(247, 249, 251));
        setBorder(new EmptyBorder(0, 0, 0, 0));

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(new EmptyBorder(16, 32, 16, 32));
        JLabel title = new JLabel("New Sale");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        topBar.add(title, BorderLayout.WEST);

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setOpaque(false);
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 32));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(0, 8, 0, 8)));
        searchField.setText("");
        searchPanel.add(new JLabel(new ImageIcon()), BorderLayout.WEST); // Placeholder for search icon
        searchPanel.add(searchField, BorderLayout.CENTER);
        topBar.add(searchPanel, BorderLayout.CENTER);

        // Add search listener
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
            private void updateSearch() {
            searchText = searchField.getText().trim().toLowerCase();
            updateProductGrid();
            }
        });

        // Notification and user info (top right)
        JPanel rightTopPanel = new JPanel();
        rightTopPanel.setOpaque(false);
        rightTopPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 18, 0));

        // Notification bell (simple icon using Unicode)
        JLabel notificationIcon = new JLabel("\uD83D\uDD14"); // Bell emoji as placeholder
        notificationIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 22));
        notificationIcon.setToolTipText("Notifications");
        // Notification badge (red dot)
        JPanel notificationPanel = new JPanel(null);
        notificationPanel.setOpaque(false);
        notificationPanel.setPreferredSize(new Dimension(32, 32));
        notificationIcon.setBounds(6, 2, 24, 28);
        notificationPanel.add(notificationIcon);
        JLabel badge = new JLabel();
        badge.setOpaque(true);
        badge.setBackground(new Color(231, 76, 60));
        badge.setBounds(22, 2, 10, 10);
        badge.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        badge.setPreferredSize(new Dimension(10, 10));
        badge.setText("");
        notificationPanel.add(badge);
        rightTopPanel.add(notificationPanel);

        // User info
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        userPanel.setOpaque(false);
        JLabel userName = new JLabel("Admin User");
        userName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        JLabel userRole = new JLabel("Administrator");
        userRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userRole.setForeground(new Color(120, 120, 120));
        JPanel userTextPanel = new JPanel();
        userTextPanel.setLayout(new BoxLayout(userTextPanel, BoxLayout.Y_AXIS));
        userTextPanel.setOpaque(false);
        userTextPanel.add(userName);
        userTextPanel.add(userRole);
        // User avatar (simple colored circle with icon/initial)
        JLabel avatar = new JLabel("\uD83D\uDC64"); // Unicode bust in silhouette as placeholder
        avatar.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 28));
        avatar.setOpaque(true);
        avatar.setBackground(new Color(33, 150, 243));
        avatar.setForeground(Color.WHITE);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setVerticalAlignment(SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(38, 38));
        avatar.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));
        userPanel.add(userTextPanel);
        userPanel.add(avatar);
        rightTopPanel.add(userPanel);

        topBar.add(rightTopPanel, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Center panel (categories + products)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(new Color(247, 249, 251));
        centerPanel.setBorder(new EmptyBorder(16, 32, 16, 16));

        // Category bar
        JPanel categoryBar = new JPanel();
        categoryBar.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));
        categoryBar.setOpaque(false);
        String[] categories = {"All", "Electronics", "Clothing", "Food", "Beverages", "Home", "Sports"};
        Map<String, JButton> categoryButtons = new HashMap<>();
        for (String cat : categories) {
            JButton btn = new JButton(cat);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setBackground(cat.equals("All") ? new Color(33, 150, 243) : Color.WHITE);
            btn.setForeground(cat.equals("All") ? Color.WHITE : new Color(33, 33, 33));
            btn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
            btn.setPreferredSize(new Dimension(110, 32));
            categoryBar.add(btn);
            categoryButtons.put(cat, btn);
            btn.addActionListener(e -> {
                selectedCategory = cat;
                // Update button styles
                for (Map.Entry<String, JButton> entry : categoryButtons.entrySet()) {
                    if (entry.getKey().equals(selectedCategory)) {
                        entry.getValue().setBackground(new Color(33, 150, 243));
                        entry.getValue().setForeground(Color.WHITE);
                    } else {
                        entry.getValue().setBackground(Color.WHITE);
                        entry.getValue().setForeground(new Color(33, 33, 33));
                    }
                }
                updateProductGrid();
            });
        }
        centerPanel.add(categoryBar, BorderLayout.NORTH);

        // Product grid
        productGrid = new JPanel(new GridLayout(0, 4, 24, 16));
        productGrid.setOpaque(false);
        JScrollPane productScroll = new JScrollPane(productGrid);
        productScroll.setBorder(null);
        productScroll.getViewport().setBackground(new Color(247, 249, 251));
        centerPanel.add(productScroll, BorderLayout.CENTER);

        updateProductGrid();

        add(centerPanel, BorderLayout.CENTER);

        // Right panel (Current Sale)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(340, 0));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(16, 16, 16, 32));

        JLabel currentSaleLabel = new JLabel("Current Sale");
        currentSaleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        rightPanel.add(currentSaleLabel, BorderLayout.NORTH);

        // Cart area (dynamic)
        cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        cartPanel.setBackground(new Color(245, 247, 250));
        cartPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(16, 16, 16, 16)));
        rightPanel.add(cartPanel, BorderLayout.CENTER);

        // Bottom panel (totals and payment)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(16, 0, 0, 0));
        JPanel subtotalPanel = new JPanel(new BorderLayout());
        subtotalPanel.setOpaque(false);
        subtotalPanel.add(new JLabel("Subtotal:"), BorderLayout.WEST);
        subtotalValue = new JLabel("$0.00");
        subtotalValue.setFont(new Font("Segoe UI", Font.BOLD, 15));
        subtotalPanel.add(subtotalValue, BorderLayout.EAST);
        bottomPanel.add(subtotalPanel);
        bottomPanel.add(Box.createVerticalStrut(8));
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setOpaque(false);
        totalPanel.add(new JLabel("Total:"), BorderLayout.WEST);
        totalValue = new JLabel("$0.00");
        totalValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalValue.setForeground(new Color(33, 150, 243));
        totalPanel.add(totalValue, BorderLayout.EAST);
        bottomPanel.add(totalPanel);
        bottomPanel.add(Box.createVerticalStrut(16));
        JPanel payPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        payPanel.setOpaque(false);
        JButton cashBtn = new JButton("Cash");
        cashBtn.setBackground(new Color(46, 204, 113));
        cashBtn.setForeground(Color.WHITE);
        cashBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        cashBtn.setFocusPainted(false);
        cashBtn.setPreferredSize(new Dimension(120, 40));
        JButton cardBtn = new JButton("Card");
        cardBtn.setBackground(new Color(33, 150, 243));
        cardBtn.setForeground(Color.WHITE);
        cardBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        cardBtn.setFocusPainted(false);
        cardBtn.setPreferredSize(new Dimension(120, 40));
        payPanel.add(cashBtn);
        payPanel.add(cardBtn);
        bottomPanel.add(payPanel);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);

        // Initial cart update
        updateCartPanel();
    }


    // Add product to cart or increase quantity
    private void addToCart(String name, String priceStr) {
        CartItem item = cart.get(name);
        if (item == null) {
            item = new CartItem(name, priceStr, 1);
            cart.put(name, item);
        } else {
            item.qty++;
        }
        updateCartPanel();
    }

    // Update product grid based on selected category
    private void updateProductGrid() {
        productGrid.removeAll();
        int columns = 4;
        int rows = 3; // Always show 3 rows
        int maxCells = columns * rows;
        java.util.List<JPanel> cards = new ArrayList<>();
        Dimension cardSize = new Dimension(220, 120);
        // Collect filtered products
        for (String[] prod : products) {
            String prodCategory = prod[2];
            String prodName = prod[0].toLowerCase();
            if ((selectedCategory.equals("All") || prodCategory.equals(selectedCategory)) &&
                (searchText.isEmpty() || prodName.contains(searchText))) {
                JPanel card = new JPanel();
                card.setLayout(new BorderLayout());
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 230, 230)),
                        new EmptyBorder(12, 12, 12, 12)));
                card.setPreferredSize(cardSize);
                card.setMinimumSize(cardSize);
                card.setMaximumSize(cardSize);
                JLabel name = new JLabel(prod[0]);
                name.setFont(new Font("Segoe UI", Font.BOLD, 16));
                name.setAlignmentX(Component.CENTER_ALIGNMENT);
                name.setHorizontalAlignment(SwingConstants.LEFT);
                JLabel price = new JLabel(prod[1]);
                price.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                price.setForeground(new Color(46, 204, 113));
                price.setAlignmentX(Component.CENTER_ALIGNMENT);
                price.setHorizontalAlignment(SwingConstants.LEFT);
                card.add(name, BorderLayout.NORTH);
                card.add(price, BorderLayout.SOUTH);
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                card.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        addToCart(prod[0], prod[1]);
                    }
                });
                cards.add(card);
            }
        }
        // Fill remaining cells with empty panels to keep grid size
        int toAdd = maxCells - cards.size();
        for (int i = 0; i < toAdd; i++) {
            JPanel empty = new JPanel();
            empty.setOpaque(false);
            empty.setPreferredSize(cardSize);
            empty.setMinimumSize(cardSize);
            empty.setMaximumSize(cardSize);
            cards.add(empty);
        }
        productGrid.setLayout(new GridLayout(rows, columns, 24, 16));
        for (JPanel card : cards) {
            productGrid.add(card);
        }
        productGrid.revalidate();
        productGrid.repaint();
    }

    // Update cart UI
    private void updateCartPanel() {
        cartPanel.removeAll();
        double subtotal = 0.0;
        for (CartItem item : cart.values()) {
            JPanel cartItem = new JPanel();
            cartItem.setLayout(new BorderLayout());
            cartItem.setOpaque(false);

            // Top: name and price
            JPanel cartTop = new JPanel(new BorderLayout());
            cartTop.setOpaque(false);
            JLabel cartName = new JLabel(item.name);
            cartName.setFont(new Font("Segoe UI", Font.BOLD, 15));
            JLabel cartPrice = new JLabel(item.priceStr);
            cartPrice.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            cartPrice.setForeground(new Color(33, 150, 243));
            cartTop.add(cartName, BorderLayout.WEST);
            cartTop.add(cartPrice, BorderLayout.EAST);
            cartItem.add(cartTop, BorderLayout.NORTH);

            // Bottom: quantity control centered
            JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
            qtyPanel.setOpaque(false);
            JButton minusBtn = new JButton("-");
            minusBtn.setPreferredSize(new Dimension(32, 32));
            minusBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            minusBtn.setBackground(new Color(231, 76, 60)); // Red
            minusBtn.setForeground(Color.WHITE);
            minusBtn.setFocusPainted(false);
            minusBtn.setBorder(BorderFactory.createLineBorder(new Color(200, 50, 50)));
            JLabel qtyLabel = new JLabel(String.valueOf(item.qty));
            qtyLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            qtyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            qtyLabel.setPreferredSize(new Dimension(24, 32));
            JButton plusBtn = new JButton("+");
            plusBtn.setPreferredSize(new Dimension(32, 32));
            plusBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            plusBtn.setBackground(new Color(46, 204, 113)); // Green
            plusBtn.setForeground(Color.WHITE);
            plusBtn.setFocusPainted(false);
            plusBtn.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96)));
            qtyPanel.add(minusBtn);
            qtyPanel.add(qtyLabel);
            qtyPanel.add(plusBtn);
            cartItem.add(qtyPanel, BorderLayout.SOUTH);

            // Button actions
            minusBtn.addActionListener(e -> {
                if (item.qty > 1) {
                    item.qty--;
                } else {
                    cart.remove(item.name);
                }
                updateCartPanel();
            });
            plusBtn.addActionListener(e -> {
                item.qty++;
                updateCartPanel();
            });

            cartPanel.add(cartItem);
            cartPanel.add(Box.createVerticalStrut(8));

            // Calculate subtotal
            try {
                double price = Double.parseDouble(item.priceStr.replace("$", ""));
                subtotal += price * item.qty;
            } catch (Exception ignored) {}
        }
        cartPanel.revalidate();
        cartPanel.repaint();
        subtotalValue.setText(String.format("$%.2f", subtotal));
        totalValue.setText(String.format("$%.2f", subtotal));
    }

    // Cart item class
    private static class CartItem {
        String name;
        String priceStr;
        int qty;
        CartItem(String name, String priceStr, int qty) {
            this.name = name;
            this.priceStr = priceStr;
            this.qty = qty;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("POS System - New Sale");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1400, 800);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new NewSale());
            frame.setVisible(true);
        });
    }
}
