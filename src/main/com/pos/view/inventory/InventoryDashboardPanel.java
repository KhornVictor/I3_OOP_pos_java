package main.com.pos.view.inventory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import main.com.pos.components.ui.TableProduct;
import main.com.pos.dao.ProductDAO;
import main.com.pos.model.Product;
import main.com.pos.util.Telegram;

public class InventoryDashboardPanel extends JPanel {
    private JTable currentTable;
    private TableRowSorter<DefaultTableModel> sorter;
    @SuppressWarnings("unused")
    private static int selectedRowIndex;
    private static Product selectedProduct;
    private JPanel bottomActionPanel;
    private JTextField inputStock;

    public InventoryDashboardPanel() {
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(245, 246, 248));
        setBorder(new EmptyBorder(16, 16, 16, 16));
        add(createMainPanel(), BorderLayout.CENTER);
    }

    /* ================= MAIN LAYOUT ================= */

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setOpaque(false);

        JScrollPane tablePane = createTablePanel();
        extractTableReference(tablePane);

        JPanel north = new JPanel(new BorderLayout(12, 12));
        north.setOpaque(false);
        north.add(createSearchBar(), BorderLayout.NORTH);
        north.add(createSummaryPanel(), BorderLayout.CENTER);

        panel.add(north, BorderLayout.NORTH);
        panel.add(tablePane, BorderLayout.CENTER);
        bottomActionPanel = createBottomActionPanel();
        panel.add(bottomActionPanel, BorderLayout.SOUTH);
        return panel;
    }

    /* ================= SUMMARY ================= */

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 16, 0));
        panel.setOpaque(false);

        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAll();

        int totalProducts = products.size();
        int lowStockItems = 0;
        int criticalStock = 0;

        for (Product product : products) {
            int stock = product.getStockQuantity();
            if (stock < 5) criticalStock++;
            else if (stock <= 100)  lowStockItems++;
        }

        JPanel totalCard = createCard("Total Products", String.valueOf(totalProducts), new Color(59, 130, 246));
        totalCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                filterTableByStatus(null);
            }
        });

        JPanel lowStockCard = createCard("Low Stock Items", String.valueOf(lowStockItems), new Color(249, 115, 22));
        lowStockCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                filterTableByStatus("Low Stock");
            }
        });

        JPanel criticalCard = createCard("Critical Stock", String.valueOf(criticalStock), new Color(239, 68, 68));
        criticalCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                filterTableByStatus("Critical");
            }
        });

        panel.add(totalCard);
        panel.add(lowStockCard);
        panel.add(criticalCard);
        return panel;
    }

    private JPanel createCard(String title, String value, Color accent) {
        RoundedPanel card = new RoundedPanel(20);
        card.setLayout(new BorderLayout());
        card.setBackground(accent);
        card.setBorder(new EmptyBorder(16, 16, 16, 16));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valueLabel.setForeground(Color.WHITE);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(valueLabel);

        JLabel swatch = new JLabel();
        swatch.setOpaque(true);
        swatch.setBackground(accent);
        swatch.setPreferredSize(new Dimension(48, 48));
        swatch.setBorder(new EmptyBorder(12, 12, 12, 12));

        card.add(textPanel, BorderLayout.WEST);
        card.add(swatch, BorderLayout.EAST);
        return card;
    }

    /* ================= SEARCH ================= */

    private JPanel createSearchBar() {
        RoundedPanel panel = new RoundedPanel(16);
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 14, 10, 14));

        JTextField search = new JTextField();
        search.setBorder(null);
        search.setForeground(Color.GRAY);
        search.setText("Search products...");
        search.setForeground(Color.GRAY);

        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { applyFilter(); }
            @Override
            public void removeUpdate(DocumentEvent e) { applyFilter(); }
            @Override
            public void changedUpdate(DocumentEvent e) { applyFilter(); }

            private void applyFilter() {
                if (sorter == null) return;
                String text = search.getText();
                if (text == null || text.isEmpty() || text.equals("Search products...")) {
                    sorter.setRowFilter(null);
                    return;
                }
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0, 1));
            }
        });

        search.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (search.getText().equals("Search products...")) {
                    search.setText("");
                    search.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (search.getText().isEmpty()) {
                    search.setText("Search products...");
                    search.setForeground(Color.GRAY);
                }
            }
        });

        panel.add(search, BorderLayout.CENTER);
        return panel;
    }

    /* ================= TABLE ================= */

    private JScrollPane createTablePanel() {
        String[] columns = {"Product ID", "Name", "Stock", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        TableProduct.styleTable(table);
        table.setRowHeight(36);
        table.getTableHeader().setReorderingAllowed(false);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int viewRow = table.getSelectedRow();
                if (viewRow < 0) {
                    selectedRowIndex = -1;
                    selectedProduct = null;
                    createBottomActionPanelRefresh();
                    return;
                }
                selectedRowIndex = viewRow;
                int modelRow = table.convertRowIndexToModel(viewRow);
                Object idValue = table.getModel().getValueAt(modelRow, 0);
                int productId;
                try {
                    productId = (idValue instanceof Integer) ? (Integer) idValue : (idValue != null ? Integer.parseInt(idValue.toString()) : -1);
                } catch (NumberFormatException ex) {
                    selectedProduct = null;
                    createBottomActionPanelRefresh();
                    return;
                }
                selectedProduct = new ProductDAO().getById(productId);
                createBottomActionPanelRefresh();
            }
        });

        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAll();
        for (Product product : products) {
            model.addRow(new Object[] {
                product.getProductId(),
                product.getName(),
                product.getStockQuantity(),
                getStockStatus(product.getStockQuantity())
            });
        }

        table.getColumnModel().getColumn(3).setCellRenderer(new InventoryStatusBadgeRenderer());

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);    
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        return scrollPane;
    }

    private void extractTableReference(JScrollPane scrollPane) {
        Component viewport = scrollPane.getViewport().getView();
        if (viewport instanceof JTable table) {
            currentTable = table;
        }
    }

    private void filterTableByStatus(String status) {
        if (sorter == null) return;
        if (status == null) {
            sorter.setRowFilter(null);
            return;
        }
        sorter.setRowFilter(RowFilter.regexFilter("^" + status + "$", 3));
    }

    /* ================= BOTTOM ACTIONS ================= */

    private JPanel createBottomActionPanel() { 
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0)){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(229, 231, 235));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.setOpaque(false);

        if (inputStock == null) {
            inputStock = new JTextField();
        }

        
        
        JLabel productNameLabel = new JLabel("Selected Product: ");
        JLabel productNameValue = new JLabel("None");
        if (selectedProduct != null)  productNameValue.setText(selectedProduct.getName());

        JPanel productUInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(219, 234, 254));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        productUInfoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        productUInfoPanel.setOpaque(true);
        productUInfoPanel.add(productNameLabel);
        productUInfoPanel.add(productNameValue);
        inputPanel.add(productUInfoPanel);

        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(219, 234, 254));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };

        JLabel priceLabel = new JLabel("Current Stock: ");
        JLabel priceValue = new JLabel(selectedProduct != null ? String.valueOf(selectedProduct.getPrice()) + " $" : "0 $");
        if (selectedProduct != null && !inputStock.getText().isEmpty()) {
            try {
                int quantity = Integer.parseInt(inputStock.getText());
                priceValue.setText(String.valueOf(selectedProduct.getPrice() * quantity) + " $");
            } catch (NumberFormatException ex) {
                priceValue.setText("0 $");
            }
        }
        pricePanel.add(priceLabel);
        pricePanel.add(priceValue);

        pricePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        pricePanel.setOpaque(true);
        inputPanel.add(pricePanel);
        
        JLabel quantityLabel = new JLabel("Add Stock Quantity:");
        inputStock.setPreferredSize(new Dimension(100, 28));
        inputStock.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel addButton = new JLabel("Add Stock"){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#268218"));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                super.paintComponent(g);
            }
        };
        addButton.setOpaque(false); 
        addButton.setForeground(Color.WHITE); 
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setHorizontalAlignment(SwingConstants.CENTER); 
        addButton.setBorder(new EmptyBorder(8, 16, 8, 16));
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedProduct == null) {
                    JOptionPane.showMessageDialog(mainPanel, "Please select a product to add stock.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String qtyText = inputStock.getText();
                int quantity;
                try {
                    quantity = Integer.parseInt(qtyText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainPanel, "Please enter a valid quantity.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Telegram.telegramSend("Stock Added:\nProduct: " + selectedProduct.getName() + "\nQuantity: " + quantity + "\nNew Stock: " + (selectedProduct.getStockQuantity() + quantity) + "\nCost: " + ((selectedProduct.getPrice() * quantity) - (selectedProduct.getPrice() * quantity) * 0.1) + " $");
                addStockToProduct(selectedProduct.getProductId(), quantity);
                inputStock.setText("");
            }
        });

        JLabel resetButton = new JLabel("Reset"){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#ff0000"));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                super.paintComponent(g);
            }
        };
        resetButton.setOpaque(false); 
        resetButton.setForeground(Color.WHITE); 
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resetButton.setHorizontalAlignment(SwingConstants.CENTER); 
        resetButton.setBorder(new EmptyBorder(8, 16, 8, 16));
        resetButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                inputStock.setText("");
                priceValue.setText(selectedProduct != null ? selectedProduct.getPrice() + " $" : "0 $");
            }
        });

        inputPanel.add(quantityLabel);
        inputPanel.add(inputStock);
        inputPanel.add(addButton);
        inputPanel.add(resetButton);

        inputStock.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updatePrice();}

            @Override
            public void removeUpdate(DocumentEvent e) { updatePrice(); }

            @Override
            public void changedUpdate(DocumentEvent e) { updatePrice(); }

            private void updatePrice() {
                if (selectedProduct != null && !inputStock.getText().isEmpty()) {
                    try {
                        int quantity = Integer.parseInt(inputStock.getText());
                        priceValue.setText(String.valueOf((selectedProduct.getPrice() * quantity) - (selectedProduct.getPrice() * quantity) * 0.1) + " $");
                    } catch (NumberFormatException ex) {
                        priceValue.setText("0 $");
                    }
                } else {
                    priceValue.setText("0 $");
                }
            }
        });
        

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    private void createBottomActionPanelRefresh(){
        bottomActionPanel.removeAll();
        bottomActionPanel.setLayout(new BorderLayout());
        bottomActionPanel.add(createBottomActionPanel(), BorderLayout.CENTER);
        bottomActionPanel.revalidate();
        bottomActionPanel.repaint();
    }


    private String getStockStatus(int stock) {
        if (stock < 5) return "Critical";
        if (stock <= 100) return "Low Stock";
        return "Healthy";
    }

    private void addStockToProduct(int productId, int quantity) {
        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this, "Quantity must be greater than zero", "Invalid Quantity", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ProductDAO productDAO = new ProductDAO();
        Product product = productDAO.getById(productId);
        if (product == null) {
            JOptionPane.showMessageDialog(this, "Product not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean updated = productDAO.updateStock(productId, quantity);
        if (!updated) {
            JOptionPane.showMessageDialog(this, "Failed to update stock", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        refreshTable();
    }

    private void refreshTable() {
        if (currentTable == null) return;
        currentTable.clearSelection();
        DefaultTableModel model = (DefaultTableModel) currentTable.getModel();
        model.setRowCount(0);

        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAll();
        for (Product product : products) {
            model.addRow(new Object[] {
                product.getProductId(),
                product.getName(),
                product.getStockQuantity(),
                getStockStatus(product.getStockQuantity())
            });
        }
    }

    /* ================= RENDERERS & UI UTILS ================= */

    private static class InventoryStatusBadgeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);

            String status = value == null ? "" : value.toString();
            switch (status) {
                case "Healthy" -> {
                    label.setBackground(new Color(220, 252, 231));
                    label.setForeground(new Color(22, 163, 74));
                }
                case "Low Stock" -> {
                    label.setBackground(new Color(255, 247, 237));
                    label.setForeground(new Color(234, 88, 12));
                }
                case "Critical" -> {
                    label.setBackground(new Color(254, 226, 226));
                    label.setForeground(new Color(220, 38, 38));
                }
                default -> {
                    label.setBackground(Color.WHITE);
                    label.setForeground(Color.GRAY);
                }
            }

            if (isSelected) {
                label.setBackground(label.getBackground().darker());
                label.setForeground(Color.WHITE);
            }
            return label;
        }
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;

        RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }

    /* ================= MAIN (MANUAL PREVIEW) ================= */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Inventory Dashboard Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new InventoryDashboardPanel());
            frame.setVisible(true);
        });
    }
}
