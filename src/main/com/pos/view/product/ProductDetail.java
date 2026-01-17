package main.com.pos.view.product;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import main.com.pos.components.ui.UI;
import main.com.pos.controller.CategoryController;
import main.com.pos.model.Category;
import main.com.pos.model.Product;

public class ProductDetail extends JPanel {
    
    private final CategoryController categoryController;
    private JLabel productImageLabel;
    private JLabel productNameLabel;
    private JLabel productIdLabel;
    private JLabel categoryLabel;
    private JLabel priceLabel;
    private JLabel stockLabel;
    private JLabel statusLabel;
    
    public ProductDetail() {
        setLayout(new BorderLayout());
        setBackground(new Color(249, 250, 251));
        setPreferredSize(new Dimension(350, 0));
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(229, 231, 235)));
        
        categoryController = new CategoryController();
        
        // Header Panel with gradient
        JPanel headerPanel = createHeaderPanel();
        
        // Content Panel with ScrollPane
        JPanel contentPanel = createContentPanel();
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /* ---------------- CREATE HEADER PANEL ---------------- */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(59, 130, 246), 0, getHeight(), new Color(37, 99, 235));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        headerPanel.setPreferredSize(new Dimension(350, 60));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Product Details");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
            
        return headerPanel;
    }
    
    /* ---------------- CREATE CONTENT PANEL ---------------- */
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Product Image with modern rounded corners
        productImageLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        productImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        productImageLabel.setPreferredSize(new Dimension(310, 200));
        productImageLabel.setMaximumSize(new Dimension(310, 200));
        productImageLabel.setBackground(Color.WHITE);
        productImageLabel.setOpaque(false);
        productImageLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        contentPanel.add(productImageLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Info Card Panel
        JPanel infoCard = createModernInfoCard();
        contentPanel.add(infoCard);
        
        return contentPanel;
    }
    
    /* ---------------- CREATE MODERN INFO CARD ---------------- */
    private JPanel createModernInfoCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.setColor(new Color(229, 231, 235));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setMaximumSize(new Dimension(310, 600));
        
        // Product Name (Prominent)
        productNameLabel = new JLabel("Select a product");
        productNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        productNameLabel.setForeground(new Color(17, 24, 39));
        productNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(productNameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Product ID (subtle)
        productIdLabel = new JLabel("ID: -");
        productIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        productIdLabel.setForeground(new Color(107, 114, 128));
        productIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(productIdLabel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Divider
        JSeparator separator1 = new JSeparator();
        separator1.setForeground(new Color(229, 231, 235));
        separator1.setMaximumSize(new Dimension(270, 1));
        separator1.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(separator1);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Category
        card.add(createInfoRow("📦", "Category"));
        categoryLabel = new JLabel("Not specified");
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryLabel.setForeground(new Color(55, 65, 81));
        categoryLabel.setBorder(BorderFactory.createEmptyBorder(3, 30, 0, 0));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(categoryLabel);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Price
        card.add(createInfoRow("💰", "Price"));
        priceLabel = new JLabel("$0.00");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        priceLabel.setForeground(new Color(34, 197, 94));
        priceLabel.setBorder(BorderFactory.createEmptyBorder(3, 30, 0, 0));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(priceLabel);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Stock
        card.add(createInfoRow("📊", "Stock Level"));
        stockLabel = new JLabel("0 units");
        stockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        stockLabel.setForeground(new Color(55, 65, 81));
        stockLabel.setBorder(BorderFactory.createEmptyBorder(3, 30, 0, 0));
        stockLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(stockLabel);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Status Badge
        card.add(createInfoRow("🔵", "Status"));
        statusLabel = new JLabel("Unknown") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(false);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel statusWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 3));
        statusWrapper.setOpaque(false);
        statusWrapper.add(statusLabel);
        statusWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(statusWrapper);
        
        return card;
    }
    
    /* ---------------- CREATE INFO ROW WITH ICON ---------------- */
    private JPanel createInfoRow(String emoji, String label) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel iconLabel = new JLabel(emoji);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        
        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        textLabel.setForeground(new Color(75, 85, 99));
        
        row.add(iconLabel);
        row.add(textLabel);
        
        return row;
    }
    
    /* ---------------- DISPLAY PRODUCT DETAILS ---------------- */
    public void displayProduct(Product product) {
        if (product == null) return;
        
        // Load and display product image
        String imagePath = product.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image img = UI.getImage(imagePath);
                if (img != null) {
                    ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(290, 180, Image.SCALE_SMOOTH));
                    productImageLabel.setIcon(imageIcon);
                    productImageLabel.setText("");
                } else {
                    productImageLabel.setIcon(null);
                    productImageLabel.setText("📷 No image available");
                    productImageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    productImageLabel.setForeground(new Color(156, 163, 175));
                }
            } catch (Exception e) {
                productImageLabel.setIcon(null);
                productImageLabel.setText("📷 Image not found");
                productImageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                productImageLabel.setForeground(new Color(156, 163, 175));
            }
        } else {
            productImageLabel.setIcon(null);
            productImageLabel.setText("📷 No image");
            productImageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            productImageLabel.setForeground(new Color(156, 163, 175));
        }
        
        // Update product information
        productNameLabel.setText(product.getName());
        productIdLabel.setText("ID: #" + product.getProductId());
        
        Map<Integer, String> categoryMap = getCategoryMap();
        categoryLabel.setText(categoryMap.getOrDefault(product.getCategoryId(), "Unknown"));
        
        priceLabel.setText(String.format("$%.2f", product.getPrice()));
        stockLabel.setText(product.getStockQuantity() + " units available");
        
        // Update status with modern badge styling
        String status;
        Color statusBg;
        if (product.getStockQuantity() == 0) {
            status = "OUT OF STOCK";
            statusBg = new Color(239, 68, 68);
        } else if (product.getStockQuantity() < 10) {
            status = "LOW STOCK";
            statusBg = new Color(245, 158, 11);
        } else {
            status = "IN STOCK";
            statusBg = new Color(34, 197, 94);
        }
        statusLabel.setText(status);
        statusLabel.setBackground(statusBg);
        
        revalidate();
        repaint();
    }
    
    /* ---------------- GET CATEGORY MAP FROM DATABASE ---------------- */
    private Map<Integer, String> getCategoryMap() {
        Map<Integer, String> categoryMap = new HashMap<>();
        List<Category> categories = categoryController.getAllCategories();
        
        for (Category category : categories) {
            categoryMap.put(category.getCategoryId(), category.getName());
        }
        
        return categoryMap;
    }
    
}
