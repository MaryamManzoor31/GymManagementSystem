package dao;

import database.DatabaseConnection;
import model.FitnessSchedule;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// SQL operations for the existing fitness_schedules table.
public class ScheduleDao {

    public List<FitnessSchedule> getAllSchedules() throws SQLException {
        List<FitnessSchedule> schedules = new ArrayList<>();
        String sql = "SELECT schedule_id, schedule_date, schedule_time, member_name, trainer_name, activity "
                   + "FROM fitness_schedules ORDER BY schedule_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                schedules.add(new FitnessSchedule(
                    rs.getInt("schedule_id"),
                    rs.getString("member_name"),
                    rs.getString("trainer_name"),
                    rs.getString("activity"),
                    rs.getDate("schedule_date").toString(),
                    rs.getTime("schedule_time").toString()
                ));
            }
        }
        return schedules;
    }

    public int addSchedule(FitnessSchedule schedule) throws SQLException {
        String sql = "INSERT INTO fitness_schedules "
                   + "(schedule_date, schedule_time, member_name, trainer_name, activity) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(schedule.getDate()));
            ps.setTime(2, Time.valueOf(LocalTime.parse(schedule.getTime())));
            ps.setString(3, schedule.getMemberName());
            ps.setString(4, schedule.getTrainerName());
            ps.setString(5, schedule.getActivity());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("The database did not return a schedule ID.");
    }
    public FitnessSchedule getScheduleById(int id) throws SQLException {
        String sql = "SELECT schedule_id, schedule_date, schedule_time, member_name, trainer_name, activity "
                   + "FROM fitness_schedules WHERE schedule_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new FitnessSchedule(rs.getInt("schedule_id"),
                            rs.getString("member_name"), rs.getString("trainer_name"),
                            rs.getString("activity"), rs.getDate("schedule_date").toString(),
                            rs.getTime("schedule_time").toString());
                }
            }
        }
        return null;
    }

    public boolean updateSchedule(FitnessSchedule schedule) throws SQLException {
        String sql = "UPDATE fitness_schedules SET schedule_date = ?, schedule_time = ?, activity = ? "
                   + "WHERE schedule_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(schedule.getDate()));
            ps.setTime(2, Time.valueOf(LocalTime.parse(schedule.getTime())));
            ps.setString(3, schedule.getActivity());
            ps.setInt(4, schedule.getScheduleId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteSchedule(int id) throws SQLException {
        String sql = "DELETE FROM fitness_schedules WHERE schedule_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

}
