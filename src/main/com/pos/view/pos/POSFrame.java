package main.com.pos.view.pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import main.com.pos.controller.POSController;
import main.com.pos.model.DateTime;
import main.com.pos.model.Product;
import main.com.pos.model.Sale;
import main.com.pos.model.User;

public class POSFrame extends JFrame {
    private final POSController posController;
    private final User currentUser;
    private JComboBox<Product> productCombo;
    private JSpinner quantitySpinner;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel totalLabel;
    private JLabel discountLabel;
    private JLabel finalTotalLabel;

    public POSFrame(User currentUser) {
        this.currentUser = currentUser;
        this.posController = new POSController();
        initUI();
    }

    private void initUI() {
        setTitle("POS - Point of Sale");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel with user info
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Main content
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Left side - Product Selection
        JPanel productPanel = createProductPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.weighty = 1;
        mainPanel.add(productPanel, gbc);

        // Right side - Cart
        JPanel cartPanel = createCartPanel();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        mainPanel.add(cartPanel, gbc);

        // Bottom - Actions
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(51, 102, 153));
        JLabel userLabel = new JLabel("User: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(userLabel);
        return panel;
    }

    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Products"));

        // Product list
        List<Product> products = posController.getAvailableProducts();
        productCombo = new JComboBox<>(products.toArray(new Product[products.size()]));
        productCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            if (value != null) {
                label.setText(value.getName() + " - $" + String.format("%.2f", value.getPrice()));
            }
            label.setOpaque(true);
            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            }
            return label;
        });

        JPanel selectionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridy = 0;
        selectionPanel.add(new JLabel("Product:"), gbc);
        gbc.gridy = 1;
        selectionPanel.add(productCombo, gbc);

        gbc.gridy = 2;
        selectionPanel.add(new JLabel("Quantity:"), gbc);
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        gbc.gridy = 3;
        selectionPanel.add(quantitySpinner, gbc);

        JButton addBtn = new JButton("Add to Cart");
        addBtn.setBackground(new Color(34, 139, 34));
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> addToCart());
        gbc.gridy = 4;
        selectionPanel.add(addBtn, gbc);

        panel.add(selectionPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));

        // Cart table
        String[] columns = {"Item", "Qty", "Unit Price", "Total"};
        cartTableModel = new DefaultTableModel(columns, 0);
        cartTable = new JTable(cartTableModel);
        cartTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(cartTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Totals panel
        JPanel totalsPanel = new JPanel(new GridBagLayout());
        totalsPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridy = 0;
        gbc.weightx = 0.5;
        totalsPanel.add(new JLabel("Subtotal:"), gbc);
        gbc.weightx = 0.5;
        totalLabel = new JLabel("$0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 12));
        totalsPanel.add(totalLabel, gbc);

        gbc.gridy = 1;
        gbc.weightx = 0.5;
        totalsPanel.add(new JLabel("Discount:"), gbc);
        gbc.weightx = 0.5;
        discountLabel = new JLabel("$0.00");
        totalsPanel.add(discountLabel, gbc);

        gbc.gridy = 2;
        gbc.weightx = 0.5;
        totalsPanel.add(new JLabel("Final Total:"), gbc);
        gbc.weightx = 0.5;
        finalTotalLabel = new JLabel("$0.00");
        finalTotalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        finalTotalLabel.setForeground(new Color(34, 139, 34));
        totalsPanel.add(finalTotalLabel, gbc);

        JButton removeBtn = new JButton("Remove Selected");
        removeBtn.setBackground(new Color(220, 20, 60));
        removeBtn.setForeground(Color.WHITE);
        removeBtn.addActionListener(e -> removeFromCart());
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        totalsPanel.add(removeBtn, gbc);

        panel.add(totalsPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(230, 230, 230));

        JButton applyDiscountBtn = new JButton("Apply Discount");
        applyDiscountBtn.addActionListener(e -> applyDiscount());
        panel.add(applyDiscountBtn);

        JButton completeBtn = new JButton("Complete Sale");
        completeBtn.setBackground(new Color(34, 139, 34));
        completeBtn.setForeground(Color.WHITE);
        completeBtn.setFont(new Font("Arial", Font.BOLD, 12));
        completeBtn.addActionListener(e -> completeSale());
        panel.add(completeBtn);

        JButton clearBtn = new JButton("Clear Cart");
        clearBtn.setBackground(new Color(220, 20, 60));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.addActionListener(e -> clearCart());
        panel.add(clearBtn);

        return panel;
    }

    private void addToCart() {
        Product product = (Product) productCombo.getSelectedItem();
        int quantity = (Integer) quantitySpinner.getValue();

        if (product == null) {
            JOptionPane.showMessageDialog(this, "Please select a product", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (posController.addItemToCart(product.getProductId(), quantity)) {
            addRowToCart(product, quantity);
            updateTotals();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add item to cart", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRowToCart(Product product, int quantity) {
        double itemTotal = product.getPrice() * quantity;
        cartTableModel.addRow(new Object[]{
            product.getName(),
            quantity,
            String.format("$%.2f", product.getPrice()),
            String.format("$%.2f", itemTotal)
        });
    }

    private void removeFromCart() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow >= 0) {
            cartTableModel.removeRow(selectedRow);
            posController.removeItemFromCart(selectedRow);
            updateTotals();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to remove", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void applyDiscount() {
        String discountStr = JOptionPane.showInputDialog(this, "Enter discount amount:", "0");
        if (discountStr != null) {
            try {
                double discount = Double.parseDouble(discountStr);
                posController.applyDiscount(discount);
                updateTotals();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid discount amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateTotals() {
        double total = posController.getCartTotal();
        totalLabel.setText(String.format("$%.2f", total));
        finalTotalLabel.setText(String.format("$%.2f", posController.getCartFinalTotal()));
    }

    private void completeSale() {
        if (cartTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Cart is empty", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String paymentType = JOptionPane.showInputDialog(this, "Payment Type (Cash/Card/Check):", "Cash");
        if (paymentType == null) return;

        int customerId = 0;
        try {
            String customerIdStr = JOptionPane.showInputDialog(this, "Customer ID (or 0 for walk-in):", "0");
            customerId = Integer.parseInt(customerIdStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid customer ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Sale sale = new Sale(0, currentUser.getUserId(), new DateTime(), customerId, 
                            posController.getCartTotal(), 0, paymentType);
        posController.initializeSale(sale);

        if (posController.completeSale(currentUser.getUserId(), customerId, paymentType)) {
            JOptionPane.showMessageDialog(this, "Sale completed successfully!\nTotal: " + 
                                        String.format("$%.2f", posController.getCartFinalTotal()), 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            clearCart();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to complete sale", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearCart() {
        cartTableModel.setRowCount(0);
        posController.clearCart();
        totalLabel.setText("$0.00");
        discountLabel.setText("$0.00");
        finalTotalLabel.setText("$0.00");
    }
}
