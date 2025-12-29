package main.com.pos.view.product;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import main.com.pos.controller.ProductController;
import main.com.pos.model.Product;

public class ProductPanel extends JPanel {
    private final ProductController productController;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField stockField;
    private JTextField categoryField;

    public ProductPanel() {
        this.productController = new ProductController();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel addPanel = createAddProductPanel();
        add(addPanel, BorderLayout.NORTH);

        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);

        loadProducts();
    }

    private JPanel createAddProductPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Add/Update Product"));
        panel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        panel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        nameField = new JTextField();
        panel.add(nameField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.1;
        panel.add(new JLabel("Category ID:"), gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.2;
        categoryField = new JTextField();
        panel.add(categoryField, gbc);

        gbc.gridx = 4;
        gbc.weightx = 0.1;
        panel.add(new JLabel("Price:"), gbc);

        gbc.gridx = 5;
        gbc.weightx = 0.2;
        priceField = new JTextField();
        panel.add(priceField, gbc);

        gbc.gridx = 6;
        gbc.weightx = 0.1;
        panel.add(new JLabel("Stock:"), gbc);

        gbc.gridx = 7;
        gbc.weightx = 0.2;
        stockField = new JTextField();
        panel.add(stockField, gbc);

        gbc.gridx = 8;
        gbc.weightx = 0.1;
        JButton addBtn = new JButton("Add");
        addBtn.setBackground(new Color(34, 139, 34));
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> addProduct());
        panel.add(addBtn, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Products"));

        String[] columns = {"ID", "Name", "Category", "Price", "Stock", "Inventory Value"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        productTable = new JTable(tableModel);
        productTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(230, 230, 230));

        JButton updateBtn = new JButton("Update Selected");
        updateBtn.addActionListener(e -> updateProduct());
        panel.add(updateBtn);

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setBackground(new Color(220, 20, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deleteProduct());
        panel.add(deleteBtn);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadProducts());
        panel.add(refreshBtn);

        JButton statsBtn = new JButton("Inventory Stats");
        statsBtn.addActionListener(e -> showStats());
        panel.add(statsBtn);

        return panel;
    }

    private void addProduct() {
        try {
            if (nameField.getText().isEmpty() || priceField.getText().isEmpty() ||
                stockField.getText().isEmpty() || categoryField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product product = new Product(
                0,
                nameField.getText(),
                Integer.parseInt(categoryField.getText()),
                Double.parseDouble(priceField.getText()),
                Integer.parseInt(stockField.getText())
            );

            if (productController.addProduct(product)) {
                JOptionPane.showMessageDialog(this, "Product added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add product", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product to update", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            Product product = new Product(
                id,
                nameField.getText(),
                Integer.parseInt(categoryField.getText()),
                Double.parseDouble(priceField.getText()),
                Integer.parseInt(stockField.getText())
            );

            if (productController.updateProduct(product)) {
                JOptionPane.showMessageDialog(this, "Product updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update product", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (productController.deleteProduct(id)) {
                JOptionPane.showMessageDialog(this, "Product deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete product", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        List<Product> products = productController.getAllProducts();

        for (Product product : products) {
            double inventoryValue = product.getPrice() * product.getStockQuantity();
            tableModel.addRow(new Object[]{
                product.getProductId(),
                product.getName(),
                product.getCategoryId(),
                String.format("$%.2f", product.getPrice()),
                product.getStockQuantity(),
                String.format("$%.2f", inventoryValue)
            });
        }
    }

    private void showStats() {
        int totalProducts = productController.getTotalProductCount();
        double totalValue = productController.getTotalInventoryValue();

        String stats = String.format(
            """
                Inventory Statistics
                Total Products: %d
                Total Inventory Value: $%.2f
                Average Product Value: $%.2f
            """,
            totalProducts,
            totalValue,
            totalProducts > 0 ? totalValue / totalProducts : 0
        );

        JOptionPane.showMessageDialog(this, stats, "Inventory Stats", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearFields() {
        nameField.setText("");
        priceField.setText("");
        stockField.setText("");
        categoryField.setText("");
    }
}
