package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DatabaseConnection {

    public static final String DATABASE = "gym_management";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "Maryam";
    public static final String URL = "jdbc:mysql://localhost:3306/gym_management";

    private DatabaseConnection() {
        // Utility class
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void displaySavedMembers() throws SQLException {
        String sql = "SELECT member_id, name, age, phone, membership_type "
                + "FROM members ORDER BY member_id";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Database connected successfully.");
            System.out.println("Saved gym members:");

            boolean foundMember = false;
            while (resultSet.next()) {
                foundMember = true;
                System.out.println(
                        "ID: " + resultSet.getInt("member_id")
                                + ", Name: " + resultSet.getString("name")
                                + ", Age: " + resultSet.getInt("age")
                                + ", Phone: " + resultSet.getString("phone")
                                + ", Membership: " + resultSet.getString("membership_type")
                );
            }

            if (!foundMember) {
                System.out.println("No members are saved yet.");
            }
        }
    }
}
