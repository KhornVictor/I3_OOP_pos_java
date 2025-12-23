package main.com.pos.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static Connection connection = null;

    static {
        try {Class.forName("com.mysql.cj.jdbc.Driver");}
        catch (ClassNotFoundException e) {System.err.println("❌ JDBC Driver not found!");}
    }

    public static Connection getConnection() {
        try { if (connection != null && connection.isClosed()) connection = null;} 
        catch (SQLException ignore) {connection = null;}
        
        if (connection == null) {
            try {
                Properties properties = new Properties();
                InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("main/com/pos/resources/db.properties");
                if (input == null) input = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("main/com/pos/resources/db.properties");

                if (input == null) { throw new RuntimeException("❗ db.properties not found on classpath at main/com/pos/resources/db.properties"); }

                try (InputStream in = input) { properties.load(in); }

                String url = firstNonEmpty( 
                    System.getenv("DB_URL"),
                    properties.getProperty("db.url")
                );
                String user = firstNonEmpty(
                    System.getenv("DB_USER"),
                    properties.getProperty("db.user")
                );
                String password = firstNonEmpty(
                    System.getenv("DB_PASSWORD"),
                    properties.getProperty("db.password")
                );

                if (isEmpty(url) || isEmpty(user)) throw new IllegalStateException("Database configuration invalid: missing db.url or db.user");

                connection = DriverManager.getConnection(url, user, password);
                System.out.println("✅ Connected to database: " + url);
            } catch (IOException | RuntimeException | SQLException e) {
                System.err.println("❌ Failed to connect to database:");
                throw new RuntimeException("Database connection failed", e);
            }
        }
        return connection;
    }

    private static boolean isEmpty(String s) { return s == null || s.isBlank(); }
    private static String firstNonEmpty(String a, String b) { return !isEmpty(a) ? a : b; }

    public static void closeQuietly() {
        if (connection != null) {
            try { connection.close(); }
            catch (SQLException ignored) { } 
            finally { connection = null; }
        }
    }
}