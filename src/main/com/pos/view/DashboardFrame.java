package main.com.pos.view;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class DashboardFrame extends JFrame {
    private JLabel dateTimeLabel;
    private JLabel totalSalesLabel;
    private JLabel transactionLabel;
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JTextField itemNameField;
    private JTextField priceField;
    private JTextField quantityField;

    public DashboardFrame() {
        setTitle("POS System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initializeComponents();
        startClockUpdate();
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout());
        
        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Content
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        
        // Footer
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setLayout(new BorderLayout(10, 0));
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        headerPanel.setPreferredSize(new Dimension(0, 60));
        
        JLabel titleLabel = new JLabel("Point of Sale Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        
        dateTimeLabel = new JLabel();
        dateTimeLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        dateTimeLabel.setForeground(Color.WHITE);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 11));
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> logout());
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(dateTimeLabel, BorderLayout.CENTER);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(1, 2, 10, 0));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(new Color(240, 240, 240));
        
        contentPanel.add(createInputPanel());
        contentPanel.add(createCartPanel());
        
        return contentPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Product"));
        
        // Item Name
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        itemPanel.setBackground(Color.WHITE);
        JLabel itemLabel = new JLabel("Item Name:");
        itemLabel.setPreferredSize(new Dimension(100, 25));
        itemNameField = new JTextField(15);
        itemPanel.add(itemLabel);
        itemPanel.add(itemNameField);
        
        // Price
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pricePanel.setBackground(Color.WHITE);
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setPreferredSize(new Dimension(100, 25));
        priceField = new JTextField(15);
        pricePanel.add(priceLabel);
        pricePanel.add(priceField);
        
        // Quantity
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quantityPanel.setBackground(Color.WHITE);
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setPreferredSize(new Dimension(100, 25));
        quantityField = new JTextField(15);
        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantityField);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addButton = new JButton("Add Item");
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> addItemToCart());
        
        JButton clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(52, 73, 94));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(e -> clearInputFields());
        
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        
        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 5, 10));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel label1 = new JLabel("Total Sales:");
        label1.setFont(new Font("Arial", Font.BOLD, 11));
        totalSalesLabel = new JLabel("$0.00");
        totalSalesLabel.setFont(new Font("Arial", Font.BOLD, 12));
        totalSalesLabel.setForeground(new Color(46, 204, 113));
        
        JLabel label2 = new JLabel("Transactions:");
        label2.setFont(new Font("Arial", Font.BOLD, 11));
        transactionLabel = new JLabel("0");
        transactionLabel.setFont(new Font("Arial", Font.BOLD, 12));
        transactionLabel.setForeground(new Color(46, 204, 113));
        
        JLabel label3 = new JLabel("Items in Cart:");
        label3.setFont(new Font("Arial", Font.BOLD, 11));
        JLabel itemCountLabel = new JLabel("0");
        itemCountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        itemCountLabel.setForeground(new Color(46, 204, 113));
        
        statsPanel.add(label1);
        statsPanel.add(totalSalesLabel);
        statsPanel.add(label2);
        statsPanel.add(transactionLabel);
        statsPanel.add(label3);
        statsPanel.add(itemCountLabel);
        
        inputPanel.add(itemPanel);
        inputPanel.add(pricePanel);
        inputPanel.add(quantityPanel);
        inputPanel.add(buttonPanel);
        inputPanel.add(Box.createVerticalStrut(20));
        inputPanel.add(new JSeparator());
        inputPanel.add(statsPanel);
        inputPanel.add(Box.createVerticalGlue());
        
        return inputPanel;
    }

    private JPanel createCartPanel() {
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBackground(Color.WHITE);
        cartPanel.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));
        
        // Table Setup
        String[] columnNames = {"Product", "Price", "Quantity", "Total"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cartTable = new JTable(tableModel);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 12));
        cartTable.setRowHeight(25);
        cartTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        cartTable.getTableHeader().setBackground(new Color(52, 73, 94));
        cartTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(cartTable);
        cartPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(Color.WHITE);
        
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        checkoutButton.setBackground(new Color(46, 204, 113));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFocusPainted(false);
        checkoutButton.addActionListener(e -> checkout());
        
        JButton removeButton = new JButton("Remove Selected");
        removeButton.setFont(new Font("Arial", Font.BOLD, 12));
        removeButton.setBackground(new Color(231, 76, 60));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        removeButton.addActionListener(e -> removeSelectedItem());
        
        actionPanel.add(removeButton);
        actionPanel.add(checkoutButton);
        
        cartPanel.add(actionPanel, BorderLayout.SOUTH);
        
        return cartPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(new Color(52, 73, 94));
        footerPanel.setPreferredSize(new Dimension(0, 40));
        
        JLabel footerLabel = new JLabel("© 2025 POS System - All Rights Reserved");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        footerLabel.setForeground(Color.WHITE);
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }

    private void addItemToCart() {
        try {
            String itemName = itemNameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());
            
            if (itemName.isEmpty() || price <= 0 || quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter valid information!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double total = price * quantity;
            Object[] row = {itemName, String.format("$%.2f", price), quantity, String.format("$%.2f", total)};
            tableModel.addRow(row);
            
            updateTotals();
            clearInputFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price and Quantity must be numbers!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeSelectedItem() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
            updateTotals();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to remove!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void checkout() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, "Checkout successful!\nThank you for your purchase!", "Success", JOptionPane.INFORMATION_MESSAGE);
        tableModel.setRowCount(0);
        updateTotals();
    }

    private void updateTotals() {
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String totalStr = tableModel.getValueAt(i, 3).toString().replaceAll("[^0-9.]", "");
            total += Double.parseDouble(totalStr);
        }
        totalSalesLabel.setText(String.format("$%.2f", total));
        transactionLabel.setText(String.valueOf(tableModel.getRowCount()));
    }

    private void clearInputFields() {
        itemNameField.setText("");
        priceField.setText("");
        quantityField.setText("");
    }

    private void logout() {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }

    private void startClockUpdate() {
        new Thread(() -> {
            while (true) {
                try {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    SwingUtilities.invokeLater(() -> dateTimeLabel.setText(now.format(formatter)));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }
}
