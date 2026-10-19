package service;

import database.DatabaseConnection;
import model.Trainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

// JDBC CRUD operations for trainers. Trainer IDs are entered manually because
// the existing trainers table does not use AUTO_INCREMENT.
public class TrainerService {

    private final List<Trainer> trainers = new ArrayList<>();

    public void loadFromDatabase() throws SQLException {
        trainers.clear();
        trainers.addAll(getAllTrainers());
    }

    public Trainer addTrainer(Trainer trainer) throws SQLException {
        String sql = "INSERT INTO trainers (trainer_id, name, specialization, phone) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, trainer.getTrainerId());
            ps.setString(2, trainer.getName());
            setOptionalString(ps, 3, trainer.getSpecialization());
            setOptionalString(ps, 4, trainer.getPhone());
            ps.executeUpdate();
        }
        trainers.add(trainer);
        return trainer;
    }

    public List<Trainer> getAllTrainers() throws SQLException {
        List<Trainer> result = new ArrayList<>();
        String sql = "SELECT trainer_id, name, specialization, phone FROM trainers ORDER BY trainer_id";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new Trainer(rs.getInt("trainer_id"), rs.getString("name"),
                        rs.getString("specialization"), rs.getString("phone")));
            }
        }
        return result;
    }

    public Trainer getTrainerById(int id) throws SQLException {
        String sql = "SELECT trainer_id, name, specialization, phone FROM trainers WHERE trainer_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Trainer(rs.getInt("trainer_id"), rs.getString("name"),
                            rs.getString("specialization"), rs.getString("phone"));
                }
            }
        }
        return null;
    }

    public boolean updateTrainer(Trainer trainer) throws SQLException {
        String sql = "UPDATE trainers SET name = ?, specialization = ?, phone = ? WHERE trainer_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trainer.getName());
            setOptionalString(ps, 2, trainer.getSpecialization());
            setOptionalString(ps, 3, trainer.getPhone());
            ps.setInt(4, trainer.getTrainerId());
            boolean updated = ps.executeUpdate() > 0;
            if (updated) loadFromDatabase();
            return updated;
        }
    }

    public boolean removeTrainer(int id) throws SQLException {
        String sql = "DELETE FROM trainers WHERE trainer_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            boolean deleted = ps.executeUpdate() > 0;
            if (deleted) {
                for (int i = 0; i < trainers.size(); i++) {
                    if (trainers.get(i).getTrainerId() == id) {
                        trainers.remove(i);
                        break;
                    }
                }
            }
            return deleted;
        }
    }

    public List<Trainer> getTrainers() {
        return trainers;
    }

    private void setOptionalString(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null || value.trim().isEmpty()) {
            ps.setNull(index, Types.VARCHAR);
        } else {
            ps.setString(index, value.trim());
        }
    }
}
