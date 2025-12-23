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
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    role VARCHAR(20) NOT NULL
                );
            """;
            statement.execute(userTable);
            System.out.println("🍀 Database initialized successfully.");
        } catch (SQLException e) {
            System.out.println("❌ Error initializing database: " + e.getMessage());
        }
    }
}