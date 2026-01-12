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
import main.com.pos.controller.CategoryController;
import main.com.pos.dao.ProductDAO;
import main.com.pos.model.Category;
import main.com.pos.model.Product;

public class ProductPanel extends JPanel {
    
    private final ProductDAO productDAO;
    private final CategoryController categoryController;
    private final List<Product> allProducts;
    private List<Product> currentFilteredProducts;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private final ProductDetail productDetail;
    private final AddProductPanel addProductPanel;
    private final JPanel centerPanel;

    public ProductPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 247, 250));
        
        // Initialize DAO and Controller
        productDAO = new ProductDAO();
        categoryController = new CategoryController();
        allProducts = productDAO.getAll();
        currentFilteredProducts = new ArrayList<>(allProducts);

        add(createTopBar(), BorderLayout.NORTH);
        
        // Create main content panel with table and sidebar
        centerPanel = new JPanel(new BorderLayout(10, 0));
        centerPanel.setOpaque(false);
        centerPanel.add(createTable(), BorderLayout.CENTER);
        productDetail = new ProductDetail();
        centerPanel.add(productDetail, BorderLayout.EAST);
        addProductPanel = new AddProductPanel();
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

        // Load categories dynamically from database
        JComboBox<String> categoryBox = createStyledComboBox(buildCategoryOptions());
        categoryBox.addActionListener(e -> filterByCategory(categoryBox.getSelectedItem().toString()));
        left.add(categoryBox);

        JButton deleteButton = createAnimatedButton("Delete", new Color(255, 90, 90), new Color(220, 50, 50), new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleDeleteProduct();
            }
        });
        JButton updateButton = createAnimatedButton("Update", new Color(100, 149, 237), new Color(70, 119, 207), new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleUpdateProduct();
            }
        });
        JButton addButton = createAnimatedButton("Add Product", new Color(34, 139, 34), new Color(24, 109, 24), new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleAddProduct();
            }
        });

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
    private JButton createAnimatedButton(String text, Color normalColor, Color hoverColor, MouseAdapter clickHandler) {
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
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                
                timer = new Timer(10, event -> {
                    opacity += 0.05f;
                    if (opacity >= 1.0f) {
                        opacity = 1.0f;
                        timer.stop();
                        button.setBackground(normalColor);
                    } else {
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
                Color pressedColor = new Color(
                    Math.max(0, hoverColor.getRed() - 30),
                    Math.max(0, hoverColor.getGreen() - 30),
                    Math.max(0, hoverColor.getBlue() - 30)
                );
                button.setBackground(pressedColor);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.contains(e.getPoint())) {
                    button.setBackground(hoverColor);
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                // Delegate the click event to the provided handler
                if (clickHandler != null) {
                    clickHandler.mouseClicked(e);
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
                    DetailView();
                    productDetail.displayProduct(selectedProduct);
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

    /* ---------------- BUILD CATEGORY OPTIONS FOR COMBOBOX ---------------- */
    private String[] buildCategoryOptions() {
        List<Category> categories = categoryController.getAllCategories();
        String[] options = new String[categories.size() + 1];
        options[0] = "All Categories";
        
        for (int i = 0; i < categories.size(); i++) {
            options[i + 1] = categories.get(i).getName();
        }
        
        return options;
    }

    /* ---------------- GET CATEGORY MAP (Category ID to Name) ---------------- */
    private Map<Integer, String> getCategoryMap() {
        Map<Integer, String> categoryMap = new HashMap<>();
        List<Category> categories = categoryController.getAllCategories();
        
        for (Category category : categories) {
            categoryMap.put(category.getCategoryId(), category.getName());
        }
        
        return categoryMap;
    }

    /* ---------------- FILTER PRODUCTS BY CATEGORY ---------------- */
    private void filterByCategory(String categoryName) {
        if (categoryName.equals("All Categories")) {
            currentFilteredProducts = new ArrayList<>(allProducts);
        } else {
            currentFilteredProducts = new ArrayList<>();
            Map<Integer, String> categoryMap = getCategoryMap();
            
            for (Product product : allProducts) {
                String productCategory = categoryMap.get(product.getCategoryId());
                if (categoryName.equals(productCategory)) {
                    currentFilteredProducts.add(product);
                }
            }
        }
        
        refreshTable(currentFilteredProducts);
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
            refreshTable(currentFilteredProducts);
            return;
        }
        
        List<Product> filteredProducts = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Product product : currentFilteredProducts) {
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
        AddProductView();
    }

    private void handleUpdateProduct() {
        System.out.println("Update Product button clicked");
    }

    private void handleDeleteProduct() {
        System.out.println("Delete Product button clicked");
    }
    
    /* ---------------- SWITCH BETWEEN ADD PRODUCT AND DETAIL VIEW ---------------- */
    private void AddProductView() {
        centerPanel.remove(productDetail);
        centerPanel.add(addProductPanel, BorderLayout.EAST);
        centerPanel.revalidate();
        centerPanel.repaint();
    }
    
    private void DetailView() {
        centerPanel.remove(addProductPanel);
        centerPanel.add(productDetail, BorderLayout.EAST);
        centerPanel.revalidate();
        centerPanel.repaint();
    }
}