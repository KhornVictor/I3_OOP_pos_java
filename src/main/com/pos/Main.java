package main.com.pos;
import java.awt.EventQueue;
import java.sql.SQLException;
import main.com.pos.components.layout.Layout;
import main.com.pos.database.DBConnection;
import main.com.pos.database.DBInitializer;
import main.com.pos.database.DBTest;
import main.com.pos.model.User;

public class Main {
    public static void main(String[] args) {
        User user = new User("Guest");
        try {
            var connection = DBConnection.getConnection();
            if (connection != null && !connection.isClosed()) { System.out.println("✅ DB Test: Connection is active!"); }
            DBTest.runTests(connection);
            DBInitializer.init();
        } catch (SQLException e) { System.err.println("❌ DB Test Failed!"); }
        EventQueue.invokeLater(() -> {new Layout(user).setVisible(true); });
    }
}