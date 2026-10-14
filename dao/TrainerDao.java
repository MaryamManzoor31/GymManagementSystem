package dao;

import database.DatabaseConnection;
import model.Trainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TrainerDao {

    public int addTrainer(Trainer trainer) throws SQLException {
        String sql = "INSERT INTO trainers (name, specialization, phone) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, trainer.getName());
            statement.setString(2, trainer.getSpecialization());
            statement.setString(3, trainer.getPhone());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("Adding the trainer did not return a trainer ID.");
                }
                return generatedKeys.getInt(1);
            }
        }
    }

    public List<Trainer> getAllTrainers() throws SQLException {
        List<Trainer> trainers = new ArrayList<>();
        String sql = "SELECT trainer_id, name, specialization, phone FROM trainers ORDER BY trainer_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                trainers.add(new Trainer(
                        resultSet.getInt("trainer_id"),
                        resultSet.getString("name"),
                        resultSet.getString("specialization"),
                        resultSet.getString("phone")
                ));
            }
        }
        return trainers;
    }

    public boolean deleteTrainer(int trainerId) throws SQLException {
        String deleteSchedules = "DELETE FROM fitness_schedules WHERE trainer_id = ?";
        String deleteTrainer = "DELETE FROM trainers WHERE trainer_id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement statement = connection.prepareStatement(deleteSchedules)) {
                    statement.setInt(1, trainerId);
                    statement.executeUpdate();
                }

                int deleted;
                try (PreparedStatement statement = connection.prepareStatement(deleteTrainer)) {
                    statement.setInt(1, trainerId);
                    deleted = statement.executeUpdate();
                }

                if (deleted == 0) {
                    connection.rollback();
                    return false;
                }

                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }
}
