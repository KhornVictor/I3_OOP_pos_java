package main.com.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.com.pos.database.DBConnection;
import main.com.pos.model.Product;
import main.com.pos.util.Color;

public class ProductDAO {

    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT ProductID, Name, CategoryID, Price, StockQuantity, Image FROM Product ORDER BY ProductID";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) { products.add(mapRow(resultSet)); }
        } catch (SQLException error) { System.out.println(Color.RED + "❌ getAll product error: " + error.getMessage() + Color.RESET); }
        return products;
    }

    public int countProducts() {
        String sql = "SELECT COUNT(*) AS total FROM Product";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ countProducts error: " + error.getMessage() + Color.RESET);
        }
        return 0;
    }

    public Product getById(int id) {
        String sql = "SELECT ProductID, Name, CategoryID, Price, StockQuantity, Image FROM Product WHERE ProductID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) return mapRow(resultSet);
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getById product error: " + error.getMessage() + Color.RESET);
        }
        return null;
    }

    public List<Product> getByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT ProductID, Name, CategoryID, Price, StockQuantity, Image FROM Product WHERE CategoryID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapRow(resultSet));
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getByCategory product error: " + error.getMessage() + Color.RESET);
        }
        return products;
    }

    public boolean create(Product product) {
        String sql = "INSERT INTO Product (Name, CategoryID, Price, StockQuantity, Image) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getCategoryId());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setInt(4, product.getStockQuantity());
            preparedStatement.setString(5, product.getImage());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Product created successfully");
                return true;
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ create product error: " + error.getMessage() + Color.RESET);
        }
        return false;
    }

    public boolean update(Product product) {
        String sql = "UPDATE Product SET Name = ?, CategoryID = ?, Price = ?, StockQuantity = ?, Image = ? WHERE ProductID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getCategoryId());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setInt(4, product.getStockQuantity());
            preparedStatement.setString(5, product.getImage());
            preparedStatement.setInt(6, product.getProductId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Product updated successfully");
                return true;
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ update product error: " + error.getMessage() + Color.RESET);
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
            System.out.println(Color.RED + "❌ delete product error: " + error.getMessage() + Color.RESET);
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
        } catch (SQLException error) { System.out.println(Color.RED + "❌ updateStock product error: " + error.getMessage() + Color.RESET); }
        return false;
    }

    private Product mapRow(ResultSet resultSet) throws SQLException {
        int productId = resultSet.getInt("ProductID");
        String name = resultSet.getString("Name");
        int categoryId = resultSet.getInt("CategoryID");
        double price = resultSet.getDouble("Price");
        int stockQuantity = resultSet.getInt("StockQuantity");
        String image = resultSet.getString("Image");
        return new Product(productId, name, categoryId, price, stockQuantity, image);
    }

    public static int getCategoryIdByName(String categoryName) {
        String sql = "SELECT CategoryID FROM Category WHERE Name = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, categoryName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("CategoryID");
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getCategoryIdByName error: " + error.getMessage() + Color.RESET);
        }
        return -1;
    }
}