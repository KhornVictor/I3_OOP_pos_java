package main.com.pos.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import main.com.pos.config.DBConfig;

public class DBConnection {
    private static Connection connection;

    @SuppressWarnings("CallToPrintStackTrace")
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                    DBConfig.getDbUrl(),
                    DBConfig.getDbUser(),
                    DBConfig.getDbPassword()
                );
            }
            System.out.println("✅ Database connection established.");
        } catch (SQLException e) {
            System.out.println("❌ Failed to establish database connection.");
        }
        return connection;
    }
}