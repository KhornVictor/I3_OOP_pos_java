package main.com.pos.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import main.com.pos.config.DBConfig;
import main.com.pos.util.Color;

public class DBConnection {
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                    DBConfig.getDbUrl(),
                    DBConfig.getDbUser(),
                    DBConfig.getDbPassword()
                );
            }
        } catch (SQLException e) { System.out.println(Color.RED + "❌ Failed to establish database connection." + Color.RESET);}
        return connection;
    }
}