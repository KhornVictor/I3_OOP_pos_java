package main.com.pos;
import java.sql.SQLException;
import main.com.pos.database.DBConnection;
import main.com.pos.database.DBInitializer;
import main.com.pos.database.DBTest;
import main.com.pos.view.login.LoginFrame;

public class Main {
    public static void main(String[] args) {
        try {
            var connection = DBConnection.getConnection();
            if (connection != null && !connection.isClosed()) { System.out.println("✅ DB Test: Connection is active!"); }
            DBTest.runTests(connection);
            DBInitializer.init();
        } catch (SQLException e) { System.err.println("❌ DB Test Failed!"); }
        
        java.awt.EventQueue.invokeLater(() -> {new LoginFrame().setVisible(true); });
    }
}
