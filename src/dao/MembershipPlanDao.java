package dao;

import database.DatabaseConnection;
import model.MembershipPlan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// JDBC CRUD operations for the existing membership_plans table.
public class MembershipPlanDao {

    public boolean addPlan(MembershipPlan plan) throws SQLException {
        String sql = "INSERT INTO membership_plans (plan_id, plan_name, duration, price) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, plan.getPlanId());
            ps.setString(2, plan.getPlanName());
            ps.setInt(3, plan.getDuration());
            ps.setDouble(4, plan.getPrice());
            return ps.executeUpdate() > 0;
        }
    }

    public List<MembershipPlan> getAllPlans() throws SQLException {
        List<MembershipPlan> plans = new ArrayList<>();
        String sql = "SELECT plan_id, plan_name, duration, price FROM membership_plans ORDER BY plan_id";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                plans.add(new MembershipPlan(rs.getInt("plan_id"), rs.getString("plan_name"),
                        rs.getInt("duration"), rs.getDouble("price")));
            }
        }
        return plans;
    }

    public MembershipPlan getPlanById(int id) throws SQLException {
        String sql = "SELECT plan_id, plan_name, duration, price FROM membership_plans WHERE plan_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new MembershipPlan(rs.getInt("plan_id"), rs.getString("plan_name"),
                            rs.getInt("duration"), rs.getDouble("price"));
                }
            }
        }
        return null;
    }

    public boolean updatePlan(MembershipPlan plan) throws SQLException {
        String sql = "UPDATE membership_plans SET plan_name = ?, duration = ?, price = ? WHERE plan_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, plan.getPlanName());
            ps.setInt(2, plan.getDuration());
            ps.setDouble(3, plan.getPrice());
            ps.setInt(4, plan.getPlanId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deletePlan(int id) throws SQLException {
        String sql = "DELETE FROM membership_plans WHERE plan_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
