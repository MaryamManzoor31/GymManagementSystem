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
}
