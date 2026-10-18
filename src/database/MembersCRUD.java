
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MembersCRUD {

    private static final String URL =
        "jdbc:mysql://localhost:3306/gym_management";

    private static final String USER = "root";
    private static final String PASSWORD = "Maryam";

    private static final Scanner sc = new Scanner(System.in);

    // Establish a connection to MySQL
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n===== GYM MEMBER MANAGEMENT =====");
            System.out.println("1. Add Member");
            System.out.println("2. View All Members");
            System.out.println("3. Search Member by ID");
            System.out.println("4. Update Member");
            System.out.println("5. Delete Member");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = readInt("Choice");

            switch (choice) {
                case 1:
                    addMember();
                    break;
                case 2:
                    viewMembers();
                    break;
                case 3:
                    searchMember();
                    break;
                case 4:
                    updateMember();
                    break;
                case 5:
                    deleteMember();
                    break;
                case 6:
                    System.out.println("Exiting Member Management.");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Safely read an integer from the console
    private static int readInt(String label) {
        while (true) {
            System.out.print(label + ": ");
            String value = sc.nextLine().trim();

            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    // CREATE: Add a new member
    private static void addMember() {

        int id = readInt("Member ID");

        System.out.print("Enter name: ");
        String name = sc.nextLine().trim();

        int age = readInt("Age");

        System.out.print("Enter phone (up to 10 digits): ");
        String phone = sc.nextLine().trim();

        if (name.isEmpty() || name.length() > 100) {
            System.out.println("Name must contain 1-100 characters.");
            return;
        }

        if (age <= 0) {
            System.out.println("Age must be positive.");
            return;
        }

        if (!phone.matches("\\d{1,10}")) {
            System.out.println("Phone must contain 1-10 digits.");
            return;
        }

        String sql =
            "INSERT INTO members (member_id, name, age, phone) "
            + "VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setInt(3, age);
            ps.setString(4, phone);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Member added successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Could not add member: " + e.getMessage());
        }
    }

    // READ: Display all members
    private static void viewMembers() {

        String sql =
            "SELECT member_id, name, age, phone "
            + "FROM members ORDER BY member_id";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            System.out.println("\n===== ALL MEMBERS =====");
            System.out.printf("%-12s %-25s %-8s %-12s%n",
                "ID", "Name", "Age", "Phone");

            while (rs.next()) {
                found = true;

                System.out.printf("%-12d %-25s %-8d %-12s%n",
                    rs.getInt("member_id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("phone"));
            }

            if (!found) {
                System.out.println("No members found.");
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve members: "
                + e.getMessage());
        }
    }

    // READ: Search for one member by ID
    private static void searchMember() {

        int id = readInt("Enter member ID to search");

        String sql =
            "SELECT member_id, name, age, phone "
            + "FROM members WHERE member_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\nMember found:");
                    System.out.println("ID: "
                        + rs.getInt("member_id"));
                    System.out.println("Name: "
                        + rs.getString("name"));
                    System.out.println("Age: "
                        + rs.getInt("age"));
                    System.out.println("Phone: "
                        + rs.getString("phone"));
                } else {
                    System.out.println("No member found with ID " + id);
                }
            }

        } catch (SQLException e) {
            System.out.println("Search failed: " + e.getMessage());
        }
    }

    // UPDATE: Modify an existing member
    private static void updateMember() {

        int id = readInt("Enter member ID to update");

        System.out.print("Enter new name: ");
        String name = sc.nextLine().trim();

        int age = readInt("Enter new age");

        System.out.print("Enter new phone (up to 10 digits): ");
        String phone = sc.nextLine().trim();

        if (name.isEmpty() || name.length() > 100
                || age <= 0 || !phone.matches("\\d{1,10}")) {
            System.out.println("Invalid member details.");
            return;
        }

        String sql =
            "UPDATE members SET name = ?, age = ?, phone = ? "
            + "WHERE member_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, phone);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Member updated successfully!");
            } else {
                System.out.println("No member found with ID " + id);
            }

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    // DELETE: Remove a member after confirmation
    private static void deleteMember() {

        int id = readInt("Enter member ID to delete");

        // Verify the member exists before asking for confirmation
        String checkSql =
            "SELECT name FROM members WHERE member_id = ?";

        try (Connection con = getConnection();
             PreparedStatement check = con.prepareStatement(checkSql)) {

            check.setInt(1, id);

            try (ResultSet rs = check.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("No member found with ID " + id);
                    return;
                }

                System.out.println("Member: " + rs.getString("name"));
            }

            System.out.print("Confirm deletion? (y/n): ");
            String confirm = sc.nextLine().trim();

            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println("Deletion cancelled.");
                return;
            }

            String sql = "DELETE FROM members WHERE member_id = ?";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);

                if (ps.executeUpdate() > 0) {
                    System.out.println("Member deleted successfully!");
                } else {
                    System.out.println("Member was not deleted.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Deletion failed: " + e.getMessage());
        }
    }
}
