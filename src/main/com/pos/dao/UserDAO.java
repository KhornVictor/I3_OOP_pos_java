package main.com.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import main.com.pos.database.DBConnection;
import main.com.pos.model.Address;
import main.com.pos.model.User;
import main.com.pos.util.Color;

public class UserDAO {

	public User getById(int id) {
		String sql = "SELECT UserID, Username, Password, Role, Name, Email, AddressID, Image FROM User WHERE UserID = ?";
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
		String sql = "SELECT UserID, Username, Password, Role, Name, Email, AddressID, Image FROM User WHERE Username = ?";
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
		String sql = "INSERT INTO User (Username, Password, Role, Name, Email, AddressID, Image) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
		String sql = "SELECT UserID, Username, Password, Role, Name, Email, AddressID, Image FROM User WHERE Username = ? AND Password = ?";
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
		String sql = "SELECT UserID, Username, Password, Role, Name, Email, AddressID, Image FROM User ORDER BY UserID";
		List<User> result = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(sql);
			 ResultSet resultSet = preparedStatement.executeQuery()) {
			while (resultSet.next()) result.add(mapRow(resultSet));
		} catch (SQLException error) { System.out.println("❌ getAll error: " + error.getMessage()); }
		return result;
	}

	public int createUserWithAddress(User user, Address address) throws SQLException {
		String insertAddress = """
			INSERT INTO Address (Street, City, State, ZipCode, Country)
			VALUES (?, ?, ?, ?, ?)
		""";

		String insertUser = """
			INSERT INTO User (Username, Password, Role, Name, Email, AddressID, Image)
			VALUES (?, ?, ?, ?, ?, ?, ?)
		""";

		try (Connection connection = DBConnection.getConnection()) {
			connection.setAutoCommit(false);
			int addressId = -1;

			try {
				// Insert address if provided; otherwise keep it null
				if (address != null) {
					try (PreparedStatement stmtAddr = connection.prepareStatement(insertAddress, Statement.RETURN_GENERATED_KEYS)) {
						stmtAddr.setString(1, address.getStreet());
						stmtAddr.setString(2, address.getCity());
						stmtAddr.setString(3, address.getState());
						stmtAddr.setString(4, address.getZipCode());
						stmtAddr.setString(5, address.getCountry());
						stmtAddr.executeUpdate();

						try (ResultSet resultSet = stmtAddr.getGeneratedKeys()) {
							if (!resultSet.next()) throw new SQLException("No address ID returned");
							addressId = resultSet.getInt(1);
						}
					}
				}

				try (PreparedStatement statementUser = connection.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
					statementUser.setString(1, user.getUsername());
					statementUser.setString(2, user.getPassword()); // already hashed
					statementUser.setString(3, user.getRole());
					statementUser.setString(4, user.getName());
					statementUser.setString(5, user.getEmail());
					if (addressId > 0) statementUser.setInt(6, addressId); else statementUser.setNull(6, Types.INTEGER);
					statementUser.setString(7, user.getImage());

					statementUser.executeUpdate();

					try (ResultSet resultSet = statementUser.getGeneratedKeys()) {
						if (resultSet.next()) {
							int userId = resultSet.getInt(1);
							connection.commit();
							return userId;
						}
					}
				}

				connection.rollback();
				throw new SQLException("Failed to create user");
			} catch (SQLException error) {
				connection.rollback();
				throw error;
			} finally { connection.setAutoCommit(true); }
		}
	}

	public boolean updateUser(User updatedUser, Address updatedAddress) throws SQLException {
		if (updatedUser.getUserId() <= 0) throw new IllegalArgumentException("User ID is required for update");
		
		try (Connection connection = DBConnection.getConnection()) {
			connection.setAutoCommit(false);
			
			try {
				Integer newAddressId = updatedUser.getAddressId();
				if (updatedAddress != null && newAddressId > 0) {
					String updateAddr = "UPDATE Address SET Street = ?, City = ?, State = ?, ZipCode = ?, Country = ? WHERE AddressID = ?";
					try (PreparedStatement ps = connection.prepareStatement(updateAddr)) {
						ps.setString(1, updatedAddress.getStreet());
						ps.setString(2, updatedAddress.getCity());
						ps.setString(3, updatedAddress.getState());
						ps.setString(4, updatedAddress.getZipCode());
						ps.setString(5, updatedAddress.getCountry());
						ps.setInt(6, newAddressId);
						ps.executeUpdate();
					}
				} else if (updatedAddress != null && newAddressId <= 0) {
					// Create new address
					String insertAddr = "INSERT INTO Address (Street, City, State, ZipCode, Country) VALUES (?, ?, ?, ?, ?)";
					try (PreparedStatement preparedStatement = connection.prepareStatement(insertAddr, Statement.RETURN_GENERATED_KEYS)) {
						preparedStatement.setString(1, updatedAddress.getStreet());
						preparedStatement.setString(2, updatedAddress.getCity());
						preparedStatement.setString(3, updatedAddress.getState());
						preparedStatement.setString(4, updatedAddress.getZipCode());
						preparedStatement.setString(5, updatedAddress.getCountry());
						preparedStatement.executeUpdate();
						
						try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
							if (resultSet.next()) newAddressId = resultSet.getInt(1);
						}
					}
				}
				
				// Update user record
				String updateUserSql = "UPDATE User SET Username = ?, Password = ?, Role = ?, Name = ?, Email = ?, AddressID = ?, Image = ? WHERE UserID = ?";
				try (PreparedStatement preparedStatement = connection.prepareStatement(updateUserSql)) {
					preparedStatement.setString(1, updatedUser.getUsername());
					preparedStatement.setString(2, updatedUser.getPassword());
					preparedStatement.setString(3, updatedUser.getRole());
					preparedStatement.setString(4, updatedUser.getName());
					preparedStatement.setString(5, updatedUser.getEmail());
					if (newAddressId > 0) preparedStatement.setInt(6, newAddressId);
					else preparedStatement.setNull(6, Types.INTEGER);
					preparedStatement.setString(7, updatedUser.getImage());
					preparedStatement.setInt(8, updatedUser.getUserId());
					
					int rowsAffected = preparedStatement.executeUpdate();
					connection.commit();
					
					if (rowsAffected > 0) {
						System.out.println(Color.GREEN + "✅ User updated successfully" + Color.RESET);
						return true;
					}
				}
				
				connection.rollback();
				return false;
			} catch (SQLException e) {
				connection.rollback();
				System.out.println(Color.RED + "❌ update user error: " + e.getMessage() + Color.RESET);
				throw e;
			} finally {
				connection.setAutoCommit(true);
			}
		}
	}

	public boolean deleteWithAddressCleanup(int userId) {
		Connection connection = null;
		try {
			connection = DBConnection.getConnection();
			connection.setAutoCommit(false);

			// 1. Get the AddressID of this user
			Integer addressId = null;
			try (PreparedStatement ps = connection.prepareStatement(
					"SELECT AddressID FROM User WHERE UserID = ?")) {
				ps.setInt(1, userId);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						addressId = rs.getInt("AddressID");
						if (rs.wasNull()) addressId = null;
					}
				}
			}

			if (addressId == null) {
				// No address → just delete user
				try (PreparedStatement ps = connection.prepareStatement(
						"DELETE FROM User WHERE UserID = ?")) {
					ps.setInt(1, userId);
					int rows = ps.executeUpdate();
					if (rows == 0) {
						connection.rollback();
						return false;
					}
				}
				connection.commit();
				return true;
			}

			// 2. Delete the user
			int userRows = 0;
			try (PreparedStatement ps = connection.prepareStatement(
					"DELETE FROM User WHERE UserID = ?")) {
				ps.setInt(1, userId);
				userRows = ps.executeUpdate();
			}

			if (userRows == 0) {
				connection.rollback();
				return false;
			}

			// 4. Delete address only if no other users reference it
			try (PreparedStatement ps = connection.prepareStatement("DELETE FROM Address WHERE AddressID = ?")) {
				ps.setInt(1, userId);
				ps.executeUpdate();
			}

			connection.commit();
			System.out.println(Color.GREEN + "✅ User ID " + userId + " and unused address deleted" + Color.RESET);
			return true;

		} catch (SQLException e) {
			if (connection != null) {
				try { connection.rollback(); } catch (SQLException ignored) {}
			}
			System.err.println("❌ Delete failed: " + e.getMessage());
			return false;
		} finally {
			if (connection != null) {
				try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
				try { connection.close(); } catch (SQLException ignored) {}
			}
		}
	}

	private User mapRow(ResultSet resultSet) throws SQLException {
		User user = new User();
		user.setUserId(resultSet.getInt("UserID"));
		user.setUsername(resultSet.getString("Username"));
		user.setPassword(resultSet.getString("Password"));
		user.setRole(resultSet.getString("Role"));
		user.setName(resultSet.getString("Name"));
		user.setEmail(resultSet.getString("Email"));
		user.setAddressId(resultSet.getInt("AddressID"));
		user.setImage(resultSet.getString("Image"));
		return user;
	}

    public static void displayUser(User selectedUser) { throw new UnsupportedOperationException("Unimplemented method 'displayUser'"); }
    public static User getEmptyUser() { throw new UnsupportedOperationException("Unimplemented method 'getEmptyUser'"); }
}