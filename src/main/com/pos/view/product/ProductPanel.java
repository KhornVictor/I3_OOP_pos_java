package main.com.pos.view.product;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.*;
import main.com.pos.components.ui.TableDesign;
import main.com.pos.components.ui.UI;
import main.com.pos.dao.ProductDAO;
import main.com.pos.model.Product;

public class ProductPanel extends JPanel {
    
    private final ProductDAO productDAO;
    private final List<Product> allProducts;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JPanel detailSidebar;
    private JLabel productImageLabel;
    private JLabel productNameLabel;
    private JLabel productIdLabel;
    private JLabel categoryLabel;
    private JLabel priceLabel;
    private JLabel stockLabel;
    private JLabel statusLabel;
    private JTextArea descriptionArea;

    public ProductPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 247, 250));
        
        // Initialize ProductDAO
        productDAO = new ProductDAO();
        allProducts = productDAO.getAll();

        add(createTopBar(), BorderLayout.NORTH);
        
        // Create main content panel with table and sidebar
        JPanel centerPanel = new JPanel(new BorderLayout(10, 0));
        centerPanel.setOpaque(false);
        centerPanel.add(createTable(), BorderLayout.CENTER);
        centerPanel.add(createDetailSidebar(), BorderLayout.EAST);
        
        add(centerPanel, BorderLayout.CENTER);
    }

    /* ---------------- TOP BAR ---------------- */
    private JPanel createTopBar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 247, 250));
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setBackground(new Color(245, 247, 250));
        JPanel searchPanel = createSearchBar();
        left.add(searchPanel);
        JComboBox<String> statusBox = createStyledComboBox(
            new String[]{"All Status", "Published", "Draft", "Inactive", "Stock Out"}
        );
        left.add(statusBox);
        

        JComboBox<String> categoryBox = createStyledComboBox(
            new String[]{"All Categories", "Sunglass", "Clothes", "Beauty", "Cap", "Shoes", "Electronic", "Watch"}
        );
        left.add(categoryBox);

        JButton deleteButton = createAnimatedButton("Delete", new Color(255, 90, 90), new Color(220, 50, 50));
        JButton updateButton = createAnimatedButton("Update", new Color(100, 149, 237), new Color(70, 119, 207));
        JButton addButton = createAnimatedButton("Add Product", new Color(34, 139, 34), new Color(24, 109, 24));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setBackground(new Color(245, 247, 250));
        right.add(deleteButton);
        right.add(updateButton);    
        right.add(addButton);

        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);

        return panel;
    }


    private JPanel createSearchBar() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchPanel.setPreferredSize(new Dimension(280, 38));

        // Search Icon
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

        // Search TextField
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createEmptyBorder());
        searchField.setBackground(Color.WHITE);
        searchField.setForeground(new Color(60, 60, 60));
        searchField.setCaretColor(new Color(60, 60, 60));
        
        // Placeholder text 
        searchField.setText("Search products...");
        searchField.setForeground(Color.GRAY);
        
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search products...")) {
                    searchField.setText("");
                    searchField.setForeground(new Color(60, 60, 60));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search products...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });


        JButton clearButton = new JButton("✕");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 14));
        clearButton.setBorder(BorderFactory.createEmptyBorder());
        clearButton.setBackground(Color.WHITE);
        clearButton.setForeground(Color.GRAY);
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.setVisible(false);
        clearButton.addActionListener(e -> {
            searchField.setText("");
            searchField.requestFocus();
        });

        // Show/hide clear button based on text and filter products
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterProducts(searchField.getText()); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterProducts(searchField.getText()); }
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterProducts(searchField.getText()); }
        });

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(clearButton, BorderLayout.EAST);

        return searchPanel;
    }

    /* ---------------- STYLED COMBOBOX ---------------- */
    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(60, 60, 60));
        comboBox.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        comboBox.setPreferredSize(new Dimension(150, 38));
        comboBox.setFocusable(true);
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Custom renderer for dropdown items
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                label.setFont(new Font("Arial", Font.PLAIN, 13));
                
                if (isSelected) {
                    label.setBackground(new Color(100, 149, 237));
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(60, 60, 60));
                }
                
                return label;
            }
        });

        return comboBox;
    }

    /* ---------------- ANIMATED BUTTON ---------------- */
    private JButton createAnimatedButton(String text, Color normalColor, Color hoverColor) {
        JButton button = UI.setButton(text, new Font("Arial", Font.BOLD, 12), 
            normalColor, Color.WHITE, 
            BorderFactory.createEmptyBorder(10, 20, 10, 20), 10, null);
        
        button.addMouseListener(new MouseAdapter() {
            private Timer timer;
            private float opacity = 1.0f;
            
            @Override
            public void mouseEntered(MouseEvent e) {
                // Stop any existing timer
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                
                // Smooth transition to hover color
                timer = new Timer(10, event -> {
                    opacity -= 0.05f;
                    if (opacity <= 0) {
                        opacity = 0;
                        timer.stop();
                        button.setBackground(hoverColor);
                    } else {
                        // Interpolate between colors
                        int r = (int)(normalColor.getRed() * opacity + hoverColor.getRed() * (1 - opacity));
                        int g = (int)(normalColor.getGreen() * opacity + hoverColor.getGreen() * (1 - opacity));
                        int b = (int)(normalColor.getBlue() * opacity + hoverColor.getBlue() * (1 - opacity));
                        button.setBackground(new Color(r, g, b));
                    }
                });
                timer.start();
                
                // Scale up effect
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                // Stop any existing timer
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                
                // Smooth transition back to normal color
                timer = new Timer(10, event -> {
                    opacity += 0.05f;
                    if (opacity >= 1.0f) {
                        opacity = 1.0f;
                        timer.stop();
                        button.setBackground(normalColor);
                    } else {
                        // Interpolate between colors
                        int r = (int)(hoverColor.getRed() * (1 - opacity) + normalColor.getRed() * opacity);
                        int g = (int)(hoverColor.getGreen() * (1 - opacity) + normalColor.getGreen() * opacity);
                        int b = (int)(hoverColor.getBlue() * (1 - opacity) + normalColor.getBlue() * opacity);
                        button.setBackground(new Color(r, g, b));
                    }
                });
                timer.start();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                // Darken color on press
                Color pressedColor = new Color(
                    Math.max(0, hoverColor.getRed() - 30),
                    Math.max(0, hoverColor.getGreen() - 30),
                    Math.max(0, hoverColor.getBlue() - 30)
                );
                button.setBackground(pressedColor);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                // Return to hover color on release
                if (button.contains(e.getPoint())) {
                    button.setBackground(hoverColor);
                }
            }
        });
        
        return button;
    }

    /* ---------------- TABLE ---------------- */
    private JScrollPane createTable() {
        String[] columns = {"Product ID", "Product Name", "Category", "Stock", "Status", "Price"};
        Object[][] data = convertProductsToTableData(allProducts);

        tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };

        productTable = new JTable(tableModel);
        
        // Apply modern styling
        TableDesign.styleTable(productTable);
        productTable.setRowHeight(35);
        
        // Apply specific renderers for columns
        productTable.getColumn("Price").setCellRenderer(new TableDesign.CurrencyRenderer());
        productTable.getColumn("Stock").setCellRenderer(new TableDesign.CenteredRenderer());
        productTable.getColumn("Status").setCellRenderer(new StockStatusRenderer());
        
        // Add mouse listener to show product details when row is clicked
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Product selectedProduct = allProducts.get(selectedRow);
                    displayProductDetails(selectedProduct);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    /* ---------------- CONVERT PRODUCTS TO TABLE DATA ---------------- */
    private Object[][] convertProductsToTableData(List<Product> products) {
        // Create a mapping for category IDs to category names
        Map<Integer, String> categoryMap = getCategoryMap();
        
        Object[][] data = new Object[products.size()][6];
        
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            String categoryName = categoryMap.getOrDefault(product.getCategoryId(), "Unknown");
            String stockStatus = getStockStatus(product.getStockQuantity());
            data[i][0] = product.getProductId();
            data[i][1] = product.getName();
            data[i][2] = categoryName;
            data[i][3] = product.getStockQuantity();
            data[i][4] = stockStatus;  // Derived attribute: Stock Status
            data[i][5] = "$" + String.format("%.2f", product.getPrice());
        }
        
        return data;
    }

    /* ---------------- GET CATEGORY MAP (Category ID to Name) ---------------- */
    private Map<Integer, String> getCategoryMap() {
        Map<Integer, String> categoryMap = new HashMap<>();
        // Mapping category IDs to names (adjust based on your database)
        categoryMap.put(1, "Sunglass");
        categoryMap.put(2, "Clothes");
        categoryMap.put(3, "Beauty");
        categoryMap.put(4, "Cap");
        categoryMap.put(5, "Shoes");
        categoryMap.put(6, "Electronic");
        categoryMap.put(7, "Watch");
        return categoryMap;
    }

    /* ---------------- GET STOCK STATUS ---------------- */
    private String getStockStatus(int quantity) {
        if (quantity == 0) return "Out of Stock";
        else if (quantity < 50) return "Low Stock";
        else if (quantity >= 50 && quantity < 100) return "Medium Stock";
        else return "In Stock";
    }

    /* ---------------- STOCK STATUS RENDERER (Color-Coded Badges) ---------------- */
    private class StockStatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            
            String status = (String) value;
            label.setHorizontalAlignment(CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
              // Set colors based on status (Derived attribute color coding)
            switch (status) {
                case "Out of Stock" -> {
                    label.setBackground(new Color(255, 102, 102)); 
                    label.setForeground(Color.WHITE);
                }
                case "Low Stock" -> {
                    label.setBackground(new Color(255, 193, 7));  
                    label.setForeground(Color.WHITE);
                }
                case "Medium Stock" -> {
                    label.setBackground(new Color(255, 152, 0));    
                    label.setForeground(Color.WHITE);
                }
                case "In Stock" -> {
                    label.setBackground(new Color(76, 175, 80));    
                    label.setForeground(Color.WHITE);
                }
                default -> {
                    label.setBackground(table.getBackground());
                    label.setForeground(table.getForeground());
                }
            }
            
            label.setOpaque(true);
            return label;
        }
    }

    /* ---------------- FILTER PRODUCTS BY SEARCH QUERY ---------------- */
    private void filterProducts(String query) {
        if (query == null || query.isEmpty() || query.equals("Search products...")) {
            refreshTable(allProducts);
            return;
        }
        
        List<Product> filteredProducts = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(lowerQuery) ||
                String.valueOf(product.getProductId()).contains(lowerQuery)) {
                filteredProducts.add(product);
            }
        }
        
        refreshTable(filteredProducts);
    }

    /* ---------------- REFRESH TABLE WITH NEW DATA ---------------- */
    private void refreshTable(List<Product> products) {
        Object[][] data = convertProductsToTableData(products);
        tableModel.setRowCount(0);  // Clear existing rows
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    private void handleAddProduct() {
        System.out.println("Add Product button clicked");
    }

    private void handleUpdateProduct() {
        System.out.println("Update Product button clicked");
    }

    private void handleDeleteProduct() {
        System.out.println("Delete Product button clicked");
    }
    
    /* ---------------- PRODUCT DETAIL SIDEBAR ---------------- */
    private JPanel createDetailSidebar() {
        detailSidebar = new JPanel(new BorderLayout());
        detailSidebar.setBackground(new Color(249, 250, 251));
        detailSidebar.setPreferredSize(new Dimension(350, 0));
        detailSidebar.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(229, 231, 235)));
        
        // Header Panel with gradient
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
        
        // Content Panel with ScrollPane
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
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        detailSidebar.add(headerPanel, BorderLayout.NORTH);
        detailSidebar.add(scrollPane, BorderLayout.CENTER);
        
        return detailSidebar;
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
    
    /* ---------------- DISPLAY PRODUCT DETAILS IN SIDEBAR ---------------- */
    private void displayProductDetails(Product product) {
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
        
        detailSidebar.revalidate();
        detailSidebar.repaint();
    }
}