
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestConnection {

    public static void main(String[] args) {

        String url =
            "jdbc:mysql://localhost:3306/gym_management";
        String username = "root";
        String password = "Maryam";

        String[] tables = {
            "members",
            "trainers",
            "membership_plans",
            "fitness_schedules"
        };

        try (Connection con =
                 DriverManager.getConnection(url, username, password)) {

            System.out.println("Database connected successfully!");

            for (String table : tables) {
                String sql = "SELECT COUNT(*) FROM " + table;

                try (Statement stmt = con.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {

                    if (rs.next()) {
                        System.out.println(
                            table + ": " + rs.getInt(1) + " row(s)"
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Database operation failed!");
            e.printStackTrace();
        }
    }
}
