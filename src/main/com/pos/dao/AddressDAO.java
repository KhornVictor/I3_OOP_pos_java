package main.com.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import main.com.pos.database.DBConnection;
import main.com.pos.model.Address;
import main.com.pos.util.Color;

public class AddressDAO {

    public Address getById(int id) {
        String sql = "SELECT AddressID, Street, City, State, ZipCode, Country FROM Address WHERE AddressID = ?";
        try (Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) { if (resultSet.next()) return mapRow(resultSet); }
        } catch (SQLException error) { System.out.println(Color.RED + "❌ getById address error: " + error.getMessage() + Color.RESET); }
        return null;
    }

    public int create(Address address) {
        // Let the database assign AddressID (AUTO_INCREMENT)
        String sql = "INSERT INTO Address (Street, City, State, ZipCode, Country) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, address.getStreet());
            preparedStatement.setString(2, address.getCity());
            preparedStatement.setString(3, address.getState());
            preparedStatement.setString(4, address.getZipCode());
            preparedStatement.setString(5, address.getCountry());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) return -1;

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    System.out.println("✅ Address created successfully (ID: " + newId + ")");
                    return newId;
                }
            }
        } catch (SQLException error) { System.out.println(Color.RED + "❌ create address error: " + error.getMessage() + Color.RESET); }
        return -1;
    }

    public boolean update(Address address) {
        String sql = "UPDATE Address SET Street = ?, City = ?, State = ?, ZipCode = ?, Country = ? WHERE AddressID = ?";
        try (Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, address.getStreet());
            preparedStatement.setString(2, address.getCity());
            preparedStatement.setString(3, address.getState());
            preparedStatement.setString(4, address.getZipCode());
            preparedStatement.setString(5, address.getCountry());
            preparedStatement.setInt(6, address.getAddressId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Address updated successfully");
                return true;
            }
        } catch (SQLException error) {
            System.out.println(Color.RED + "❌ update address error: " + error.getMessage() + Color.RESET);
        }
        return false;
    }

    private Address mapRow(ResultSet resultSet) throws SQLException {
        return new Address(
            resultSet.getInt("AddressID"),
            resultSet.getString("Street"),
            resultSet.getString("City"),
            resultSet.getString("State"),
            resultSet.getString("ZipCode"),
            resultSet.getString("Country")
        );
    }
}
