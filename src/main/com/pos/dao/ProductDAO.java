package main.com.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.com.pos.database.DBConnection;
import main.com.pos.model.Product;

public class ProductDAO {

    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT ProductID, Name, CategoryID, Price, StockQuantity FROM Product";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                products.add(mapRow(resultSet));
            }
        } catch (SQLException error) {
            System.out.println("❌ getAll error: " + error.getMessage());
        }
        return products;
    }

    public Product getById(int id) {
        String sql = "SELECT ProductID, Name, CategoryID, Price, StockQuantity FROM Product WHERE ProductID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) return mapRow(resultSet);
            }
        } catch (SQLException error) {
            System.out.println("❌ getById error: " + error.getMessage());
        }
        return null;
    }

    public List<Product> getByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT ProductID, Name, CategoryID, Price, StockQuantity FROM Product WHERE CategoryID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapRow(resultSet));
                }
            }
        } catch (SQLException error) {
            System.out.println("❌ getByCategory error: " + error.getMessage());
        }
        return products;
    }

    public boolean create(Product product) {
        String sql = "INSERT INTO Product (Name, CategoryID, Price, StockQuantity) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getCategoryId());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setInt(4, product.getStockQuantity());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Product created successfully");
                return true;
            }
        } catch (SQLException error) {
            System.out.println("❌ create error: " + error.getMessage());
        }
        return false;
    }

    public boolean update(Product product) {
        String sql = "UPDATE Product SET Name = ?, CategoryID = ?, Price = ?, StockQuantity = ? WHERE ProductID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getCategoryId());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setInt(4, product.getStockQuantity());
            preparedStatement.setInt(5, product.getProductId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Product updated successfully");
                return true;
            }
        } catch (SQLException error) {
            System.out.println("❌ update error: " + error.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Product WHERE ProductID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Product deleted successfully");
                return true;
            }
        } catch (SQLException error) {
            System.out.println("❌ delete error: " + error.getMessage());
        }
        return false;
    }

    public boolean updateStock(int productId, int quantityChange) {
        String sql = "UPDATE Product SET StockQuantity = StockQuantity + ? WHERE ProductID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, quantityChange);
            preparedStatement.setInt(2, productId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException error) {
            System.out.println("❌ updateStock error: " + error.getMessage());
        }
        return false;
    }

    private Product mapRow(ResultSet resultSet) throws SQLException {
        int productId = resultSet.getInt("ProductID");
        String name = resultSet.getString("Name");
        int categoryId = resultSet.getInt("CategoryID");
        double price = resultSet.getDouble("Price");
        int stockQuantity = resultSet.getInt("StockQuantity");
        return new Product(productId, name, categoryId, price, stockQuantity);
    }
}
