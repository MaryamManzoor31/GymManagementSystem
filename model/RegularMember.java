package model;

// Module M2: Regular membership type.
// This class represents a standard member and adds a type-specific display format.
public class RegularMember extends Member {

    public RegularMember(int memberId, String name,
                         int age, String phone) {

        super(memberId, name, age, phone);
    }

    @Override
    public void displayDetails() {

        System.out.println("Membership Type: Regular");

        super.displayDetails();
    }
}
