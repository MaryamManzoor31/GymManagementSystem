package service;

import dao.MemberDao;
import model.Member;
import exception.DuplicateMemberIdException;
import exception.InvalidAgeException;
import exception.InvalidPhoneException;
import exception.MemberNotFoundException;

import java.util.ArrayList;
import java.sql.SQLException;

// Module M3: Member management service.
// This class handles member registration, validation, lookup, and removal using an ArrayList.
public class MemberService {

    private ArrayList<Member> members;
    private final MemberDao memberDao;

    public MemberService() {
        members = new ArrayList<>();
        memberDao = new MemberDao();
    }

    public void loadFromDatabase() throws SQLException {
        members.clear();
        members.addAll(memberDao.getAllMembers());
        Member.setMemberCount(members.size());
    }

    // Adds a new member after validating age and phone number rules.
    public void addMember(Member member)
            throws DuplicateMemberIdException, InvalidAgeException, InvalidPhoneException, SQLException {

        for (Member existingMember : members) {
            if (existingMember.getMemberId() == member.getMemberId()) {
                throw new DuplicateMemberIdException(
                        "A member with ID " + member.getMemberId() + " already exists."
                );
            }
        }

        validateAge(member.getAge());
        validatePhone(member.getPhone());

        if (memberDao.exists(member.getMemberId())) {
            throw new DuplicateMemberIdException(
                    "A member with ID " + member.getMemberId() + " already exists."
            );
        }

        memberDao.addMember(member);
        members.add(member);
        Member.incrementMemberCount();

        System.out.println("Member added successfully.");
    }

    // Validates member age against the allowed gym age range.
    public void validateAge(int age)
            throws InvalidAgeException {

        if (age < Member.MIN_AGE || age > Member.MAX_AGE) {

            throw new InvalidAgeException(
                    "Age must be between 16 and 70."
            );
        }
    }

    // Validates phone number format by checking length and numeric-only digits.
    public void validatePhone(String phone)
            throws InvalidPhoneException {

        if (phone.length() != 10) {

            throw new InvalidPhoneException(
                    "Phone number must contain 10 digits."
            );
        }

        for (int i = 0; i < phone.length(); i++) {

            if (!Character.isDigit(phone.charAt(i))) {

                throw new InvalidPhoneException(
                        "Phone number must contain only digits."
                );
            }
        }
    }

    // Displays all stored members by iterating the ArrayList.
    public void displayMembers() {

        if (members.isEmpty()) {
            System.out.println("No members available.");
            return;
        }

        for (Member member : members) {

            member.displayDetails();

            System.out.println("----------------------");
        }
    }

    // Searches for a member by ID and throws an exception if not found.
    public Member searchMember(int id)
            throws MemberNotFoundException {

        for (Member member : members) {

            if (member.getMemberId() == id) {
                return member;
            }
        }

        throw new MemberNotFoundException(
                "Member with ID " + id + " not found."
        );
    }

    // Removes a member if the ID exists; otherwise the search throws an exception.
    public void removeMember(int id)
            throws MemberNotFoundException, SQLException {

        Member member = searchMember(id);
        if (!memberDao.deleteMember(id)) {
            throw new MemberNotFoundException("Member with ID " + id + " not found.");
        }

        members.remove(member);
        Member.setMemberCount(members.size());

        System.out.println("Member removed successfully.");
    }

    public ArrayList<Member> getMembers() {
        return members;
    }
}
