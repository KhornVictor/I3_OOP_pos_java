package main.com.pos.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {
    @SuppressWarnings("CallToPrintStackTrace")
    public static void init() {
        try {
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();

            String userTable = """
                CREATE TABLE IF NOT EXISTS User (
                    UserID INT AUTO_INCREMENT PRIMARY KEY,
                    Username VARCHAR(50) NOT NULL UNIQUE,
                    Password VARCHAR(255) NOT NULL,
                    Role VARCHAR(20) NOT NULL,
                    Name VARCHAR(100),
                    Email VARCHAR(100)
                ) ENGINE=InnoDB;
            """;
            statement.execute(userTable);
            System.out.println("🍀 Database initialized successfully.");
        } catch (SQLException e) {
            System.out.println("❌ Error initializing database: " + e.getMessage());
        }
    }
}