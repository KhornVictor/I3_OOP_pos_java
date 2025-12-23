package main.com.pos.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTest {
    public static void runTests(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM test");
            System.out.println("✅ DB Test Passed\n");
            System.out.println("Name\t|\tAge");
            System.out.println("-----------------------");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("name") + "\t|\t" + resultSet.getInt("age"));
            }
            System.out.println("-----------------------\n");
        } catch (SQLException e) {
            System.err.println("❌ DB Test Failed: " + e.getMessage());
        }

    }
}
