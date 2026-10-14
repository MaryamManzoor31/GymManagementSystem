package dao;

import database.DatabaseConnection;
import model.Member;
import model.PremiumMember;
import model.RegularMember;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDao {

    public void addMember(Member member) throws SQLException {
        String sql = "INSERT INTO members (member_id, name, age, phone, membership_type, plan_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, member.getMemberId());
            statement.setString(2, member.getName());
            statement.setInt(3, member.getAge());
            statement.setString(4, member.getPhone());
            statement.setString(5, member instanceof PremiumMember ? "Premium" : "Regular");
            statement.setNull(6, java.sql.Types.INTEGER);
            statement.executeUpdate();
        }
    }

    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT member_id, name, age, phone, membership_type FROM members ORDER BY member_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("member_id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String membershipType = resultSet.getString("membership_type");

                Member member = "Premium".equalsIgnoreCase(membershipType)
                        ? new PremiumMember(id, name, age, phone)
                        : new RegularMember(id, name, age, phone);
                members.add(member);
            }
        }
        return members;
    }

    public boolean exists(int memberId) throws SQLException {
        String sql = "SELECT 1 FROM members WHERE member_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean deleteMember(int memberId) throws SQLException {
        String deleteSchedules = "DELETE FROM fitness_schedules WHERE member_id = ?";
        String deleteMember = "DELETE FROM members WHERE member_id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement statement = connection.prepareStatement(deleteSchedules)) {
                    statement.setInt(1, memberId);
                    statement.executeUpdate();
                }

                int deleted;
                try (PreparedStatement statement = connection.prepareStatement(deleteMember)) {
                    statement.setInt(1, memberId);
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
