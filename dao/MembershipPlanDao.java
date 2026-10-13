package dao;

import database.DatabaseConnection;
import model.MembershipPlan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MembershipPlanDao {

    public void addPlan(MembershipPlan plan) throws SQLException {
        String sql = "INSERT INTO membership_plans (plan_name, duration, price) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, plan.getPlanName());
            statement.setInt(2, plan.getDuration());
            statement.setDouble(3, plan.getPrice());
            statement.executeUpdate();
        }
    }

    public List<MembershipPlan> getAllPlans() throws SQLException {
        List<MembershipPlan> plans = new ArrayList<>();
        String sql = "SELECT plan_id, plan_name, duration, price FROM membership_plans ORDER BY plan_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                plans.add(new MembershipPlan(
                        resultSet.getInt("plan_id"),
                        resultSet.getString("plan_name"),
                        resultSet.getInt("duration"),
                        resultSet.getDouble("price")
                ));
            }
        }
        return plans;
    }
}
