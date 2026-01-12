package main.com.pos.database.seed;

import java.sql.*;
import java.time.LocalDateTime;
import main.com.pos.database.DBConnection;
import main.com.pos.model.Address;
import main.com.pos.model.Customer;
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
        // Insert addresses first
        String addressSql = """
            INSERT INTO Address (Street, City, State, ZipCode, Country)
            SELECT ?, ?, ?, ?, ?
            WHERE NOT EXISTS (
                SELECT 1 FROM Address WHERE Street = ? AND City = ?
            )
        """;

        Address[] addresses = {
            new Address(1, "123 Main St", "Phnom Penh", "Phnom Penh", "12000", "Cambodia"),
            new Address(2, "456 Second St", "Siem Reap", "Siem Reap", "17000", "Cambodia"),
            new Address(3, "789 Third St", "Battambang", "Battambang", "02000", "Cambodia"),
        };

        try (PreparedStatement preparedStatement = connection.prepareStatement(addressSql)) {
            for (Address address : addresses) {
                preparedStatement.setString(1, address.getStreet());
                preparedStatement.setString(2, address.getCity());
                preparedStatement.setString(3, address.getState());
                preparedStatement.setString(4, address.getZipCode());
                preparedStatement.setString(5, address.getCountry());
                preparedStatement.setString(6, address.getStreet());
                preparedStatement.setString(7, address.getCity());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }

        // Then insert users with address references
        String userSql = """
            INSERT INTO User (Username, Password, Role, Name, Email, AddressID, Image)
            SELECT ?, ?, ?, ?, ?, 
                (SELECT AddressID FROM Address WHERE Street = ?), ?
            WHERE NOT EXISTS (
                SELECT 1 FROM User WHERE Username = ?
            )
        """;

        Object[][] users = {
            {"victor", "123456", "admin", "Khorn Victor", "victor@gmail.com", "123 Main St", "images/avatar/victor.png"},
            {"samnang", "123456", "admin", "Hour Samnang", "samnang@gmail.com", "456 Second St", "images/avatar/samnang.png"},
            {"sonita", "123456", "cashier", "Nhean Sonita", "sonita@gmail.com", "789 Third St", "images/avatar/sonita.png"},
        };

        try (PreparedStatement preparedStatement = connection.prepareStatement(userSql)) {
            for (Object[] user : users) {
                preparedStatement.setString(1, (String) user[0]); // Username
                preparedStatement.setString(2, (String) user[1]); // Password
                preparedStatement.setString(3, (String) user[2]); // Role
                preparedStatement.setString(4, (String) user[3]); // Name
                preparedStatement.setString(5, (String) user[4]); // Email
                preparedStatement.setString(6, (String) user[5]); // AddressID lookup
                preparedStatement.setString(7, (String) user[6]); // Image
                preparedStatement.setString(8, (String) user[0]); // WHERE NOT EXISTS Username
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    // ================= CUSTOMER =================
    private static void seedCustomers(Connection connection) throws SQLException {
        String sql = """
            INSERT INTO Customer (Name, Email, Phone)
            SELECT ?, ?, ?
            WHERE NOT EXISTS (
                SELECT 1 FROM Customer WHERE Name = ?
            )
        """;

        Customer[] customers = {
            new Customer(1, "Walk-in Customer", null, null),
            new Customer(2, "John Doe", "john@gmail.com", "012345678")
        };

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Customer customer : customers) {
                preparedStatement.setString(1, customer.getName());
                preparedStatement.setString(2, customer.getEmail());
                preparedStatement.setString(3, customer.getPhone());
                preparedStatement.setString(4, customer.getName()); 
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
            for (String category : categories) {
                preparedStatement.setString(1, category);
                preparedStatement.setString(2, category);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    // ================= PRODUCT =================
    private static void seedProducts(Connection connection) throws SQLException {

        String sql = """
            INSERT INTO Product (Name, CategoryID, Price, StockQuantity, Image)
            SELECT ?, (SELECT CategoryID FROM Category WHERE Name = ?), ?, ?, ?
            WHERE NOT EXISTS (
                SELECT 1 FROM Product WHERE Name = ?
            )
        """;

        Object[][] products = {
            {"Coca Cola", "Beverages", 1.50, 100, "images/product/coca_cola.png"},
            {"Chips", "Snacks", 2.00, 150, "images/product/chips.png"},
            {"Smartphone", "Electronics", 300.00, 50, "images/product/smartphone.png"}
        };

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Object[] product : products) {
                preparedStatement.setString(1, (String) product[0]); // Name
                preparedStatement.setString(2, (String) product[1]); // Category name for lookup
                preparedStatement.setDouble(3, (double) product[2]); // Price
                preparedStatement.setInt(4, (int) product[3]); // StockQuantity
                preparedStatement.setString(5, (String) product[4]); // Image
                preparedStatement.setString(6, (String) product[0]); // Name for WHERE NOT EXISTS
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }


    // ================= SALE =================
    private static int seedSales(Connection connection) throws SQLException {
        String sql = """
            INSERT INTO Sale (SaleDate, UserID, CustomerID, Total, Discount, PaymentType)
            VALUES (?, ?, ?, ?, ?, ?) 
        """;

        Integer userId = getIdByValue(connection, "SELECT UserID FROM User WHERE Username = ?", "sonita");
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
            (ProductID, AdjustmentDate, QuantityChange, Reason, UserID)
            VALUES (?, ?, ?, ?, ?)
        """;

        Integer productId = getIdByValue(connection, "SELECT ProductID FROM Product WHERE Name = ?", "Coca Cola");
        Integer userId = getIdByValue(connection, "SELECT UserID FROM User WHERE Username = ?", "sonita");
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
