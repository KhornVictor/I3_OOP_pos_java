package main.com.pos.thread;

import java.sql.SQLException;
import main.com.pos.database.DBConnection;
import main.com.pos.database.DBInitializer;
import main.com.pos.database.DBTest;

public class DatabaseConnectionTask implements Runnable {

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            var connection = DBConnection.getConnection();
            if (connection != null && !connection.isClosed()) { System.out.println("✅ DB Test: Connection is active!"); }
            DBTest.runTests(connection);
            DBInitializer.init();
        } catch (SQLException e) { System.err.println("❌ DB Test Failed!"); }
        long endTime = System.currentTimeMillis();
        System.out.println("Database connection task completed in " + (endTime - startTime) + " ms");
    }
}
