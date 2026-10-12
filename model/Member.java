package model;

// Module M2: Core member model.
// This abstract class represents the shared data and behavior for all gym members.
public abstract class Member implements Displayable {

    private int memberId;
    private String name;
    private int age;
    private String phone;

    // Tracks how many member objects have been created in the system.
    static int memberCount = 0;

    public static final int MIN_AGE = 16;
    public static final int MAX_AGE = 70;

    // Constructor: initializes the member properties.
    public Member(int memberId, String name, int age, String phone) {
        this.memberId = memberId;
        this.name = name;
        this.age = age;
        this.phone = phone;
    }

    // Getters: used by service classes to read member information.
    public int getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    // Setters: allow updating member information after creation.
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Method overloading: same method name with different parameters for display flexibility.
    public void displayDetails() {
        System.out.println("Member ID: " + memberId);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Phone: " + phone);
    }

    public void displayDetails(boolean showPhone) {
        System.out.println("Member ID: " + memberId);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);

        if (showPhone) {
            System.out.println("Phone: " + phone);
        }
    }

    // Static counter used by report generation to show total members.
    public static int getMemberCount() {
        return memberCount;
    }

    public static void incrementMemberCount() {
        memberCount++;
    }
}
