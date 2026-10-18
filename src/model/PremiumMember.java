package model;

// Module M2: Premium membership type.
// This class specializes the base member model for premium customers with a distinct label.
public class PremiumMember extends Member {

    public PremiumMember(int memberId, String name,
                         int age, String phone) {

        super(memberId, name, age, phone);
    }

    @Override
    public void displayDetails() {

        System.out.println("Membership Type: Premium");

        super.displayDetails();
    }
}
