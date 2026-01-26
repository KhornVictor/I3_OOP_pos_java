package main.com.pos.view.report;

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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import main.com.pos.components.ui.TableProduct;
import main.com.pos.dao.ReportDAO;
import main.com.pos.dao.UserDAO;
import main.com.pos.model.Sale;
import main.com.pos.model.User;
import main.com.pos.service.OrderService;

/**
 * Modern, dynamic Sales Reports panel with real-time metrics and filtering.
 */
public class ReportPanel extends JPanel {
    private final OrderService orderService;
    private final ReportDAO reportDAO;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextField dateField;
    private JLabel totalSalesValue;
    private JLabel totalTransactionsValue;
    private JLabel avgTransactionValue;
    private JLabel totalDiscountsValue;
    private JComboBox<String> filterTypeCombo;
    private String currentFilterDate = null;
    private User currentUsers;
    private final UserDAO userDAO;

    public ReportPanel() {
        this.orderService = new OrderService();
        this.reportDAO = new ReportDAO();
        this.userDAO = new UserDAO();
        initUI();
        applyFilter();
    }

    private void initUI() {
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(245, 246, 248));
        setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setOpaque(false);

        JPanel topSection = new JPanel(new BorderLayout(12, 12));
        topSection.setOpaque(false);
        topSection.add(createFilterPanel(), BorderLayout.NORTH);
        topSection.add(createMetricsPanel(), BorderLayout.CENTER);

        mainPanel.add(topSection, BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Load all transactions initially
        loadAllSales();
    }


    /* ================= FILTER PANEL ================= */

    private JPanel createFilterPanel() {
        RoundedPanel panel = new RoundedPanel(16);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel filterLabel = new JLabel("Filter by:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(filterLabel);

        String[] filterTypes = {"All Transactions", "Today", "Specific Date"};
        filterTypeCombo = new JComboBox<>(filterTypes);
        filterTypeCombo.setPreferredSize(new Dimension(150, 32));
        filterTypeCombo.addActionListener(e -> onFilterTypeChanged());
        panel.add(filterTypeCombo);

        JLabel dateLabel = new JLabel("Date:");
        panel.add(dateLabel);

        dateField = new JTextField(LocalDate.now().toString(), 12);
        dateField.setPreferredSize(new Dimension(120, 32));
        dateField.setHorizontalAlignment(SwingConstants.CENTER);
        dateField.setEnabled(false); // Disabled initially for "All Transactions"
        panel.add(dateField);

        JButton applyBtn = createStyledButton("Apply Filter", new Color(59, 130, 246));
        applyBtn.addActionListener(e -> applyFilter());
        panel.add(applyBtn);

        JButton reportBtn = createStyledButton("Daily Report", new Color(16, 185, 129));
        reportBtn.addActionListener(e -> showDailyReport());
        panel.add(reportBtn);

        JButton topProductsBtn = createStyledButton("Top Products", new Color(139, 92, 246));
        topProductsBtn.addActionListener(e -> showTopProducts());
        panel.add(topProductsBtn);

        JButton categoriesBtn = createStyledButton("Categories", new Color(236, 72, 153));
        categoriesBtn.addActionListener(e -> showCategoriesReport());
        panel.add(categoriesBtn);

        JButton usersBtn = createStyledButton("Users", new Color(245, 158, 11));
        usersBtn.addActionListener(e -> showUserPerformance());
        panel.add(usersBtn);

        return panel;
    }

    private void onFilterTypeChanged() {
        String selected = (String) filterTypeCombo.getSelectedItem();
        if (null == selected) {
            dateField.setEnabled(true);
        } else switch (selected) {
            case "Today" -> {
                dateField.setText(LocalDate.now().toString());
                dateField.setEnabled(false);
                loadTodaysSales();
            }
            case "All Transactions" -> {
                dateField.setEnabled(false);
                loadAllSales();
            }
            default -> dateField.setEnabled(true);
        }
    }

    private void applyFilter() {
        String selected = (String) filterTypeCombo.getSelectedItem();
        if ("Specific Date".equals(selected) || "Today".equals(selected)) {
            filterByDate();
        } else {
            loadAllSales();
        }
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isEnabled() ? bgColor : bgColor.darker());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(120, 32));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    /* ================= METRICS PANEL ================= */

    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 16, 0));
        panel.setOpaque(false);

        RoundedPanel totalSalesCard = createMetricCard(
            "Total Sales",
            "$0.00",
            new Color(59, 130, 246),
            "💰"
        );
        totalSalesValue = (JLabel) ((JPanel) totalSalesCard.getComponent(0)).getComponent(1);

        RoundedPanel transactionsCard = createMetricCard(
            "Transactions",
            "0",
            new Color(16, 185, 129),
            "📊"
        );
        totalTransactionsValue = (JLabel) ((JPanel) transactionsCard.getComponent(0)).getComponent(1);

        RoundedPanel avgCard = createMetricCard(
            "Avg Transaction",
            "$0.00",
            new Color(249, 115, 22),
            "📈"
        );
        avgTransactionValue = (JLabel) ((JPanel) avgCard.getComponent(0)).getComponent(1);

        RoundedPanel discountsCard = createMetricCard(
            "Total Discounts",
            "$0.00",
            new Color(239, 68, 68),
            "🎫"
        );
        totalDiscountsValue = (JLabel) ((JPanel) discountsCard.getComponent(0)).getComponent(1);

        panel.add(totalSalesCard);
        panel.add(transactionsCard);
        panel.add(avgCard);
        panel.add(discountsCard);

        return panel;
    }

    private RoundedPanel createMetricCard(String title, String value, Color bgColor, String emoji) {
        RoundedPanel card = new RoundedPanel(16);
        card.setLayout(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(new Color(255, 255, 255, 200));
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        textPanel.add(titleLabel);
        textPanel.add(valueLabel);

        JLabel iconLabel = new JLabel(emoji);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(iconLabel, BorderLayout.EAST);

        return card;
    }


    /* ================= TABLE PANEL ================= */

    private JPanel createTablePanel() {
        RoundedPanel panel = new RoundedPanel(16);
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel tableTitle = new JLabel("Sales Transactions");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setBorder(new EmptyBorder(0, 0, 12, 0));

        String[] columns = {"Sale ID", "Cashier", "Customer", "Date", "Total", "Discount", "Final Amount", "Payment"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        reportTable = new JTable(tableModel);
        TableProduct.styleTable(reportTable);
        reportTable.setRowHeight(40);
        reportTable.getTableHeader().setReorderingAllowed(false);

        // Center alignment for specific columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        reportTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Sale ID
        reportTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Customer

        // Custom renderers for currency columns
        reportTable.getColumnModel().getColumn(4).setCellRenderer(new ModernCurrencyRenderer());
        reportTable.getColumnModel().getColumn(5).setCellRenderer(new ModernCurrencyRenderer());
        reportTable.getColumnModel().getColumn(6).setCellRenderer(new ModernCurrencyRenderer());
        
        // Payment type badge renderer
        reportTable.getColumnModel().getColumn(7).setCellRenderer(new PaymentTypeBadgeRenderer());

        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(tableTitle, BorderLayout.NORTH);
        wrapper.add(scrollPane, BorderLayout.CENTER);

        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    /* ================= DATA LOADING ================= */

    private void loadTodaysSales() {
        currentFilterDate = LocalDate.now().toString();
        dateField.setText(currentFilterDate);
        filterByDate();
    }

    private void loadAllSales() {
        tableModel.setRowCount(0);
        currentFilterDate = null;
        
        // Use ReportDAO to get detailed sales data for all time
        List<Map<String, Object>> salesData = reportDAO.getAllDetailedSalesData();
        
        // If today has no data, fall back to OrderService for all transactions
        if (salesData.isEmpty()) {
            List<Sale> sales = orderService.getAllOrders();
            double totalSales = 0;
            double totalDiscounts = 0;

            for (Sale sale : sales) {
                currentUsers = userDAO.getById(sale.getUserId());
                tableModel.addRow(new Object[]{
                    sale.getSaleId(),
                    currentUsers.getName(),
                    sale.getCustomerId() == 0 ? "Guest" : String.valueOf(sale.getCustomerId()),
                    sale.getSaleDate().getDate(),
                    sale.getTotal(),
                    sale.getDiscount(),
                    sale.getFinalTotal(),
                    sale.getPaymentType()
                });
                totalSales += sale.getFinalTotal();
                totalDiscounts += sale.getDiscount();
            }

            updateMetrics(totalSales, sales.size(), 
                         sales.isEmpty() ? 0 : totalSales / sales.size(), 
                         totalDiscounts);
        } else loadSalesFromDAO(salesData);
    }

    private void filterByDate() {
        String dateText = dateField.getText().trim();
        if (dateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date", "Invalid Date", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateText);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Date must be in ISO format (YYYY-MM-DD)", "Invalid Date", JOptionPane.WARNING_MESSAGE);
            return;
        }

        currentFilterDate = date.toString();
        tableModel.setRowCount(0);
        
        // Use ReportDAO to get detailed sales data with user and customer names
        List<Map<String, Object>> salesData = reportDAO.getDetailedSalesData(currentFilterDate);
        
        if (!salesData.isEmpty()) {
            loadSalesFromDAO(salesData);
        } else {
            // Fallback to OrderService if no data
            List<Sale> sales = orderService.getOrdersByDate(date);
            double totalSales = 0;
            double totalDiscounts = 0;
            int count = 0;

            for (Sale sale : sales) {
                tableModel.addRow(new Object[]{
                    sale.getSaleId(),
                    currentUsers.getName(),
                    sale.getCustomerId() == 0 ? "Guest" : String.valueOf(sale.getCustomerId()),
                    sale.getSaleDate().getDate(),
                    sale.getTotal(),
                    sale.getDiscount(),
                    sale.getFinalTotal(),
                    sale.getPaymentType()
                });
                count++;
                totalSales += sale.getFinalTotal();
                totalDiscounts += sale.getDiscount();
            }

            double avgTransaction = count > 0 ? totalSales / count : 0;
            updateMetrics(totalSales, count, avgTransaction, totalDiscounts);
        }
    }

    /**
     * Load sales data from ReportDAO detailed sales data
     */
    private void loadSalesFromDAO(List<Map<String, Object>> salesData) {
        tableModel.setRowCount(0);
        double totalSales = 0;
        double totalDiscounts = 0;

        for (Map<String, Object> sale : salesData) {
            tableModel.addRow(new Object[]{
                sale.get("saleId"),
                sale.get("cashierName"),  // Show cashier name instead of user ID
                sale.get("customerName"),
                sale.get("dateTime"),
                sale.get("total"),
                sale.get("discount"),
                sale.get("finalAmount"),
                sale.get("paymentType")
            });
            totalSales += (Double) sale.get("finalAmount");
            totalDiscounts += (Double) sale.get("discount");
        }

        double avgTransaction = salesData.isEmpty() ? 0 : totalSales / salesData.size();
        updateMetrics(totalSales, salesData.size(), avgTransaction, totalDiscounts);
    }

    private void updateMetrics(double totalSales, int transactions, double avgTransaction, double totalDiscounts) {
        totalSalesValue.setText(String.format("$%.2f", totalSales));
        totalTransactionsValue.setText(String.valueOf(transactions));
        avgTransactionValue.setText(String.format("$%.2f", avgTransaction));
        totalDiscountsValue.setText(String.format("$%.2f", totalDiscounts));
    }


    /* ================= DAILY REPORT ================= */

    private void showDailyReport() {
        String date = currentFilterDate != null ? currentFilterDate : dateField.getText();
        Map<String, Object> report = reportDAO.getDailySalesReport(date);
        List<Map<String, Object>> paymentRevenue = reportDAO.getPaymentTypeRevenue(date);
        // Create modern report panel with tabs or sections
        JPanel reportPanel = new JPanel(new BorderLayout(16, 16));
        reportPanel.setBackground(Color.WHITE);
        reportPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        reportPanel.setPreferredSize(new Dimension(650, 500));

        // Header
        JLabel headerLabel = new JLabel("Daily Sales Report");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(new Color(59, 130, 246));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Date
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(Color.GRAY);
        dateLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        headerPanel.add(headerLabel);
        headerPanel.add(dateLabel);

        // Metrics
        JPanel metricsPanel = new JPanel(new GridLayout(4, 2, 12, 12));
        metricsPanel.setOpaque(false);
        metricsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        addReportMetric(metricsPanel, "Total Sales:", String.format("$%.2f", (Double) report.get("totalSales")));
        addReportMetric(metricsPanel, "Transactions:", String.valueOf(report.get("totalTransactions")));
        addReportMetric(metricsPanel, "Average Value:", String.format("$%.2f", (Double) report.get("averageValue")));
        addReportMetric(metricsPanel, "Total Discounts:", String.format("$%.2f", (Double) report.get("totalDiscounts")));

        // Payment Distribution with Revenue
        JPanel paymentPanel = new JPanel(new BorderLayout());
        paymentPanel.setOpaque(false);
        
        JLabel paymentTitle = new JLabel("Payment Type Distribution");
        paymentTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        paymentTitle.setBorder(new EmptyBorder(0, 0, 8, 0));

        JPanel paymentList = new JPanel(new GridLayout(0, 3, 8, 8));
        paymentList.setOpaque(false);
        
        for (Map<String, Object> payment : paymentRevenue) {
            JLabel typeLabel = new JLabel(payment.get("paymentType") + ":");
            typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            
            JLabel countLabel = new JLabel(payment.get("transactionCount") + " txns");
            countLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            countLabel.setForeground(new Color(59, 130, 246));
            
            JLabel revenueLabel = new JLabel(String.format("$%.2f", payment.get("totalRevenue")));
            revenueLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            revenueLabel.setForeground(new Color(16, 185, 129));
            
            paymentList.add(typeLabel);
            paymentList.add(countLabel);
            paymentList.add(revenueLabel);
        }

        paymentPanel.add(paymentTitle, BorderLayout.NORTH);
        paymentPanel.add(paymentList, BorderLayout.CENTER);

        // Assemble
        JPanel contentPanel = new JPanel(new BorderLayout(16, 16));
        contentPanel.setOpaque(false);
        contentPanel.add(metricsPanel, BorderLayout.NORTH);
        contentPanel.add(paymentPanel, BorderLayout.CENTER);

        reportPanel.add(headerPanel, BorderLayout.NORTH);
        reportPanel.add(contentPanel, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
            this,
            reportPanel,
            "Daily Sales Report",
            JOptionPane.PLAIN_MESSAGE
        );
    }

    private void addReportMetric(JPanel panel, String label, String value) {
        JLabel keyLabel = new JLabel(label);
        keyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        keyLabel.setForeground(Color.DARK_GRAY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(new Color(31, 41, 55));

        panel.add(keyLabel);
        panel.add(valueLabel);
    }

    /* ================= TOP PRODUCTS REPORT ================= */

    private void showTopProducts() {
        String date = currentFilterDate != null ? currentFilterDate : LocalDate.now().toString();
        List<Map<String, Object>> topProducts = reportDAO.getTopSellingProducts(date, date, 10);
        
        if (topProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No product data available for this date.", 
                "No Data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create table for top products
        String[] columns = {"#", "Product", "Category", "Qty Sold", "Revenue", "Transactions"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        int rank = 1;
        for (Map<String, Object> product : topProducts) {
            model.addRow(new Object[]{
                rank++,
                product.get("productName"),
                product.get("categoryName"),
                product.get("totalQuantity"),
                String.format("$%.2f", product.get("totalRevenue")),
                product.get("transactionCount")
            });
        }

        JTable table = new JTable(model);
        TableProduct.styleTable(table);
        table.setRowHeight(35);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 400));

        JOptionPane.showMessageDialog(this, scrollPane, 
            "Top 10 Products - " + date, JOptionPane.PLAIN_MESSAGE);
    }

    /* ================= CATEGORIES REPORT ================= */

    private void showCategoriesReport() {
        String date = currentFilterDate != null ? currentFilterDate : LocalDate.now().toString();
        List<Map<String, Object>> categories = reportDAO.getSalesByCategory(date, date);
        
        if (categories.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No category data available for this date.", 
                "No Data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create table for categories
        String[] columns = {"Category", "Items Sold", "Total Qty", "Revenue", "Avg Price"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        for (Map<String, Object> category : categories) {
            model.addRow(new Object[]{
                category.get("categoryName"),
                category.get("itemsSold"),
                category.get("totalQuantity"),
                String.format("$%.2f", category.get("totalRevenue")),
                String.format("$%.2f", category.get("avgPrice"))
            });
        }

        JTable table = new JTable(model);
        TableProduct.styleTable(table);
        table.setRowHeight(35);
        
        // Right align currency columns
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(650, 350));

        JOptionPane.showMessageDialog(this, scrollPane, 
            "Sales by Category - " + date, JOptionPane.PLAIN_MESSAGE);
    }

    /* ================= USER PERFORMANCE REPORT ================= */

    private void showUserPerformance() {
        String date = currentFilterDate != null ? currentFilterDate : LocalDate.now().toString();
        List<Map<String, Object>> users = reportDAO.getUserPerformance(date, date);
        
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No user performance data available for this date.", 
                "No Data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create table for user performance
        String[] columns = {"Cashier", "Role", "Transactions", "Revenue", "Avg Sale", "Discounts"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        for (Map<String, Object> user : users) {
            model.addRow(new Object[]{
                user.get("name"),
                user.get("role"),
                user.get("transactionCount"),
                String.format("$%.2f", user.get("totalRevenue")),
                String.format("$%.2f", user.get("avgTransaction")),
                String.format("$%.2f", user.get("totalDiscounts"))
            });
        }

        JTable table = new JTable(model);
        TableProduct.styleTable(table);
        table.setRowHeight(35);
        
        // Right align currency columns
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(750, 350));

        JOptionPane.showMessageDialog(this, scrollPane, 
            "User Performance - " + date, JOptionPane.PLAIN_MESSAGE);
    }

    /* ================= CUSTOM RENDERERS ================= */

    private static class ModernCurrencyRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            
            if (value instanceof Number amount) {
                label.setText(String.format("$%.2f", amount.doubleValue()));
                
                // Color coding for amounts
                if (!isSelected) {
                    if (amount.doubleValue() > 0) {
                        label.setForeground(new Color(22, 163, 74)); // Green
                    } else {
                        label.setForeground(Color.DARK_GRAY);
                    }
                }
            }
            
            return label;
        }
    }

    private static class PaymentTypeBadgeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);
            label.setFont(new Font("Segoe UI", Font.BOLD, 11));

            String paymentType = value == null ? "" : value.toString();
            
            switch (paymentType.toLowerCase()) {
                case "cash" -> {
                    label.setBackground(new Color(220, 252, 231));
                    label.setForeground(new Color(22, 163, 74));
                }
                case "card", "credit card", "debit card" -> {
                    label.setBackground(new Color(219, 234, 254));
                    label.setForeground(new Color(59, 130, 246));
                }
                case "digital wallet", "e-wallet" -> {
                    label.setBackground(new Color(254, 243, 199));
                    label.setForeground(new Color(202, 138, 4));
                }
                default -> {
                    label.setBackground(new Color(243, 244, 246));
                    label.setForeground(new Color(107, 114, 128));
                }
            }

            if (isSelected) {
                label.setBackground(label.getBackground().darker());
            }
            
            return label;
        }
    }

    /* ================= ROUNDED PANEL ================= */

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
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JFrame frame = new javax.swing.JFrame("Modern Sales Report Panel");
            frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            frame.setSize(1400, 800);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new ReportPanel());
            frame.setVisible(true);
        });
    }
}
