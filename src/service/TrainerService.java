
package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.Trainer;

public class TrainerService {

    private static final String URL =
        "jdbc:mysql://localhost:3306/gym_management";

    private static final String USER = "root";

    // Use the same local MySQL password as your working connection test.
    private static final String PASSWORD = "Maryam";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // CREATE: Add a trainer
    public void addTrainer(Trainer trainer) throws SQLException {

        String sql = "INSERT INTO trainers "
            + "(trainer_id, name, specialization, phone) "
            + "VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, trainer.getTrainerId());
            ps.setString(2, trainer.getName());
            setNullableString(ps, 3, trainer.getSpecialization());
            setNullableString(ps, 4, trainer.getPhone());

            ps.executeUpdate();
        }
    }

    // READ: Retrieve all trainers
    public List<Trainer> getAllTrainers() throws SQLException {

        List<Trainer> trainers = new ArrayList<>();

        String sql = "SELECT trainer_id, name, specialization, phone "
            + "FROM trainers ORDER BY trainer_id";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                trainers.add(new Trainer(
                    rs.getInt("trainer_id"),
                    rs.getString("name"),
                    rs.getString("specialization"),
                    rs.getString("phone")
                ));
            }
        }

        return trainers;
    }

    // READ: Retrieve one trainer by ID
    public Trainer getTrainerById(int id) throws SQLException {

        String sql = "SELECT trainer_id, name, specialization, phone "
            + "FROM trainers WHERE trainer_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Trainer(
                        rs.getInt("trainer_id"),
                        rs.getString("name"),
                        rs.getString("specialization"),
                        rs.getString("phone")
                    );
                }
            }
        }

        return null;
    }

    // UPDATE: Change a trainer's details
    public boolean updateTrainer(Trainer trainer) throws SQLException {

        String sql = "UPDATE trainers "
            + "SET name = ?, specialization = ?, phone = ? "
            + "WHERE trainer_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, trainer.getName());
            setNullableString(ps, 2, trainer.getSpecialization());
            setNullableString(ps, 3, trainer.getPhone());
            ps.setInt(4, trainer.getTrainerId());

            return ps.executeUpdate() > 0;
        }
    }

    // DELETE: Remove a trainer by ID
    public boolean deleteTrainer(int id) throws SQLException {

        String sql = "DELETE FROM trainers WHERE trainer_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Convert blank optional values into SQL NULL
    private void setNullableString(
            PreparedStatement ps, int index, String value)
            throws SQLException {

        if (value == null || value.trim().isEmpty()) {
            ps.setNull(index, Types.VARCHAR);
        } else {
            ps.setString(index, value.trim());
        }
    }
}
