package main.com.pos.thread;

import java.sql.SQLException;
import main.com.pos.database.DBConnection;
import main.com.pos.database.DBInitializer;
import main.com.pos.database.DBTest;
import main.com.pos.util.Color;
public class DatabaseConnectionTask implements Runnable {

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        Boolean test = false;
        try {
            var connection = DBConnection.getConnection();
            if (connection != null && !connection.isClosed()) { System.out.println(Color.GREEN + "📦  DB Test: Connection is active!" + Color.RESET); }
            DBInitializer.init();
            if (test) DBTest.runTests(connection);
        } catch (SQLException e) { System.err.println(Color.RED + "❌ DB Test Failed!" + Color.RESET); }
        long endTime = System.currentTimeMillis();
        System.out.println(Color.GREEN + "⚙️  Database connection task completed in " + (endTime - startTime) + " ms" + Color.RESET);
    }
}
