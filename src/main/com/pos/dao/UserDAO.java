package main.com.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.com.pos.database.DBConnection;
import main.com.pos.model.User;
import main.com.pos.util.Color;

public class UserDAO {

	public User getById(int id) {
		String sql = "SELECT UserID, Username, Password, Role, Name, Email, Image FROM User WHERE UserID = ?";
		try (
            Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
			preparedStatement.setInt(1, id);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.next()) return mapRow(resultSet);
			}
		} catch (SQLException error) { System.out.println("❌ getById error: " + error.getMessage()); }
		return null;
	}

	public User getByUsername(String username) {
		String sql = "SELECT UserID, Username, Password, Role, Name, Email, Image FROM User WHERE Username = ?";
		try (
            Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
			preparedStatement.setString(1, username);
			try (ResultSet resultSet = preparedStatement.executeQuery()) { if (resultSet.next()) return mapRow(resultSet); }
		} catch (SQLException error) { System.out.println("❌ getByUsername error: " + error.getMessage()); }
		return null;
	}

	public boolean create(User user) {
		String sql = "INSERT INTO User (Username, Password, Role, Name, Email, AddressId, Image) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.setString(3, user.getRole());
			preparedStatement.setString(4, user.getName());
			preparedStatement.setString(5, user.getEmail());
			preparedStatement.setInt(6, user.getAddressId());
			preparedStatement.setString(7, user.getImage());
			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("✅ User created successfully");
				return true;
			}
		} catch (SQLException error) {
			System.out.println("❌ create user error: " + error.getMessage());
		}
		return false;
	}

	public User authenticate(User user) {
		String sql = "SELECT UserID, Username, Password, Role, Name, Email, Image FROM User WHERE Username = ? AND Password = ?";
		try (
            Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) return mapRow(resultSet);
			}
		} catch (SQLException error) { System.out.println("❌ authenticate error: " + error.getMessage()); }
		return null;
	}

	public void delete(int id) {
		String sql = "DELETE FROM User WHERE UserID = ?";
		try (Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows > 0) System.out.println(Color.GREEN +  "✅ User deleted successfully" + Color.RESET);
			else System.out.println("⚠️ No user found with ID: " + id);
		} catch (SQLException error) { System.out.println("❌ delete error: " + error.getMessage()); }
	}

	public List<User> getAll() {
		String sql = "SELECT UserID, Username, Password, Role, Name, Email, Image FROM User ORDER BY UserID";
		List<User> result = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(sql);
			 ResultSet resultSet = preparedStatement.executeQuery()) {
			while (resultSet.next()) result.add(mapRow(resultSet));
		} catch (SQLException error) { System.out.println("❌ getAll error: " + error.getMessage()); }
		return result;
	}

	private User mapRow(ResultSet resultSet) throws SQLException {
		User user = new User();
		user.setUserId(resultSet.getInt("UserID"));
		user.setUsername(resultSet.getString("Username"));
		user.setPassword(resultSet.getString("Password"));
		user.setRole(resultSet.getString("Role"));
		user.setName(resultSet.getString("Name"));
		user.setEmail(resultSet.getString("Email"));
		user.setImage(resultSet.getString("Image"));
		return user;
	}

    public static void displayUser(User selectedUser) {
        throw new UnsupportedOperationException("Unimplemented method 'displayUser'");
    }

    public static User getEmptyUser() {
        throw new UnsupportedOperationException("Unimplemented method 'getEmptyUser'");
    }
}