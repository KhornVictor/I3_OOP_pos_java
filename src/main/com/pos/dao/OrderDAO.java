package main.com.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import main.com.pos.database.DBConnection;
import main.com.pos.model.DateTime;
import main.com.pos.model.Sale;
import main.com.pos.model.SaleItem;

public class OrderDAO {

    public int createOrder(Sale sale) {
        String sql = "INSERT INTO Sale (UserID, SaleDate, SaleTime, CustomerID, Total, Discount, PaymentType) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, sale.getUserId());
            preparedStatement.setString(2, sale.getSaleDate().getDate().toString());
            preparedStatement.setString(3, sale.getSaleDate().getTime().toString());
            preparedStatement.setInt(4, sale.getCustomerId());
            preparedStatement.setDouble(5, sale.getTotal());
            preparedStatement.setDouble(6, sale.getDiscount());
            preparedStatement.setString(7, sale.getPaymentType());
            
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int saleId = generatedKeys.getInt(1);
                    System.out.println("✅ Order created successfully with ID: " + saleId);
                    return saleId;
                }
            }
        } catch (SQLException error) {
            System.out.println("❌ createOrder error: " + error.getMessage());
        }
        return -1;
    }

    public boolean addOrderItem(SaleItem item) {
        String sql = "INSERT INTO SaleItem (SaleID, ProductID, Quantity, Price, Discount) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, item.getSaleId());
            preparedStatement.setInt(2, item.getProductId());
            preparedStatement.setInt(3, item.getQuantity());
            preparedStatement.setDouble(4, item.getPrice());
            preparedStatement.setDouble(5, item.getDiscount());
            
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException error) {
            System.out.println("❌ addOrderItem error: " + error.getMessage());
        }
        return false;
    }

    public Sale getOrderById(int saleId) {
        String sql = "SELECT SaleID, UserID, SaleDate, SaleTime, CustomerID, Total, Discount, PaymentType FROM Sale WHERE SaleID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, saleId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapSaleRow(resultSet);
                }
            }
        } catch (SQLException error) {
            System.out.println("❌ getOrderById error: " + error.getMessage());
        }
        return null;
    }

    public List<SaleItem> getOrderItems(int saleId) {
        List<SaleItem> items = new ArrayList<>();
        String sql = "SELECT SaleItemID, SaleID, ProductID, Quantity, Price, Discount FROM SaleItem WHERE SaleID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, saleId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(mapItemRow(resultSet));
                }
            }
        } catch (SQLException error) {
            System.out.println("❌ getOrderItems error: " + error.getMessage());
        }
        return items;
    }

    public List<Sale> getAllOrders() {
        List<Sale> orders = new ArrayList<>();
        String sql = "SELECT SaleID, UserID, SaleDate, SaleTime, CustomerID, Total, Discount, PaymentType FROM Sale";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                orders.add(mapSaleRow(resultSet));
            }
        } catch (SQLException error) {
            System.out.println("❌ getAllOrders error: " + error.getMessage());
        }
        return orders;
    }

    public List<Sale> getOrdersByUser(int userId) {
        List<Sale> orders = new ArrayList<>();
        String sql = "SELECT SaleID, UserID, SaleDate, SaleTime, CustomerID, Total, Discount, PaymentType FROM Sale WHERE UserID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(mapSaleRow(resultSet));
                }
            }
        } catch (SQLException error) {
            System.out.println("❌ getOrdersByUser error: " + error.getMessage());
        }
        return orders;
    }

    private Sale mapSaleRow(ResultSet resultSet) throws SQLException {
        int saleId = resultSet.getInt("SaleID");
        int userId = resultSet.getInt("UserID");
        LocalDate date = LocalDate.parse(resultSet.getString("SaleDate"));
        LocalTime time = LocalTime.parse(resultSet.getString("SaleTime"));
        DateTime dateTime = new DateTime(date, time);
        int customerId = resultSet.getInt("CustomerID");
        double total = resultSet.getDouble("Total");
        double discount = resultSet.getDouble("Discount");
        String paymentType = resultSet.getString("PaymentType");
        
        Sale sale = new Sale(saleId, userId, dateTime, customerId, total, discount, paymentType);
        return sale;
    }

    private SaleItem mapItemRow(ResultSet resultSet) throws SQLException {
        int saleItemId = resultSet.getInt("SaleItemID");
        int saleId = resultSet.getInt("SaleID");
        int productId = resultSet.getInt("ProductID");
        int quantity = resultSet.getInt("Quantity");
        double price = resultSet.getDouble("Price");
        double discount = resultSet.getDouble("Discount");
        return new SaleItem(saleItemId, saleId, productId, quantity, price, discount);
    }
}
