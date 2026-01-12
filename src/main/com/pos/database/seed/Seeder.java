package main.com.pos.database.seed;

import java.sql.*;
import java.time.LocalDateTime;
import main.com.pos.database.DBConnection;
import main.com.pos.util.Color;

public class Seeder {

    public void initialize() {

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            seedUsers(connection);
            seedCustomers(connection);
            seedCategories(connection);
            seedProducts(connection);
            int saleId = seedSales(connection);
            seedSaleItems(connection, saleId);
            seedReceipts(connection, saleId);
            seedInventoryAdjustments(connection);
            connection.commit();
            System.out.println(Color.GREEN + "🌿 Database seeded successfully!" + Color.RESET);
        } catch (SQLException e) { System.out.println(Color.RED + "❌ Database seeding failed: " + e.getMessage() + Color.RESET);}
    }

    // ================= USER =================
    private static void seedUsers(Connection connection) throws SQLException {
        String sql = """
            INSERT INTO User (Username, Password, Role, Name, Email, Image)
            SELECT ?, ?, ?, ?, ?, ?
            WHERE NOT EXISTS (
                SELECT 1 FROM User WHERE Username = ?
            )
        """;

        String[][] users = {
            {"admin", "admin123", "admin", "System Admin", "admin@pos.com", "admin.png"},
            {"cashier1", "cash123", "cashier", "Alice Smith", "alice@pos.com", "alice.png"},
            {"cashier2", "cash123", "cashier", "Bob Johnson", "bob@pos.com", "bob.png"}
        };

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (String[] user : users) {
                preparedStatement.setString(1, user[0]); // Username
                preparedStatement.setString(2, user[1]); // Password
                preparedStatement.setString(3, user[2]); // Role
                preparedStatement.setString(4, user[3]); // Name
                preparedStatement.setString(5, user[4]); // Email
                preparedStatement.setString(6, user[5]); // Image
                preparedStatement.setString(7, user[0]); // WHERE NOT EXISTS Username
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    // ================= CUSTOMER =================
    private static void seedCustomers(Connection connection) throws SQLException {
        String sql = """
            INSERT INTO Customer (Name, Email, Phone, Address)
            SELECT ?, ?, ?, ?
            WHERE NOT EXISTS (
                SELECT 1 FROM Customer WHERE Name = ?
            )
        """;

        String[][] customers = {
            {"Walk-in Customer", null, null, null},
            {"John Doe", "john@gmail.com", "012345678", "Phnom Penh"}
        };

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (String[] customer : customers) {
                preparedStatement.setString(1, customer[0]);
                preparedStatement.setString(2, customer[1]);
                preparedStatement.setString(3, customer[2]);
                preparedStatement.setString(4, customer[3]);
                preparedStatement.setString(5, customer[0]); // check Name
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    // ================= CATEGORY =================
    private static void seedCategories(Connection connection) throws SQLException {
        String sql = """
            INSERT INTO Category (Name)
            SELECT ?
            WHERE NOT EXISTS (
                SELECT 1 FROM Category WHERE Name = ?
            )
        """;

        String[] categories = {"Beverages", "Snacks", "Electronics"};

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (String name : categories) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, name);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    // ================= PRODUCT =================
    private static void seedProducts(Connection connection) throws SQLException {

        String sql = """
            INSERT INTO Product (Name, CategoryID, Price, Stock)
            SELECT ?, (SELECT CategoryID FROM Category WHERE Name = ?), ?, ?
            WHERE NOT EXISTS (
                SELECT 1 FROM Product WHERE Name = ?
            )
        """;

        Object[][] products = {
            {"Coca Cola", "Beverages", 1.50, 100},
            {"Chips", "Snacks", 2.00, 80},
            {"USB Cable", "Electronics", 3.50, 50}
        };

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Object[] p : products) {
                preparedStatement.setString(1, (String) p[0]);
                preparedStatement.setString(2, (String) p[1]);
                preparedStatement.setBigDecimal(3, new java.math.BigDecimal(p[2].toString()));
                preparedStatement.setInt(4, (int) p[3]);
                preparedStatement.setString(5, (String) p[0]);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }


    // ================= SALE =================
    private static int seedSales(Connection connection) throws SQLException {
        String sql = """
            INSERT INTO Sale (DateTime, UserID, CustomerID, Total, Discount, PaymentType)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        Integer userId = getIdByValue(connection, "SELECT UserID FROM User WHERE Username = ?", "cashier1");
        Integer customerId = getIdByValue(connection, "SELECT CustomerID FROM Customer WHERE Name = ?", "Walk-in Customer");
        if (userId == null || customerId == null) throw new SQLException("Missing user or customer for sale seeding");

        Integer existingSaleId = getSaleId(connection, userId, customerId);
        if (existingSaleId != null) return existingSaleId;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, customerId);
            preparedStatement.setBigDecimal(4, new java.math.BigDecimal("10.00"));
            preparedStatement.setBigDecimal(5, new java.math.BigDecimal("0.00"));
            preparedStatement.setString(6, "CASH");
            preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        throw new SQLException("Failed to create or retrieve Sale record");
    }

    // ================= SALE ITEM =================
    private static void seedSaleItems(Connection connection, int saleId) throws SQLException {
        String sql = """
            INSERT INTO SaleItem (SaleID, ProductID, Quantity, Price, Discount)
            SELECT ?, ?, ?, ?, ?
            WHERE NOT EXISTS (
                SELECT 1 FROM SaleItem WHERE SaleID = ? AND ProductID = ?
            )
        """;

        Object[][] items = {
            {"Coca Cola", 2, 1.50, 0.00},
            {"Chips", 1, 2.00, 0.00}
        };

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Object[] i : items) {
                Integer productId = getIdByValue(connection, "SELECT ProductID FROM Product WHERE Name = ?", (String) i[0]);
                if (productId == null) throw new SQLException("Missing product for sale item: " + i[0]);

                preparedStatement.setInt(1, saleId);
                preparedStatement.setInt(2, productId);
                preparedStatement.setInt(3, (int) i[1]);
                preparedStatement.setBigDecimal(4, new java.math.BigDecimal(i[2].toString()));
                preparedStatement.setBigDecimal(5, new java.math.BigDecimal(i[3].toString()));
                preparedStatement.setInt(6, saleId);
                preparedStatement.setInt(7, productId);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    // ================= RECEIPT =================
    private static void seedReceipts(Connection connection, int saleId) throws SQLException {
        String sql = """
            INSERT INTO Receipt (SaleID, StoreName, Item, Totals, DateTime)
            SELECT ?, ?, ?, ?, ?
            WHERE NOT EXISTS (
                SELECT 1 FROM Receipt WHERE SaleID = ?
            )
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, saleId);
            preparedStatement.setString(2, "My POS Store");
            preparedStatement.setString(3, "Coca Cola x2, Chips x1");
            preparedStatement.setBigDecimal(4, new java.math.BigDecimal("5.00"));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setInt(6, saleId);
            preparedStatement.executeUpdate();
        }
    }

    // ================= INVENTORY ADJUSTMENT =================
    private static void seedInventoryAdjustments(Connection connection) throws SQLException {
        String sql = """
            INSERT INTO InventoryAdjustment
            (ProductID, DateTime, QuantityChanged, Reason, UserID)
            VALUES (?, ?, ?, ?, ?)
        """;

        Integer productId = getIdByValue(connection, "SELECT ProductID FROM Product WHERE Name = ?", "Coca Cola");
        Integer userId = getIdByValue(connection, "SELECT UserID FROM User WHERE Username = ?", "cashier1");
        if (productId == null || userId == null) throw new SQLException("Missing product or user for inventory adjustment");

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, productId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setInt(3, -2);
            preparedStatement.setString(4, "Sold items");
            preparedStatement.setInt(5, userId);
            preparedStatement.executeUpdate();
        }
    }

    private static Integer getIdByValue(Connection connection, String sql, String value) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, value);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return null;
    }

    private static Integer getSaleId(Connection connection, int userId, int customerId) throws SQLException {
        String sql = "SELECT SaleID FROM Sale WHERE UserID = ? AND CustomerID = ? ORDER BY SaleID LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, customerId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return null;
    }
}
