package main.com.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.com.pos.database.DBConnection;
import main.com.pos.model.Users;

public class UserDAO {

	public Users getById(int id) {
		String sql = "SELECT id, username, password, role FROM users WHERE id = ?";
		try (
            Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
			preparedStatement.setInt(1, id);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.next()) return mapRow(resultSet);
			}
		} catch (SQLException e) { System.out.println("❌ getById error: " + e.getMessage()); }
		return null;
	}

	public Users getByUsername(String username) {
		String sql = "SELECT id, username, password, role FROM users WHERE username = ?";
		try (
            Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
			preparedStatement.setString(1, username);
			try (ResultSet resultSet = preparedStatement.executeQuery()) { if (resultSet.next()) return mapRow(resultSet); }
		} catch (SQLException e) { System.out.println("❌ getByUsername error: " + e.getMessage()); }
		return null;
	}

	public Users authenticate(String username, String password) {
		String sql = "SELECT id, username, password, role FROM users WHERE username = ? AND password = ?";
		try (
            Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) return mapRow(resultSet);
			}
		} catch (SQLException e) {
			System.out.println("❌ authenticate error: " + e.getMessage());
		}
		return null;
	}

	public List<Users> getAll() {
		String sql = "SELECT id, username, password, role FROM users ORDER BY id";
		List<Users> result = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(sql);
			 ResultSet resultSet = preparedStatement.executeQuery()) {
			while (resultSet.next()) result.add(mapRow(resultSet));
		} catch (SQLException e) {
			System.out.println("❌ getAll error: " + e.getMessage());
		}
		return result;
	}

	public boolean insert(Users user) {
		String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.setString(3, user.getRole());
			return preparedStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("❌ insert error: " + e.getMessage());
			return false;
		}
	}

	public boolean update(Users user) {
		String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.setString(3, user.getRole());
			preparedStatement.setInt(4, user.getId());
			return preparedStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("❌ update error: " + e.getMessage());
			return false;
		}
	}

	public boolean delete(int id) {
		String sql = "DELETE FROM users WHERE id = ?";
		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			return preparedStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("❌ delete error: " + e.getMessage());
			return false;
		}
	}

	private Users mapRow(ResultSet rs) throws SQLException {
		Users users = new Users();
		users.setId(rs.getInt("id"));
		users.setUsername(rs.getString("username"));
		users.setPassword(rs.getString("password"));
		users.setRole(rs.getString("role"));
		return users;
	}
}