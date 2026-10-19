package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

// Checks that the application's existing tables are available.
// CREATE TABLE IF NOT EXISTS does not replace or clear existing tables/data.
public class DatabaseSetup {

    public static void initializeDatabase() throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             Statement statement = con.createStatement()) {

            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS members ("
                + "member_id INT NOT NULL PRIMARY KEY,"
                + "name VARCHAR(100) NOT NULL,"
                + "age INT NOT NULL,"
                + "phone VARCHAR(10) NOT NULL)"
            );

            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS trainers ("
                + "trainer_id INT NOT NULL PRIMARY KEY,"
                + "name VARCHAR(100) NOT NULL,"
                + "specialization VARCHAR(100) NULL,"
                + "phone VARCHAR(10) NULL)"
            );

            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS membership_plans ("
                + "plan_id INT NOT NULL PRIMARY KEY,"
                + "plan_name VARCHAR(100) NOT NULL,"
                + "duration INT NOT NULL,"
                + "price DECIMAL(10,2) NOT NULL)"
            );

            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS fitness_schedules ("
                + "schedule_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                + "schedule_date DATE NOT NULL,"
                + "schedule_time TIME NOT NULL,"
                + "member_name VARCHAR(100) NOT NULL,"
                + "trainer_name VARCHAR(100) NOT NULL,"
                + "activity VARCHAR(100) NOT NULL)"
            );
        }
    }
}
