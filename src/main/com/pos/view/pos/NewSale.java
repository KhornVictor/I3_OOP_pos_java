package main.com.pos.view.pos;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.com.pos.dao.CategoryDAO;
import main.com.pos.dao.ProductDAO;
import main.com.pos.model.Category;
import main.com.pos.model.Product;


public class NewSale extends JPanel {
    // Dynamic product and cart data
    private List<Product> products = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private final Map<Integer, CartItem> cart = new LinkedHashMap<>();

    // UI components that need to be accessed
    private JPanel cartPanel;
    private JLabel subtotalValue, totalValue;
    private JPanel productGrid;
    private JComboBox<String> categoryComboBox;
    private String selectedCategory = "All";
    private JTextField searchField;
    private String searchText = "";

    public NewSale() {
        setLayout(new BorderLayout());
        setBackground(new Color(247, 249, 251));
        setBorder(new EmptyBorder(0, 0, 0, 0));

        // Top bar (modern style)
        JPanel topBar = new JPanel(new BorderLayout(10, 0));
        topBar.setBackground(new Color(245, 247, 250));
        topBar.setBorder(new EmptyBorder(20, 32, 20, 32));

        JLabel title = new JLabel("NewSale");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        topBar.add(title, BorderLayout.WEST);

        // Search bar (center)
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setOpaque(false);
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(320, 32));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(0, 16, 0, 8)));
        searchField.setText("");
        JLabel searchIcon = new JLabel(new ImageIcon("src/main/com/pos/resources/images/icons/search.png"));
        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        topBar.add(searchPanel, BorderLayout.CENTER);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
            private void updateSearch() {
                searchText = searchField.getText().trim().toLowerCase();
                updateProductGrid();
            }
        });

        // Notification and user info (right)
        JPanel rightTopPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 0));
        rightTopPanel.setOpaque(false);
        JLabel notificationIcon = new JLabel(new ImageIcon("src/main/com/pos/resources/images/icons/bell.png"));
        notificationIcon.setToolTipText("Notifications");
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

        // User info (right)
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        userPanel.setOpaque(false);
        JLabel userName = new JLabel("Khorn Victor");
        userName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        JLabel userRole = new JLabel("admin");
        userRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userRole.setForeground(new Color(120, 120, 120));
        JPanel userTextPanel = new JPanel();
        userTextPanel.setLayout(new BoxLayout(userTextPanel, BoxLayout.Y_AXIS));
        userTextPanel.setOpaque(false);
        userTextPanel.add(userName);
        userTextPanel.add(userRole);
        JLabel avatar = new JLabel();
        avatar.setOpaque(true);
        avatar.setBackground(new Color(33, 150, 243));
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

        // Category bar (dynamic)
        JPanel categoryBar = new JPanel();
        categoryBar.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));
        categoryBar.setOpaque(false);
        categoryComboBox = new JComboBox<>();
        categoryComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryComboBox.setPreferredSize(new Dimension(180, 32));
        categoryBar.add(categoryComboBox);
        centerPanel.add(categoryBar, BorderLayout.NORTH);

        // Product grid
        productGrid = new JPanel(new GridLayout(0, 4, 24, 16));
        productGrid.setOpaque(false);
        JScrollPane productScroll = new JScrollPane(productGrid);
        productScroll.setBorder(null);
        productScroll.getViewport().setBackground(new Color(247, 249, 251));
        centerPanel.add(productScroll, BorderLayout.CENTER);

        // Load categories and products from DB
        loadCategoriesAndProducts();

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

        // ...existing code...
        // Payment and QR code at the bottom
        JPanel checkoutPanel = new JPanel();
        checkoutPanel.setLayout(new BoxLayout(checkoutPanel, BoxLayout.Y_AXIS));
        checkoutPanel.setOpaque(false);
        checkoutPanel.setBorder(new EmptyBorder(16, 0, 0, 0));

        // Subtotal and total
        JPanel subtotalPanel = new JPanel(new BorderLayout());
        subtotalPanel.setOpaque(false);
        subtotalPanel.add(new JLabel("Subtotal:"), BorderLayout.WEST);
        subtotalValue = new JLabel("$0.00");
        subtotalValue.setFont(new Font("Segoe UI", Font.BOLD, 15));
        subtotalPanel.add(subtotalValue, BorderLayout.EAST);
        checkoutPanel.add(subtotalPanel);
        checkoutPanel.add(Box.createVerticalStrut(8));
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setOpaque(false);
        totalPanel.add(new JLabel("Total:"), BorderLayout.WEST);
        totalValue = new JLabel("$0.00");
        totalValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalValue.setForeground(new Color(33, 150, 243));
        totalPanel.add(totalValue, BorderLayout.EAST);
        checkoutPanel.add(totalPanel);
        checkoutPanel.add(Box.createVerticalStrut(16));

        // Payment buttons
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        paymentPanel.setOpaque(false);
        JButton cashBtn = new JButton("Cash");
        cashBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cashBtn.setBackground(new Color(34, 197, 94));
        cashBtn.setForeground(Color.WHITE);
        cashBtn.setFocusPainted(false);
        cashBtn.addActionListener(e -> {
            showReceiptPanel();
        });
        JButton cardBtn = new JButton("Card");
        cardBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cardBtn.setBackground(new Color(59, 130, 246));
        cardBtn.setForeground(Color.WHITE);
        cardBtn.setFocusPainted(false);
        cardBtn.addActionListener(e -> {
            showReceiptPanel();
        });
        paymentPanel.add(cashBtn);
        paymentPanel.add(cardBtn);
        checkoutPanel.add(paymentPanel);
        checkoutPanel.add(Box.createVerticalStrut(8));

        // QR code
        JPanel qrPanel = new JPanel(new BorderLayout());
        qrPanel.setOpaque(false);
        JLabel qrLabel = new JLabel(new ImageIcon("src/main/com/pos/resources/images/qr_checkout.png"));
        qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
        qrPanel.add(qrLabel, BorderLayout.CENTER);
        JButton qrDoneBtn = new JButton("Done");
        qrDoneBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        qrDoneBtn.setBackground(new Color(33, 150, 243));
        qrDoneBtn.setForeground(Color.WHITE);
        qrDoneBtn.setFocusPainted(false);
        qrDoneBtn.addActionListener(e -> {
            showReceiptPanel();
        });
        qrPanel.add(qrDoneBtn, BorderLayout.SOUTH);
        checkoutPanel.add(qrPanel);

        rightPanel.add(checkoutPanel, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.EAST);
        // Initial cart update
        updateCartPanel();
    }

    // Show receipt panel (for both cash and QR)
    private void showReceiptPanel() {
        JPanel receiptPanel = new JPanel();
        receiptPanel.setLayout(new BoxLayout(receiptPanel, BoxLayout.Y_AXIS));
        receiptPanel.setBackground(Color.WHITE);
        receiptPanel.setBorder(new EmptyBorder(24, 24, 24, 24));
        receiptPanel.add(new JLabel("Receipt (Preview):"));
        // Add receipt details here (for now, just a placeholder)
        receiptPanel.add(new JLabel("Thank you for your purchase!"));
        JOptionPane.showMessageDialog(this, receiptPanel, "Receipt", JOptionPane.PLAIN_MESSAGE);
    }


    // Add product to cart or increase quantity
    private void addToCart(Product product) {
        CartItem item = cart.get(product.getProductId());
        if (item == null) {
            item = new CartItem(product, 1);
            cart.put(product.getProductId(), item);
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
        for (Product prod : products) {
            String prodName = prod.getName().toLowerCase();
            String catName = getCategoryNameById(prod.getCategoryId());
            if ((selectedCategory.equals("All") || catName.equals(selectedCategory)) &&
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

                // Product image (top)
                JPanel imagePanel = new JPanel(new BorderLayout());
                imagePanel.setOpaque(false);
                String imagePath = prod.getImage();
                JLabel imageLabel;
                ImageIcon icon = null;
                Image img = null;
                if (imagePath != null && !imagePath.isEmpty()) {
                    img = main.com.pos.components.ui.UI.getImage(imagePath);
                }
                if (img != null) {
                    icon = new ImageIcon(img.getScaledInstance(60, 60, Image.SCALE_SMOOTH));
                } else {
                    Image defaultImg = main.com.pos.components.ui.UI.getImage("images/default/doesnot_exist.png");
                    if (defaultImg != null) {
                        icon = new ImageIcon(defaultImg.getScaledInstance(60, 60, Image.SCALE_SMOOTH));
                    } else {
                        icon = new ImageIcon();
                    }
                }
                imageLabel = new JLabel(icon);
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imagePanel.add(imageLabel, BorderLayout.CENTER);
                card.add(imagePanel, BorderLayout.CENTER);

                // Name and price (bottom)
                JPanel infoPanel = new JPanel(new GridLayout(2, 1));
                infoPanel.setOpaque(false);
                JLabel name = new JLabel(prod.getName());
                name.setFont(new Font("Segoe UI", Font.BOLD, 16));
                name.setHorizontalAlignment(SwingConstants.LEFT);
                JLabel price = new JLabel(String.format("$%.2f", prod.getPrice()));
                price.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                price.setForeground(new Color(46, 204, 113));
                price.setHorizontalAlignment(SwingConstants.LEFT);
                infoPanel.add(name);
                infoPanel.add(price);
                card.add(infoPanel, BorderLayout.SOUTH);

                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                card.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        addToCart(prod);
                    }
                });
                cards.add(card);
            }
        }
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

    // Load categories and products from DB
    private void loadCategoriesAndProducts() {
        CategoryDAO categoryDAO = new CategoryDAO();
        ProductDAO productDAO = new ProductDAO();
        categories.clear();
        products.clear();
        categories.add(new Category(0, "All"));
        categories.addAll(categoryDAO.getAll());
        products.addAll(productDAO.getAll());
        categoryComboBox.removeAllItems();
        for (Category cat : categories) {
            categoryComboBox.addItem(cat.getName());
        }
        categoryComboBox.setSelectedIndex(0);
        selectedCategory = "All";
        categoryComboBox.addActionListener(e -> {
            selectedCategory = (String) categoryComboBox.getSelectedItem();
            updateProductGrid();
        });
        updateProductGrid();
    }

    // Helper to get category name by id
    private String getCategoryNameById(int id) {
        for (Category cat : categories) {
            if (cat.getCategoryId() == id) return cat.getName();
        }
        return "";
    }

    // Update cart UI
    private void updateCartPanel() {
        cartPanel.removeAll();
        double subtotal = 0.0;
        for (CartItem item : cart.values()) {
            JPanel cartItem = new JPanel();
            cartItem.setLayout(new BoxLayout(cartItem, BoxLayout.X_AXIS));
            cartItem.setOpaque(false);

            // Product image
            String imagePath = item.product.getImage();
            ImageIcon icon = null;
            Image img = null;
            if (imagePath != null && !imagePath.isEmpty()) {
                img = main.com.pos.components.ui.UI.getImage(imagePath);
            }
            if (img != null) {
                icon = new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            } else {
                Image defaultImg = main.com.pos.components.ui.UI.getImage("images/default/doesnot_exist.png");
                if (defaultImg != null) {
                    icon = new ImageIcon(defaultImg.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                } else {
                    icon = new ImageIcon();
                }
            }
            JLabel cartImgLabel = new JLabel(icon);
            cartImgLabel.setPreferredSize(new Dimension(48, 48));
            cartImgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cartItem.add(cartImgLabel);

            cartItem.add(Box.createHorizontalStrut(10));

            // Name and price (vertical)
            JPanel namePricePanel = new JPanel();
            namePricePanel.setLayout(new BoxLayout(namePricePanel, BoxLayout.Y_AXIS));
            namePricePanel.setOpaque(false);
            namePricePanel.setAlignmentY(Component.CENTER_ALIGNMENT);

            JLabel cartName = new JLabel(item.product.getName());
            cartName.setFont(new Font("Segoe UI", Font.BOLD, 15));
            cartName.setAlignmentX(Component.LEFT_ALIGNMENT);
            namePricePanel.add(cartName);

            JLabel cartPrice = new JLabel(String.format("$%.2f", item.product.getPrice()));
            cartPrice.setFont(new Font("Segoe UI", Font.BOLD, 15));
            cartPrice.setForeground(new Color(34, 197, 94));
            cartPrice.setAlignmentX(Component.LEFT_ALIGNMENT);
            namePricePanel.add(cartPrice);

            cartItem.add(namePricePanel);

            cartItem.add(Box.createHorizontalGlue());

            // Quantity controls (rightmost, same size)
            JPanel qtyPanel = new JPanel();
            qtyPanel.setLayout(new BoxLayout(qtyPanel, BoxLayout.X_AXIS));
            qtyPanel.setOpaque(false);
            qtyPanel.setAlignmentY(Component.CENTER_ALIGNMENT);


            // Custom circular button class
            class CircleButton extends JButton {
                private final Color fillColor;
                private final Color borderColor;
                public CircleButton(String text, Color fillColor, Color borderColor) {
                    super(text);
                    this.fillColor = fillColor;
                    this.borderColor = borderColor;
                    setContentAreaFilled(false);
                    setFocusPainted(false);
                    setBorderPainted(false);
                    setOpaque(false);
                    setForeground(Color.WHITE);
                    setFont(new Font("Segoe UI", Font.BOLD, 18));
                    setPreferredSize(new Dimension(36, 36));
                    setMaximumSize(new Dimension(36, 36));
                    setMinimumSize(new Dimension(36, 36));
                }
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(fillColor);
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.setColor(borderColor);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                    // Draw the text (+ or -) centered
                    FontMetrics fm = g2.getFontMetrics(getFont());
                    String text = getText();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getAscent();
                    int x = (getWidth() - textWidth) / 2;
                    int y = (getHeight() + textHeight) / 2 - 2;
                    g2.setColor(getForeground());
                    g2.setFont(getFont());
                    g2.drawString(text, x, y);
                    g2.dispose();
                }
            }

            JButton plusBtn = new CircleButton("+", new Color(46, 204, 113), new Color(39, 174, 96));
            JButton minusBtn = new CircleButton("-", new Color(231, 76, 60), new Color(200, 50, 50));

            qtyPanel.add(plusBtn);

            JLabel qtyLabel = new JLabel(String.format("%02d", item.qty));
            qtyLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            qtyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            qtyLabel.setPreferredSize(new Dimension(28, 36));
            qtyLabel.setMaximumSize(new Dimension(28, 36));
            qtyLabel.setMinimumSize(new Dimension(28, 36));
            qtyPanel.add(qtyLabel);

            qtyPanel.add(minusBtn);

            cartItem.add(qtyPanel);

            // Button actions
            minusBtn.addActionListener(e -> {
                if (item.qty > 1) {
                    item.qty--;
                } else {
                    cart.remove(item.product.getProductId());
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
            subtotal += item.product.getPrice() * item.qty;
        }
        cartPanel.revalidate();
        cartPanel.repaint();
        subtotalValue.setText(String.format("$%.2f", subtotal));
        totalValue.setText(String.format("$%.2f", subtotal));
    }

    // Cart item class
        private static class CartItem {
        Product product;
        int qty;
        CartItem(Product product, int qty) {
            this.product = product;
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