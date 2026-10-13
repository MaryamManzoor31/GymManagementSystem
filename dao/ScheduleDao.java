package dao;

import database.DatabaseConnection;
import model.FitnessSchedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.Statement;

public class ScheduleDao {

    public int addSchedule(FitnessSchedule schedule) throws SQLException {
        String sql = "INSERT INTO fitness_schedules (member_id, trainer_id, activity, schedule_date, schedule_time) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, schedule.getMemberId());
            statement.setInt(2, schedule.getTrainerId());
            statement.setString(3, schedule.getActivity());
            statement.setDate(4, Date.valueOf(LocalDate.parse(schedule.getDate())));
            statement.setTime(5, Time.valueOf(LocalTime.parse(schedule.getTime())));
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("Adding the schedule did not return a schedule ID.");
                }
                return generatedKeys.getInt(1);
            }
        }
    }

    public List<FitnessSchedule> getAllSchedules() throws SQLException {
        List<FitnessSchedule> schedules = new ArrayList<>();
        String sql = "SELECT s.schedule_id, s.member_id, s.trainer_id, m.name AS member_name, "
                + "t.name AS trainer_name, s.activity, s.schedule_date, s.schedule_time "
                + "FROM fitness_schedules s "
                + "JOIN members m ON s.member_id = m.member_id "
                + "JOIN trainers t ON s.trainer_id = t.trainer_id "
                + "ORDER BY s.schedule_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                schedules.add(new FitnessSchedule(
                        resultSet.getInt("schedule_id"),
                        resultSet.getInt("member_id"),
                        resultSet.getInt("trainer_id"),
                        resultSet.getString("member_name"),
                        resultSet.getString("trainer_name"),
                        resultSet.getString("activity"),
                        resultSet.getDate("schedule_date").toString(),
                        resultSet.getTime("schedule_time").toString()
                ));
            }
        }
        return schedules;
    }
}
