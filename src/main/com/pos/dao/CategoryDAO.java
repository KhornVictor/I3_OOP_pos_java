package main.com.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.com.pos.database.DBConnection;
import main.com.pos.model.Category;
import main.com.pos.util.Color;

public class CategoryDAO {

    public List<Category> getAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT CategoryID, Name FROM Category ORDER BY CategoryID";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                categories.add(mapRow(resultSet));
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getAll category error: " + error.getMessage() + Color.RESET);
        }
        return categories;
    }

    public Category getById(int id) {
        String sql = "SELECT CategoryID, Name FROM Category WHERE CategoryID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) return mapRow(resultSet);
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getById category error: " + error.getMessage() + Color.RESET);
        }
        return null;
    }

    public Category getByName(String name) {
        String sql = "SELECT CategoryID, Name FROM Category WHERE Name = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) return mapRow(resultSet);
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getByName category error: " + error.getMessage() + Color.RESET);
        }
        return null;
    }

    public boolean create(Category category) {
        String sql = "INSERT INTO Category (Name) VALUES (?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, category.getName());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Category created successfully");
                return true;
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ create category error: " + error.getMessage() + Color.RESET);
        }
        return false;
    }

    public boolean update(Category category) {
        String sql = "UPDATE Category SET Name = ? WHERE CategoryID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getCategoryId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Category updated successfully");
                return true;
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ update category error: " + error.getMessage() + Color.RESET);
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Category WHERE CategoryID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Category deleted successfully");
                return true;
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ delete category error: " + error.getMessage() + Color.RESET);
        }
        return false;
    }

    public boolean exists(String name) {
        String sql = "SELECT COUNT(*) FROM Category WHERE Name = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ exists category error: " + error.getMessage() + Color.RESET);
        }
        return false;
    }

    public int getProductCount(int categoryId) {
        String sql = "SELECT COUNT(*) FROM Product WHERE CategoryID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ getProductCount category error: " + error.getMessage() + Color.RESET);
        }
        return 0;
    }

    private Category mapRow(ResultSet resultSet) throws SQLException {
        int categoryId = resultSet.getInt("CategoryID");
        String name = resultSet.getString("Name");
        return new Category(categoryId, name);
    }
}
