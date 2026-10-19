package dao;

import database.DatabaseConnection;
import model.Member;
import model.RegularMember;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// SQL operations for the members table.
public class MemberDao {

    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT member_id, name, age, phone FROM members ORDER BY member_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // The existing table has no column for regular/premium status.
                members.add(new RegularMember(
                    rs.getInt("member_id"), rs.getString("name"),
                    rs.getInt("age"), rs.getString("phone")
                ));
            }
        }
        return members;
    }

    public boolean exists(int id) throws SQLException {
        String sql = "SELECT member_id FROM members WHERE member_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void addMember(Member member) throws SQLException {
        String sql = "INSERT INTO members (member_id, name, age, phone) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, member.getMemberId());
            ps.setString(2, member.getName());
            ps.setInt(3, member.getAge());
            ps.setString(4, member.getPhone());
            ps.executeUpdate();
        }
    }

    public boolean updateMember(Member member) throws SQLException {
        String sql = "UPDATE members SET name = ?, age = ?, phone = ? WHERE member_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, member.getName());
            ps.setInt(2, member.getAge());
            ps.setString(3, member.getPhone());
            ps.setInt(4, member.getMemberId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteMember(int id) throws SQLException {
        String sql = "DELETE FROM members WHERE member_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
