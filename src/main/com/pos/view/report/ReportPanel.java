package main.com.pos.view.report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import main.com.pos.dao.ReportDAO;
import main.com.pos.model.Sale;
import main.com.pos.service.OrderService;

/**
 * Embedded Sales Reports panel for use inside Dashboard.
 */
public class ReportPanel extends JPanel {
    private final OrderService orderService;
    private final ReportDAO reportDAO;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JLabel summaryLabel;
    private JTextField dateField;

    public ReportPanel() {
        this.orderService = new OrderService();
        this.reportDAO = new ReportDAO();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel filterPanel = createFilterPanel();
        add(filterPanel, BorderLayout.NORTH);

        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        JPanel summaryPanel = createSummaryPanel();
        add(summaryPanel, BorderLayout.SOUTH);

        loadAllSales();
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createTitledBorder("Filters"));

        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField(LocalDate.now().toString(), 12);
        panel.add(dateField);

        JButton filterBtn = new JButton("Filter by Date");
        filterBtn.addActionListener(e -> filterByDate());
        panel.add(filterBtn);

        JButton allBtn = new JButton("Show All");
        allBtn.addActionListener(e -> loadAllSales());
        panel.add(allBtn);

        JButton dailyReportBtn = new JButton("Daily Report");
        dailyReportBtn.addActionListener(e -> showDailyReport());
        panel.add(dailyReportBtn);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Sales Transactions"));

        String[] columns = {"Sale ID", "User ID", "Customer ID", "Date", "Total", "Discount", "Final Amount", "Payment Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        reportTable = new JTable(tableModel);
        reportTable.setRowHeight(25);
        reportTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Summary"));
        panel.setBackground(new Color(240, 240, 240));

        summaryLabel = new JLabel("Select a date to view summary");
        summaryLabel.setFont(new Font("Arial", Font.BOLD, 12));
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(summaryLabel, BorderLayout.CENTER);

        return panel;
    }

    private void loadAllSales() {
        tableModel.setRowCount(0);
        List<Sale> sales = orderService.getAllOrders();

        for (Sale sale : sales) {
            tableModel.addRow(new Object[]{
                sale.getSaleId(),
                sale.getUserId(),
                sale.getCustomerId(),
                sale.getSaleDate().getDate(),
                String.format("$%.2f", sale.getTotal()),
                String.format("$%.2f", sale.getDiscount()),
                String.format("$%.2f", sale.getFinalTotal()),
                sale.getPaymentType()
            });
        }

        updateSummary(null);
    }

    private void filterByDate() {
        String date = dateField.getText();
        tableModel.setRowCount(0);
        List<Sale> sales = orderService.getAllOrders();
        int count = 0;
        double total = 0;

        for (Sale sale : sales) {
            if (sale.getSaleDate().getDate().toString().equals(date)) {
                tableModel.addRow(new Object[]{
                    sale.getSaleId(),
                    sale.getUserId(),
                    sale.getCustomerId(),
                    sale.getSaleDate().getDate(),
                    String.format("$%.2f", sale.getTotal()),
                    String.format("$%.2f", sale.getDiscount()),
                    String.format("$%.2f", sale.getFinalTotal()),
                    sale.getPaymentType()
                });
                count++;
                total += sale.getFinalTotal();
            }
        }

        String summary = String.format(
            "Date: %s | Transactions: %d | Total Sales: $%.2f",
            date, count, total
        );
        summaryLabel.setText(summary);
    }

    private void showDailyReport() {
        String date = dateField.getText();
        Map<String, Object> report = reportDAO.getDailySalesReport(date);
        Map<String, Integer> paymentDist = reportDAO.getPaymentTypeDistribution(date);
        double discounts = reportDAO.getTotalDiscountsForDay(date);

        StringBuilder reportText = new StringBuilder();
        reportText.append("═══════════════════════════════════════\n");
        reportText.append("          DAILY SALES REPORT\n");
        reportText.append("═══════════════════════════════════════\n\n");
        reportText.append(String.format("Date: %s\n", date));
        reportText.append(String.format("Total Sales: $%.2f\n", report.get("totalSales")));
        reportText.append(String.format("Total Transactions: %d\n", report.get("totalTransactions")));
        reportText.append(String.format("Average Transaction: $%.2f\n", report.get("averageValue")));
        reportText.append(String.format("Total Discounts: $%.2f\n", discounts));
        reportText.append("\nPayment Type Distribution:\n");
        reportText.append("───────────────────────────────────────\n");

        for (Map.Entry<String, Integer> entry : paymentDist.entrySet()) {
            reportText.append(String.format("%s: %d transactions\n", entry.getKey(), entry.getValue()));
        }

        javax.swing.JOptionPane.showMessageDialog(
            this,
            reportText.toString(),
            "Daily Sales Report - " + date,
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void updateSummary(String date) {
        if (date == null) {
            summaryLabel.setText("Total Sales: View all transactions");
        } else {
            Map<String, Object> report = reportDAO.getDailySalesReport(date);
            String summary = String.format(
                "Date: %s | Total Sales: $%.2f | Transactions: %d | Avg: $%.2f",
                date, report.get("totalSales"), report.get("totalTransactions"), report.get("averageValue")
            );
            summaryLabel.setText(summary);
        }
    }
}
