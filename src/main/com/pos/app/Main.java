package main.com.pos.app;
import java.sql.SQLException;
import main.com.pos.database.DBConnection;
import main.com.pos.view.LoginFrame;

public class Main {
    public static void main(String[] args) {
        try {
            var connection = DBConnection.getConnection();
            if (connection != null && !connection.isClosed()) { System.out.println("✅ DB Test: Connection is active!"); }
        } catch (SQLException e) { System.err.println("❌ DB Test Failed!"); }
        
        java.awt.EventQueue.invokeLater(() -> {new LoginFrame().setVisible(true); });
    }
}
