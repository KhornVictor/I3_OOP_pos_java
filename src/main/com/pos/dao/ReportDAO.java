package main.com.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import main.com.pos.database.DBConnection;
import main.com.pos.util.Color;

public class ReportDAO {

    // ==================== DAILY SALES METRICS ====================

    /**
     * Get total sales amount for a specific date (Total - Discount)
     */
    public double getTotalSalesForDay(String date) {
        String sql = "SELECT COALESCE(SUM(Total - Discount), 0) as TotalSales FROM Sale WHERE DATE(SaleDate) = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, date);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("TotalSales");
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getTotalSalesForDay error: " + error.getMessage() + Color.RESET);
        }
        return 0.0;
    }

    /**
     * Get total number of transactions for a specific date
     */
    public int getTotalTransactionsForDay(String date) {
        String sql = "SELECT COUNT(*) as TotalTransactions FROM Sale WHERE DATE(SaleDate) = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, date);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("TotalTransactions");
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getTotalTransactionsForDay error: " + error.getMessage() + Color.RESET);
        }
        return 0;
    }

    /**
     * Get average transaction value for a specific date
     */
    public double getAverageTransactionValue(String date) {
        String sql = "SELECT COALESCE(AVG(Total - Discount), 0) as AvgValue FROM Sale WHERE DATE(SaleDate) = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, date);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("AvgValue");
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getAverageTransactionValue error: " + error.getMessage() + Color.RESET);
        }
        return 0.0;
    }

    /**
     * Get total discounts given for a specific date
     */
    public double getTotalDiscountsForDay(String date) {
        String sql = "SELECT COALESCE(SUM(Discount), 0) as TotalDiscounts FROM Sale WHERE DATE(SaleDate) = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, date);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("TotalDiscounts");
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getTotalDiscountsForDay error: " + error.getMessage() + Color.RESET);
        }
        return 0.0;
    }

    /**
     * Get comprehensive daily sales report
     */
    public Map<String, Object> getDailySalesReport(String date) {
        Map<String, Object> report = new HashMap<>();
        report.put("date", date);
        report.put("totalSales", getTotalSalesForDay(date));
        report.put("totalTransactions", getTotalTransactionsForDay(date));
        report.put("averageValue", getAverageTransactionValue(date));
        report.put("totalDiscounts", getTotalDiscountsForDay(date));
        return report;
    }

    // ==================== MONTHLY SALES METRICS ====================

    /**
     * Get total sales for a specific month
     */
    public double getTotalSalesForMonth(String year, String month) {
        String sql = "SELECT COALESCE(SUM(Total - Discount), 0) as TotalSales FROM Sale WHERE YEAR(SaleDate) = ? AND MONTH(SaleDate) = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, year);
            preparedStatement.setString(2, month);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("TotalSales");
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getTotalSalesForMonth error: " + error.getMessage() + Color.RESET);
        }
        return 0.0;
    }

    /**
     * Get total transactions for a specific month
     */
    public int getTotalTransactionsForMonth(String year, String month) {
        String sql = "SELECT COUNT(*) as TotalTransactions FROM Sale WHERE YEAR(SaleDate) = ? AND MONTH(SaleDate) = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, year);
            preparedStatement.setString(2, month);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("TotalTransactions");
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getTotalTransactionsForMonth error: " + error.getMessage() + Color.RESET);
        }
        return 0;
    }

    // ==================== PAYMENT TYPE ANALYTICS ====================

    /**
     * Get distribution of payment types for a specific date
     */
    public Map<String, Integer> getPaymentTypeDistribution(String date) {
        Map<String, Integer> distribution = new HashMap<>();
        String sql = "SELECT PaymentType, COUNT(*) as Count FROM Sale WHERE DATE(SaleDate) = ? GROUP BY PaymentType";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, date);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    distribution.put(resultSet.getString("PaymentType"), resultSet.getInt("Count"));
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getPaymentTypeDistribution error: " + error.getMessage() + Color.RESET);
        }
        return distribution;
    }

    /**
     * Get payment type distribution with revenue for a date
     */
    public List<Map<String, Object>> getPaymentTypeRevenue(String date) {
        List<Map<String, Object>> paymentData = new ArrayList<>();
        String sql = "SELECT PaymentType, COUNT(*) as TransactionCount, " +
                    "SUM(Total - Discount) as TotalRevenue, " +
                    "AVG(Total - Discount) as AvgRevenue " +
                    "FROM Sale WHERE DATE(SaleDate) = ? GROUP BY PaymentType";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, date);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> payment = new HashMap<>();
                    payment.put("paymentType", resultSet.getString("PaymentType"));
                    payment.put("transactionCount", resultSet.getInt("TransactionCount"));
                    payment.put("totalRevenue", resultSet.getDouble("TotalRevenue"));
                    payment.put("avgRevenue", resultSet.getDouble("AvgRevenue"));
                    paymentData.add(payment);
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getPaymentTypeRevenue error: " + error.getMessage() + Color.RESET);
        }
        return paymentData;
    }

    // ==================== PRODUCT PERFORMANCE ====================

    /**
     * Get top selling products by quantity for a date range
     */
    public List<Map<String, Object>> getTopSellingProducts(String startDate, String endDate, int limit) {
        List<Map<String, Object>> products = new ArrayList<>();
        String sql = "SELECT p.ProductID, p.Name, c.Name as CategoryName, " +
                    "SUM(si.Quantity) as TotalQuantity, " +
                    "SUM(si.Quantity * si.Price) as TotalRevenue, " +
                    "COUNT(DISTINCT si.SaleID) as TransactionCount " +
                    "FROM SaleItem si " +
                    "JOIN Product p ON si.ProductID = p.ProductID " +
                    "JOIN Category c ON p.CategoryID = c.CategoryID " +
                    "JOIN Sale s ON si.SaleID = s.SaleID " +
                    "WHERE DATE(s.SaleDate) BETWEEN ? AND ? " +
                    "GROUP BY p.ProductID, p.Name, c.Name " +
                    "ORDER BY TotalQuantity DESC LIMIT ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            preparedStatement.setInt(3, limit);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> product = new HashMap<>();
                    product.put("productId", resultSet.getInt("ProductID"));
                    product.put("productName", resultSet.getString("Name"));
                    product.put("categoryName", resultSet.getString("CategoryName"));
                    product.put("totalQuantity", resultSet.getInt("TotalQuantity"));
                    product.put("totalRevenue", resultSet.getDouble("TotalRevenue"));
                    product.put("transactionCount", resultSet.getInt("TransactionCount"));
                    products.add(product);
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getTopSellingProducts error: " + error.getMessage() + Color.RESET);
        }
        return products;
    }

    /**
     * Get top revenue generating products for a date range
     */
    public List<Map<String, Object>> getTopRevenueProducts(String startDate, String endDate, int limit) {
        List<Map<String, Object>> products = new ArrayList<>();
        String sql = "SELECT p.ProductID, p.Name, c.Name as CategoryName, " +
                    "SUM(si.Quantity) as TotalQuantity, " +
                    "SUM(si.Quantity * si.Price - COALESCE(si.Discount, 0)) as TotalRevenue " +
                    "FROM SaleItem si " +
                    "JOIN Product p ON si.ProductID = p.ProductID " +
                    "JOIN Category c ON p.CategoryID = c.CategoryID " +
                    "JOIN Sale s ON si.SaleID = s.SaleID " +
                    "WHERE DATE(s.SaleDate) BETWEEN ? AND ? " +
                    "GROUP BY p.ProductID, p.Name, c.Name " +
                    "ORDER BY TotalRevenue DESC LIMIT ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            preparedStatement.setInt(3, limit);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> product = new HashMap<>();
                    product.put("productId", resultSet.getInt("ProductID"));
                    product.put("productName", resultSet.getString("Name"));
                    product.put("categoryName", resultSet.getString("CategoryName"));
                    product.put("totalQuantity", resultSet.getInt("TotalQuantity"));
                    product.put("totalRevenue", resultSet.getDouble("TotalRevenue"));
                    products.add(product);
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getTopRevenueProducts error: " + error.getMessage() + Color.RESET);
        }
        return products;
    }

    // ==================== CATEGORY PERFORMANCE ====================

    /**
     * Get sales performance by category for a date range
     */
    public List<Map<String, Object>> getSalesByCategory(String startDate, String endDate) {
        List<Map<String, Object>> categories = new ArrayList<>();
        String sql = "SELECT c.CategoryID, c.Name as CategoryName, " +
                    "COUNT(DISTINCT si.SaleItemID) as ItemsSold, " +
                    "SUM(si.Quantity) as TotalQuantity, " +
                    "SUM(si.Quantity * si.Price - COALESCE(si.Discount, 0)) as TotalRevenue, " +
                    "AVG(si.Price) as AvgPrice " +
                    "FROM SaleItem si " +
                    "JOIN Product p ON si.ProductID = p.ProductID " +
                    "JOIN Category c ON p.CategoryID = c.CategoryID " +
                    "JOIN Sale s ON si.SaleID = s.SaleID " +
                    "WHERE DATE(s.SaleDate) BETWEEN ? AND ? " +
                    "GROUP BY c.CategoryID, c.Name " +
                    "ORDER BY TotalRevenue DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> category = new HashMap<>();
                    category.put("categoryId", resultSet.getInt("CategoryID"));
                    category.put("categoryName", resultSet.getString("CategoryName"));
                    category.put("itemsSold", resultSet.getInt("ItemsSold"));
                    category.put("totalQuantity", resultSet.getInt("TotalQuantity"));
                    category.put("totalRevenue", resultSet.getDouble("TotalRevenue"));
                    category.put("avgPrice", resultSet.getDouble("AvgPrice"));
                    categories.add(category);
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getSalesByCategory error: " + error.getMessage() + Color.RESET);
        }
        return categories;
    }

    // ==================== SALES TRENDS ====================

    /**
     * Get daily sales trend for a date range
     */
    public List<Map<String, Object>> getDailySalesTrend(String startDate, String endDate) {
        List<Map<String, Object>> dailySales = new ArrayList<>();
        String sql = "SELECT DATE(SaleDate) as SaleDate, " +
                    "COUNT(*) as TransactionCount, " +
                    "SUM(Total - Discount) as TotalRevenue, " +
                    "SUM(Discount) as TotalDiscounts, " +
                    "AVG(Total - Discount) as AvgTransaction " +
                    "FROM Sale " +
                    "WHERE DATE(SaleDate) BETWEEN ? AND ? " +
                    "GROUP BY DATE(SaleDate) " +
                    "ORDER BY SaleDate";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> day = new HashMap<>();
                    day.put("date", resultSet.getString("SaleDate"));
                    day.put("transactionCount", resultSet.getInt("TransactionCount"));
                    day.put("totalRevenue", resultSet.getDouble("TotalRevenue"));
                    day.put("totalDiscounts", resultSet.getDouble("TotalDiscounts"));
                    day.put("avgTransaction", resultSet.getDouble("AvgTransaction"));
                    dailySales.add(day);
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getDailySalesTrend error: " + error.getMessage() + Color.RESET);
        }
        return dailySales;
    }

    /**
     * Get hourly sales distribution for a specific date
     */
    public List<Map<String, Object>> getHourlySalesDistribution(String date) {
        List<Map<String, Object>> hourlySales = new ArrayList<>();
        String sql = "SELECT HOUR(SaleDate) as Hour, " +
                    "COUNT(*) as TransactionCount, " +
                    "SUM(Total - Discount) as TotalRevenue " +
                    "FROM Sale " +
                    "WHERE DATE(SaleDate) = ? " +
                    "GROUP BY HOUR(SaleDate) " +
                    "ORDER BY Hour";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, date);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> hour = new HashMap<>();
                    hour.put("hour", resultSet.getInt("Hour"));
                    hour.put("transactionCount", resultSet.getInt("TransactionCount"));
                    hour.put("totalRevenue", resultSet.getDouble("TotalRevenue"));
                    hourlySales.add(hour);
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getHourlySalesDistribution error: " + error.getMessage() + Color.RESET);
        }
        return hourlySales;
    }

    // ==================== USER PERFORMANCE ====================

    /**
     * Get sales performance by user/cashier for a date range
     */
    public List<Map<String, Object>> getUserPerformance(String startDate, String endDate) {
        List<Map<String, Object>> userPerformance = new ArrayList<>();
        String sql = "SELECT u.UserID, u.Username, u.Name, u.Role, " +
                    "COUNT(s.SaleID) as TransactionCount, " +
                    "SUM(s.Total - s.Discount) as TotalRevenue, " +
                    "AVG(s.Total - s.Discount) as AvgTransaction, " +
                    "SUM(s.Discount) as TotalDiscounts " +
                    "FROM User u " +
                    "LEFT JOIN Sale s ON u.UserID = s.UserID AND DATE(s.SaleDate) BETWEEN ? AND ? " +
                    "GROUP BY u.UserID, u.Username, u.Name, u.Role " +
                    "HAVING TransactionCount > 0 " +
                    "ORDER BY TotalRevenue DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("userId", resultSet.getInt("UserID"));
                    user.put("username", resultSet.getString("Username"));
                    user.put("name", resultSet.getString("Name"));
                    user.put("role", resultSet.getString("Role"));
                    user.put("transactionCount", resultSet.getInt("TransactionCount"));
                    user.put("totalRevenue", resultSet.getDouble("TotalRevenue"));
                    user.put("avgTransaction", resultSet.getDouble("AvgTransaction"));
                    user.put("totalDiscounts", resultSet.getDouble("TotalDiscounts"));
                    userPerformance.add(user);
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getUserPerformance error: " + error.getMessage() + Color.RESET);
        }
        return userPerformance;
    }

    // ==================== CUSTOMER ANALYTICS ====================

    /**
     * Get top customers by revenue for a date range
     */
    public List<Map<String, Object>> getTopCustomers(String startDate, String endDate, int limit) {
        List<Map<String, Object>> customers = new ArrayList<>();
        String sql = "SELECT c.CustomerID, c.Name, c.Email, c.Phone, " +
                    "COUNT(s.SaleID) as PurchaseCount, " +
                    "SUM(s.Total - s.Discount) as TotalSpent, " +
                    "AVG(s.Total - s.Discount) as AvgPurchase " +
                    "FROM Customer c " +
                    "JOIN Sale s ON c.CustomerID = s.CustomerID " +
                    "WHERE DATE(s.SaleDate) BETWEEN ? AND ? " +
                    "GROUP BY c.CustomerID, c.Name, c.Email, c.Phone " +
                    "ORDER BY TotalSpent DESC LIMIT ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            preparedStatement.setInt(3, limit);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> customer = new HashMap<>();
                    customer.put("customerId", resultSet.getInt("CustomerID"));
                    customer.put("name", resultSet.getString("Name"));
                    customer.put("email", resultSet.getString("Email"));
                    customer.put("phone", resultSet.getString("Phone"));
                    customer.put("purchaseCount", resultSet.getInt("PurchaseCount"));
                    customer.put("totalSpent", resultSet.getDouble("TotalSpent"));
                    customer.put("avgPurchase", resultSet.getDouble("AvgPurchase"));
                    customers.add(customer);
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getTopCustomers error: " + error.getMessage() + Color.RESET);
        }
        return customers;
    }

    // ==================== INVENTORY IMPACT ====================

    /**
     * Get products with low stock based on recent sales velocity
     */
    public List<Map<String, Object>> getLowStockProducts(int daysToAnalyze, int thresholdDays) {
        List<Map<String, Object>> lowStockProducts = new ArrayList<>();
        String sql = "SELECT p.ProductID, p.Name, c.Name as CategoryName, p.Stock, p.Price, " +
                    "COALESCE(SUM(si.Quantity), 0) as QuantitySold, " +
                    "COALESCE(SUM(si.Quantity) / ?, 0) as DailySalesRate, " +
                    "CASE " +
                    "  WHEN COALESCE(SUM(si.Quantity) / ?, 0) > 0 " +
                    "  THEN p.Stock / (SUM(si.Quantity) / ?) " +
                    "  ELSE 999 " +
                    "END as DaysUntilStockout " +
                    "FROM Product p " +
                    "JOIN Category c ON p.CategoryID = c.CategoryID " +
                    "LEFT JOIN SaleItem si ON p.ProductID = si.ProductID " +
                    "LEFT JOIN Sale s ON si.SaleID = s.SaleID AND DATE(s.DateTime) >= DATE_SUB(CURDATE(), INTERVAL ? DAY) " +
                    "GROUP BY p.ProductID, p.Name, c.Name, p.Stock, p.Price " +
                    "HAVING DaysUntilStockout < ? AND QuantitySold > 0 " +
                    "ORDER BY DaysUntilStockout";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, daysToAnalyze);
            preparedStatement.setInt(2, daysToAnalyze);
            preparedStatement.setInt(3, daysToAnalyze);
            preparedStatement.setInt(4, daysToAnalyze);
            preparedStatement.setInt(5, thresholdDays);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> product = new HashMap<>();
                    product.put("productId", resultSet.getInt("ProductID"));
                    product.put("productName", resultSet.getString("Name"));
                    product.put("categoryName", resultSet.getString("CategoryName"));
                    product.put("currentStock", resultSet.getInt("Stock"));
                    product.put("price", resultSet.getDouble("Price"));
                    product.put("quantitySold", resultSet.getInt("QuantitySold"));
                    product.put("dailySalesRate", resultSet.getDouble("DailySalesRate"));
                    product.put("daysUntilStockout", resultSet.getDouble("DaysUntilStockout"));
                    lowStockProducts.add(product);
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getLowStockProducts error: " + error.getMessage() + Color.RESET);
        }
        return lowStockProducts;
    }

    // ==================== INVENTORY ADJUSTMENTS ====================

    /**
     * Get inventory adjustment history for a date range
     */
    public List<Map<String, Object>> getInventoryAdjustments(String startDate, String endDate) {
        List<Map<String, Object>> adjustments = new ArrayList<>();
        String sql = "SELECT ia.AdjustmentID, p.Name as ProductName, c.Name as CategoryName, " +
                    "ia.DateTime, ia.QuantityChanged, ia.Reason, u.Name as AdjustedBy " +
                    "FROM InventoryAdjustment ia " +
                    "JOIN Product p ON ia.ProductID = p.ProductID " +
                    "JOIN Category c ON p.CategoryID = c.CategoryID " +
                    "JOIN User u ON ia.UserID = u.UserID " +
                    "WHERE DATE(ia.DateTime) BETWEEN ? AND ? " +
                    "ORDER BY ia.DateTime DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> adjustment = new HashMap<>();
                    adjustment.put("adjustmentId", resultSet.getInt("AdjustmentID"));
                    adjustment.put("productName", resultSet.getString("ProductName"));
                    adjustment.put("categoryName", resultSet.getString("CategoryName"));
                    adjustment.put("dateTime", resultSet.getTimestamp("DateTime"));
                    adjustment.put("quantityChanged", resultSet.getInt("QuantityChanged"));
                    adjustment.put("reason", resultSet.getString("Reason"));
                    adjustment.put("adjustedBy", resultSet.getString("AdjustedBy"));
                    adjustments.add(adjustment);
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getInventoryAdjustments error: " + error.getMessage() + Color.RESET);
        }
        return adjustments;
    }

    // ==================== DETAILED SALES DATA ====================

    /**
     * Get detailed sales data with user and customer information for a date
     */
    public List<Map<String, Object>> getDetailedSalesData(String date) {
        List<Map<String, Object>> sales = new ArrayList<>();
        String sql = "SELECT s.SaleID, s.SaleDate, " +
                    "u.Username, u.Name as CashierName, " +
                    "COALESCE(c.Name, 'Guest') as CustomerName, " +
                    "s.Total, s.Discount, (s.Total - s.Discount) as FinalAmount, " +
                    "s.PaymentType " +
                    "FROM Sale s " +
                    "JOIN User u ON s.UserID = u.UserID " +
                    "LEFT JOIN Customer c ON s.CustomerID = c.CustomerID " +
                    "WHERE DATE(s.SaleDate) = ? " +
                    "ORDER BY s.SaleDate DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, date);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> sale = new HashMap<>();
                    sale.put("saleId", resultSet.getInt("SaleID"));
                    sale.put("dateTime", resultSet.getTimestamp("SaleDate"));
                    sale.put("username", resultSet.getString("Username"));
                    sale.put("cashierName", resultSet.getString("CashierName"));
                    sale.put("customerName", resultSet.getString("CustomerName"));
                    sale.put("total", resultSet.getDouble("Total"));
                    sale.put("discount", resultSet.getDouble("Discount"));
                    sale.put("finalAmount", resultSet.getDouble("FinalAmount"));
                    sale.put("paymentType", resultSet.getString("PaymentType"));
                    sales.add(sale);
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getDetailedSalesData error: " + error.getMessage() + Color.RESET);
        }
        return sales;
    }

    /**
     * Get all detailed sales data with user and customer information (no date filter)
     */
    public List<Map<String, Object>> getAllDetailedSalesData() {
        List<Map<String, Object>> sales = new ArrayList<>();
        String sql = "SELECT s.SaleID, s.SaleDate, " +
                    "u.Username, u.Name as CashierName, " +
                    "COALESCE(c.Name, 'Guest') as CustomerName, " +
                    "s.Total, s.Discount, (s.Total - s.Discount) as FinalAmount, " +
                    "s.PaymentType " +
                    "FROM Sale s " +
                    "JOIN User u ON s.UserID = u.UserID " +
                    "LEFT JOIN Customer c ON s.CustomerID = c.CustomerID " +
                    "ORDER BY s.SaleDate DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Map<String, Object> sale = new HashMap<>();
                sale.put("saleId", resultSet.getInt("SaleID"));
                sale.put("dateTime", resultSet.getTimestamp("SaleDate"));
                sale.put("username", resultSet.getString("Username"));
                sale.put("cashierName", resultSet.getString("CashierName"));
                sale.put("customerName", resultSet.getString("CustomerName"));
                sale.put("total", resultSet.getDouble("Total"));
                sale.put("discount", resultSet.getDouble("Discount"));
                sale.put("finalAmount", resultSet.getDouble("FinalAmount"));
                sale.put("paymentType", resultSet.getString("PaymentType"));
                sales.add(sale);
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getAllDetailedSalesData error: " + error.getMessage() + Color.RESET);
        }
        return sales;
    }

    /**
     * Get sale items for a specific sale
     */
    public List<Map<String, Object>> getSaleItems(int saleId) {
        List<Map<String, Object>> items = new ArrayList<>();
        String sql = "SELECT si.SaleItemID, p.Name as ProductName, c.Name as CategoryName, " +
                    "si.Quantity, si.Price, si.Discount, " +
                    "(si.Quantity * si.Price - COALESCE(si.Discount, 0)) as Subtotal " +
                    "FROM SaleItem si " +
                    "JOIN Product p ON si.ProductID = p.ProductID " +
                    "JOIN Category c ON p.CategoryID = c.CategoryID " +
                    "WHERE si.SaleID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, saleId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("saleItemId", resultSet.getInt("SaleItemID"));
                    item.put("productName", resultSet.getString("ProductName"));
                    item.put("categoryName", resultSet.getString("CategoryName"));
                    item.put("quantity", resultSet.getInt("Quantity"));
                    item.put("price", resultSet.getDouble("Price"));
                    item.put("discount", resultSet.getDouble("Discount"));
                    item.put("subtotal", resultSet.getDouble("Subtotal"));
                    items.add(item);
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getSaleItems error: " + error.getMessage() + Color.RESET);
        }
        return items;
    }

    // ==================== SUMMARY STATISTICS ====================

    /**
     * Get overall business summary for a date range
     */
    public Map<String, Object> getBusinessSummary(String startDate, String endDate) {
        Map<String, Object> summary = new HashMap<>();
        String sql = "SELECT " +
                        "COUNT(DISTINCT s.SaleID) as TotalTransactions, " +
                        "COUNT(DISTINCT s.UserID) as ActiveUsers, " +
                        "COUNT(DISTINCT s.CustomerID) as UniqueCustomers, " +
                        "COUNT(DISTINCT si.ProductID) as ProductsSold, " +
                        "SUM(s.Total - s.Discount) as TotalRevenue, " +
                        "SUM(s.Discount) as TotalDiscounts, " +
                        "SUM(si.Quantity) as TotalItemsSold, " +
                        "AVG(s.Total - s.Discount) as AvgTransaction, " +
                        "MAX(s.Total - s.Discount) as LargestSale, " +
                        "MIN(s.Total - s.Discount) as SmallestSale " +
                    "FROM Sale s " +
                        "LEFT JOIN SaleItem si ON s.SaleID = si.SaleID " +
                    "WHERE DATE(s.DateTime) BETWEEN ? AND ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    summary.put("totalTransactions", resultSet.getInt("TotalTransactions"));
                    summary.put("activeUsers", resultSet.getInt("ActiveUsers"));
                    summary.put("uniqueCustomers", resultSet.getInt("UniqueCustomers"));
                    summary.put("productsSold", resultSet.getInt("ProductsSold"));
                    summary.put("totalRevenue", resultSet.getDouble("TotalRevenue"));
                    summary.put("totalDiscounts", resultSet.getDouble("TotalDiscounts"));
                    summary.put("totalItemsSold", resultSet.getInt("TotalItemsSold"));
                    summary.put("avgTransaction", resultSet.getDouble("AvgTransaction"));
                    summary.put("largestSale", resultSet.getDouble("LargestSale"));
                    summary.put("smallestSale", resultSet.getDouble("SmallestSale"));
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getBusinessSummary error: " + error.getMessage() + Color.RESET);
        }
        return summary;
    }

    /**
     * Get total number of transactions for a specific month
     */
    public double getPercentageTodayAndYesterday(){
        double percentage = 0.0;
        String sql = "SELECT " +
                        "(SELECT COUNT(*) FROM Sale WHERE DATE(SaleDate) = CURDATE()) as TodayCount, " +
                        "(SELECT COUNT(*) FROM Sale WHERE DATE(SaleDate) = DATE_SUB(CURDATE(), INTERVAL 1 DAY)) as YesterdayCount";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int todayCount = resultSet.getInt("TodayCount");
                int yesterdayCount = resultSet.getInt("YesterdayCount");
                if (yesterdayCount > 0) percentage = ((double)(todayCount - yesterdayCount) / yesterdayCount) * 100;
                else if (todayCount > 0)  percentage = 100.0; // If there were no transactions yesterday but some today, it's a 100% increase
            }
        } catch (SQLException error) { System.out.println(Color.RED + "❌ getPercentageTodayAndYesterday error: " + error.getMessage() + Color.RESET); }
        return percentage;
    }

    public double getPercentageOfTransactionsOfTodayAndYesterday(){
        double percentage = 0.0;
        String sql = "SELECT " +
                        "(SELECT COUNT(*) FROM Sale WHERE DATE(SaleDate) = CURDATE()) as TodayCount, " +
                        "(SELECT COUNT(*) FROM Sale WHERE DATE(SaleDate) = DATE_SUB(CURDATE(), INTERVAL 1 DAY)) as YesterdayCount";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int todayCount = resultSet.getInt("TodayCount");
                int yesterdayCount = resultSet.getInt("YesterdayCount");
                int totalCount = todayCount + yesterdayCount;
                if (totalCount > 0) percentage = ((double) todayCount / totalCount) * 100;
            }
        } catch (SQLException error) { System.out.println(Color.RED + "❌ getPercentageOfTransactionsOfTodayAndYesterday error: " + error.getMessage() + Color.RESET); }
        return percentage;
    }
}