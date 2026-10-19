package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Opens connections to the existing gym_management database.
public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/gym_management";
    private static final String USER = "root";
    private static final String PASSWORD = "Maryam";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Simple connection check used by the startup class.
    public static boolean testConnection() throws SQLException {
        try (Connection con = getConnection()) {
            return con.isValid(5);
        }
    }

    // Prints existing member records to the console for a simple JDBC demonstration.
    public static void displaySavedMembers() throws SQLException {
        String sql = "SELECT member_id, name, age, phone FROM members ORDER BY member_id";
        try (Connection con = getConnection();
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            System.out.println("Saved members in MySQL:");
            while (rs.next()) {
                System.out.println(rs.getInt("member_id") + " "
                        + rs.getString("name") + " "
                        + rs.getInt("age") + " "
                        + rs.getString("phone"));
            }
        }
    }
}
