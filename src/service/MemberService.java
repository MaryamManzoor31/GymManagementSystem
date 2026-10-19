package service;

import dao.MemberDao;
import model.Member;
import exception.DuplicateMemberIdException;
import exception.InvalidAgeException;
import exception.InvalidPhoneException;
import exception.MemberNotFoundException;

import java.sql.SQLException;
import java.util.ArrayList;

// Handles member validation and keeps the GUI's member list in sync with MySQL.
public class MemberService {

    private final ArrayList<Member> members = new ArrayList<>();
    private final MemberDao memberDao = new MemberDao();

    public void loadFromDatabase() throws SQLException {
        members.clear();
        members.addAll(memberDao.getAllMembers());
        Member.setMemberCount(members.size());
    }

    public void addMember(Member member)
            throws DuplicateMemberIdException, InvalidAgeException,
                   InvalidPhoneException, SQLException {
        validateMember(member.getName(), member.getAge(), member.getPhone());

        if (memberDao.exists(member.getMemberId())) {
            throw new DuplicateMemberIdException(
                "A member with ID " + member.getMemberId() + " already exists.");
        }

        memberDao.addMember(member);
        members.add(member);
        Member.setMemberCount(members.size());
    }

    public void updateMember(int id, String name, int age, String phone)
            throws MemberNotFoundException, InvalidAgeException,
                   InvalidPhoneException, SQLException {
        Member existing = searchMember(id);
        validateMember(name, age, phone);

        existing.setName(name.trim());
        existing.setAge(age);
        existing.setPhone(phone.trim());
        if (!memberDao.updateMember(existing)) {
            throw new MemberNotFoundException("Member with ID " + id + " not found.");
        }
    }

    private void validateMember(String name, int age, String phone)
            throws InvalidAgeException, InvalidPhoneException {
        if (name == null || name.trim().isEmpty() || name.trim().length() > 100) {
            throw new IllegalArgumentException("Name must contain 1-100 characters.");
        }
        validateAge(age);
        validatePhone(phone);
    }

    public void validateAge(int age) throws InvalidAgeException {
        if (age < Member.MIN_AGE || age > Member.MAX_AGE) {
            throw new InvalidAgeException("Age must be between 16 and 70.");
        }
    }

    public void validatePhone(String phone) throws InvalidPhoneException {
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new InvalidPhoneException("Phone number must contain exactly 10 digits.");
        }
    }

    public Member searchMember(int id) throws MemberNotFoundException {
        for (Member member : members) {
            if (member.getMemberId() == id) return member;
        }
        throw new MemberNotFoundException("Member with ID " + id + " not found.");
    }

    public void removeMember(int id) throws MemberNotFoundException, SQLException {
        Member member = searchMember(id);
        if (!memberDao.deleteMember(id)) {
            throw new MemberNotFoundException("Member with ID " + id + " not found.");
        }
        members.remove(member);
        Member.setMemberCount(members.size());
    }

    public ArrayList<Member> getMembers() {
        return members;
    }
}
