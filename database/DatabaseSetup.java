package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseSetup {

    private DatabaseSetup() {
        // Utility class
    }

    public static void initializeDatabase() throws SQLException {
        String createDatabase = "CREATE DATABASE IF NOT EXISTS " + DatabaseConfig.DATABASE;

        String createMembershipPlans = "CREATE TABLE IF NOT EXISTS membership_plans ("
                + "plan_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "plan_name VARCHAR(100) NOT NULL, "
                + "duration INT NOT NULL, "
                + "price DOUBLE NOT NULL)";

        String createTrainers = "CREATE TABLE IF NOT EXISTS trainers ("
                + "trainer_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(100) NOT NULL, "
                + "specialization VARCHAR(100) NOT NULL, "
                + "phone VARCHAR(20) NOT NULL)";

        String createMembers = "CREATE TABLE IF NOT EXISTS members ("
                + "member_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(100) NOT NULL, "
                + "age INT NOT NULL, "
                + "phone VARCHAR(20) NOT NULL, "
                + "membership_type VARCHAR(50) NOT NULL, "
                + "plan_id INT, "
                + "FOREIGN KEY (plan_id) REFERENCES membership_plans(plan_id))";

        String createSchedules = "CREATE TABLE IF NOT EXISTS fitness_schedules ("
                + "schedule_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "member_id INT NOT NULL, "
                + "trainer_id INT NOT NULL, "
                + "activity VARCHAR(100) NOT NULL, "
                + "schedule_date DATE NOT NULL, "
                + "schedule_time TIME NOT NULL, "
                + "FOREIGN KEY (member_id) REFERENCES members(member_id), "
                + "FOREIGN KEY (trainer_id) REFERENCES trainers(trainer_id))";

        try (Connection serverConnection = DriverManager.getConnection(
                DatabaseConfig.SERVER_URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             Statement serverStatement = serverConnection.createStatement()) {

            serverStatement.executeUpdate(createDatabase);
        }

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(createMembershipPlans);
            statement.executeUpdate(createTrainers);
            statement.executeUpdate(createMembers);
            statement.executeUpdate(createSchedules);

            System.out.println("Database initialized successfully.");
        }
    }
}
