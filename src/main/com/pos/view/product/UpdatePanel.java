package main.com.pos.view.product;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import main.com.pos.controller.CategoryController;
import main.com.pos.controller.ProductController;
import main.com.pos.dao.ProductDAO;
import main.com.pos.model.Category;
import main.com.pos.model.Product;
import main.com.pos.util.DeleteImage;
import main.com.pos.util.DragDropImage;
import main.com.pos.util.Telegram;

public class UpdatePanel extends JPanel {
    
    @SuppressWarnings("unused")
    private JPanel contentPanel;
    private final CategoryController categoryController;
    private DragDropImage imagePanel;
    private JTextField nameField;
    private JComboBox<String> categoryCombo;
    private JSpinner priceSpinner;
    private JSpinner stockSpinner;
    private JTextArea descriptionArea;
    private JButton saveButton;
    private JButton cancelButton;
    private final JPanel formPanel;
    private Runnable onProductUpdatedCallback;
    private Product currentProduct;

    
    public UpdatePanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(350, Integer.MAX_VALUE));
        setBackground(new Color(250, 251, 253));
        
        categoryController = new CategoryController();
        formPanel = new JPanel();
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        add(createContentPanel(), BorderLayout.CENTER);
        
        add(createActionsPanel(), BorderLayout.SOUTH);
    }
    
    /* ==================== SET PRODUCT ==================== */
    public void setProduct(Product product) {
        this.currentProduct = product;
        if (product != null) {
            loadProductData();
        }
    }
    
    /* ==================== LOAD PRODUCT DATA ==================== */
    private void loadProductData() {
        if (currentProduct == null) return;
        
        // Set product name
        nameField.setText(currentProduct.getName());
        
        // Set category
        Category category = categoryController.getCategoryById(currentProduct.getCategoryId());
        if (category != null) {
            categoryCombo.setSelectedItem(category.getName());
        }
        
        // Set price
        priceSpinner.setValue(currentProduct.getPrice());
        
        // Set stock quantity
        stockSpinner.setValue(currentProduct.getStockQuantity());
        
        // Set image
        if (currentProduct.getImage() != null && !currentProduct.getImage().isEmpty()) {
            imagePanel.setImageFromPath(currentProduct.getImage());
        } else {
            imagePanel.resetImage();
        }
    }
    
    /* ==================== SET CALLBACK ==================== */
    public void setOnProductUpdatedCallback(Runnable callback) {
        this.onProductUpdatedCallback = callback;
    }
    
    /* ==================== HEADER PANEL ==================== */
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
        
        JLabel titleLabel = new JLabel("Update Product");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
            
        return headerPanel;
    }
    
    /* ==================== CONTENT PANEL ==================== */
    private JPanel createContentPanel() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(250, 251, 253));
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Image Panel (top)
        imagePanel = createImagePanel();
        imagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(imagePanel);
        
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Form fields (below)
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(createFormPanel());
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        return mainContent;
    }
    
    /* ==================== IMAGE PANEL ==================== */
    private DragDropImage createImagePanel() {
        DragDropImage panel = new DragDropImage(290, 180, "product_default.png");
        panel.setOpaque(false);
        // Size aligned with requested drag-drop dimensions
        panel.setPreferredSize(new Dimension(290, 180));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        return panel;
    }
    
    /* ==================== FORM PANEL ==================== */
    private JPanel createFormPanel() {

        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        
        // Only one input row (text field)
        formPanel.add(createFormField("Product Name", "nameField"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(createCategoryFieldCompact());
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(createPriceFieldCompact());
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(createStockField());
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(createDescriptionField());
        
        
        
        formPanel.add(Box.createVerticalGlue());

        JPanel panel = formPanel;
        return panel;
    }
    
    /* ==================== CREATE FORM FIELD ==================== */
    private JPanel createFormField(String label, String fieldType) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setOpaque(false);    
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        
        // Label
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fieldLabel.setForeground(new Color(31, 41, 55));
        fieldLabel.setHorizontalAlignment(SwingConstants.LEFT);
        fieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.add(fieldLabel);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        // Input field
        if ("nameField".equals(fieldType)) {
            nameField = createModernTextField();
            nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            fieldPanel.add(nameField);
        }
        
        return fieldPanel;
    }
    /* ==================== MODERN TEXT FIELD ==================== */
    private JTextField createModernTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(new Color(55, 65, 81));
        field.setCaretColor(new Color(59, 130, 246));
        
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!field.hasFocus()) {
                    field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(156, 163, 175), 1, true),
                        BorderFactory.createEmptyBorder(10, 14, 10, 14)
                    ));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!field.hasFocus()) {
                    field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                        BorderFactory.createEmptyBorder(10, 14, 10, 14)
                    ));
                }
            }
        });
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(59, 130, 246), 2, true),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)
                ));
            }
        });
        
        return field;
    }
    
    /* ==================== CREATE CATEGORY FIELD (COMPACT) ==================== */
    private JPanel createCategoryFieldCompact() {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setOpaque(false);
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel fieldLabel = new JLabel("Category");
        fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        fieldLabel.setForeground(new Color(31, 41, 55));
        fieldLabel.setHorizontalAlignment(SwingConstants.LEFT);
        fieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.add(fieldLabel);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        
        // Load categories dynamically from database
        String[] categories = buildCategoryOptions();
        categoryCombo = new JComboBox<>(categories);
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        categoryCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        categoryCombo.setBackground(Color.WHITE);
        categoryCombo.setForeground(new Color(55, 65, 81));
        categoryCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        categoryCombo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        categoryCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                
                if (isSelected) {
                    label.setBackground(new Color(59, 130, 246));
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(55, 65, 81));
                }
                
                return label;
            }
        });
        
        fieldPanel.add(categoryCombo);
        
        return fieldPanel;
    }
    
    /* ==================== CREATE PRICE FIELD (COMPACT) ==================== */
    private JPanel createPriceFieldCompact() {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setOpaque(false);
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel fieldLabel = new JLabel("Price ($)");
        fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        fieldLabel.setForeground(new Color(31, 41, 55));
        fieldLabel.setHorizontalAlignment(SwingConstants.LEFT);
        fieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.add(fieldLabel);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        
        SpinnerModel priceModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, 0.01);
        priceSpinner = new JSpinner(priceModel);
        
        JComponent priceEditor = priceSpinner.getEditor();
        if (priceEditor instanceof JSpinner.DefaultEditor editor) {
            editor.getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 13));
            editor.getTextField().setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            editor.getTextField().setForeground(new Color(55, 65, 81));
            editor.getTextField().setHorizontalAlignment(JTextField.RIGHT);
        }
        
        priceSpinner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        priceSpinner.setBackground(Color.WHITE);
        priceSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        fieldPanel.add(priceSpinner);
        
        return fieldPanel;
    }
    
    /* ==================== CREATE STOCK FIELD ==================== */
    private JPanel createStockField() {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setOpaque(false);
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        
        JLabel fieldLabel = new JLabel("Stock Quantity");
        fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fieldLabel.setForeground(new Color(31, 41, 55));
        fieldLabel.setHorizontalAlignment(SwingConstants.LEFT);
        fieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.add(fieldLabel);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        SpinnerModel stockModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        stockSpinner = new JSpinner(stockModel);
        
        JComponent stockEditor = stockSpinner.getEditor();
        if (stockEditor instanceof JSpinner.DefaultEditor editor) {
            editor.getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 14));
            editor.getTextField().setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            editor.getTextField().setForeground(new Color(55, 65, 81));
            editor.getTextField().setHorizontalAlignment(JTextField.RIGHT);
        }
        
        stockSpinner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        stockSpinner.setBackground(Color.WHITE);
        stockSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        
        fieldPanel.add(stockSpinner);
        
        return fieldPanel;
    }
    
    /* ==================== CREATE DESCRIPTION FIELD ==================== */
    private JPanel createDescriptionField() {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setOpaque(false);
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.setPreferredSize(new Dimension(290, 100));
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        JLabel fieldLabel = new JLabel("Description (Optional)");
        fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fieldLabel.setForeground(new Color(31, 41, 55));
        fieldLabel.setHorizontalAlignment(SwingConstants.LEFT);
        fieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.add(fieldLabel);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setForeground(new Color(55, 65, 81));
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        descriptionArea.setCaretColor(new Color(59, 130, 246));
        descriptionArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        fieldPanel.add(descriptionArea);
        
        return fieldPanel;
    }
    
    private JPanel createActionsPanel() {
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 20));
        actionsPanel.setBackground(new Color(250, 251, 253));
        actionsPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(215, 220, 228)));
        
        // Cancel Button
        cancelButton = createModernButton("Cancel", new Color(229, 231, 235), new Color(107, 114, 128));
        cancelButton.addActionListener(e -> clearForm());
        
        // Save Button
        saveButton = createModernButton("Update Product", new Color(34, 139, 34), Color.WHITE);
        saveButton.addActionListener(e -> updateProduct());
        
        actionsPanel.add(cancelButton);
        actionsPanel.add(saveButton);
        
        return actionsPanel;
    }
    
    /* ==================== CREATE MODERN BUTTON ==================== */
    private JButton createModernButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setBorder(BorderFactory.createEmptyBorder(11, 28, 11, 28));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            private final Color originalBg = bgColor;
            
            @Override
            public void mouseEntered(MouseEvent e) {
                int r = Math.min(255, originalBg.getRed() + 20);
                int g = Math.min(255, originalBg.getGreen() + 20);
                int b = Math.min(255, originalBg.getBlue() + 20);
                button.setBackground(new Color(r, g, b));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBg);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                int r = Math.max(0, originalBg.getRed() - 20);
                int g = Math.max(0, originalBg.getGreen() - 20);
                int b = Math.max(0, originalBg.getBlue() - 20);
                button.setBackground(new Color(r, g, b));
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(originalBg);
            }
        });
        
        return button;
    }
    
    /* ==================== BUILD CATEGORY OPTIONS ==================== */
    private String[] buildCategoryOptions() {
        java.util.List<Category> categories = categoryController.getAllCategories();
        String[] options = new String[categories.size()];
        
        for (int i = 0; i < categories.size(); i++) {
            options[i] = categories.get(i).getName();
        }
        
        return options;
    }
    
    /* ==================== UPDATE PRODUCT ==================== */
    private void updateProduct() {
        if (currentProduct == null) {
            showError("No product selected for update!");
            return;
        }
        
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showError("Product name is required!");
            return;
        }
        
        try {
            // Update the product object with new values
            String previous_name = currentProduct.getName();
            currentProduct.setName(name);
            currentProduct.setCategoryId(ProductDAO.getCategoryIdByName((String) categoryCombo.getSelectedItem()));
            currentProduct.setPrice((Double) priceSpinner.getValue());
            currentProduct.setStockQuantity((Integer) stockSpinner.getValue());
            
            // Handle image update if a new image was selected
            String imagePath = (String) imagePanel.getSelectedImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                imagePanel.saveImageToResources(name.toLowerCase().replaceAll("\\s+", "_") + ".png");
                if (!currentProduct.getName().equals(previous_name)) DeleteImage.deleteImage("/images/product/" + previous_name.toLowerCase().replaceAll("\\s+", "_") + ".png");
                currentProduct.setImage("/images/product/" + name.toLowerCase().replaceAll("\\s+", "_") + ".png");
            }
            
            ProductController productController = new ProductController();
            
            if (productController.updateProduct(currentProduct)) {
                showSuccess("Product updated successfully!");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\ud83d\udd04 Product Updated:\n\n");
                stringBuilder.append("Id: ");
                stringBuilder.append(currentProduct.getProductId());
                stringBuilder.append("\n");
                stringBuilder.append("Name: ");
                stringBuilder.append(currentProduct.getName());
                stringBuilder.append("\n");
                stringBuilder.append("Category ID: ");
                stringBuilder.append(currentProduct.getCategoryId());
                stringBuilder.append("\n");
                stringBuilder.append("Price: $");
                stringBuilder.append(currentProduct.getPrice());
                stringBuilder.append("\n");
                stringBuilder.append("Stock Quantity: ");
                stringBuilder.append(currentProduct.getStockQuantity());
                stringBuilder.append("\n");
                stringBuilder.append("Description: ");
                stringBuilder.append(descriptionArea.getText());
                Telegram.telegramSend(
                    stringBuilder.toString()
                );
                // Refresh the table if callback is set
                if (onProductUpdatedCallback != null) {
                    onProductUpdatedCallback.run();
                }
            } else  
                showError("Failed to update product. Please try again.");
        } catch (IllegalArgumentException e) {
            showError("Invalid input: " + e.getMessage());
        }
    }
    
    /* ==================== CLEAR FORM ==================== */
    private void clearForm() {
        nameField.setText("");
        categoryCombo.setSelectedIndex(0);
        priceSpinner.setValue(0.0);
        stockSpinner.setValue(0);
        descriptionArea.setText("");
        imagePanel.resetImage();
        imagePanel.repaint();
        nameField.requestFocus();
    }
    
    /* ==================== SHOW MESSAGES ==================== */
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
