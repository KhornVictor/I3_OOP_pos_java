package main.com.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import main.com.pos.database.DBConnection;

public class ReportDAO {

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
            System.out.println("❌ getTotalSalesForDay error: " + error.getMessage());
        }
        return 0.0;
    }

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
            System.out.println("❌ getTotalSalesForMonth error: " + error.getMessage());
        }
        return 0.0;
    }

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
            System.out.println("❌ getTotalTransactionsForDay error: " + error.getMessage());
        }
        return 0;
    }

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
            System.out.println("❌ getAverageTransactionValue error: " + error.getMessage());
        }
        return 0.0;
    }

    public Map<String, Object> getDailySalesReport(String date) {
        Map<String, Object> report = new HashMap<>();
        report.put("date", date);
        report.put("totalSales", getTotalSalesForDay(date));
        report.put("totalTransactions", getTotalTransactionsForDay(date));
        report.put("averageValue", getAverageTransactionValue(date));
        return report;
    }

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
            System.out.println("❌ getTotalDiscountsForDay error: " + error.getMessage());
        }
        return 0.0;
    }

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
            System.out.println("❌ getPaymentTypeDistribution error: " + error.getMessage());
        }
        return distribution;
    }
}
