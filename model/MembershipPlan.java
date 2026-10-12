package model;

public class MembershipPlan {

    private int planId;
    private String planName;
    private int duration;
    private double price;

    public MembershipPlan(int planId, String planName,
                          int duration, double price) {

        this.planId = planId;
        this.planName = planName;
        this.duration = duration;
        this.price = price;
    }

    public void displayPlan() {

        System.out.println("Plan ID: " + planId);
        System.out.println("Plan Name: " + planName);
        System.out.println("Duration: " + duration + " months");
        System.out.println("Price: Rs." + price);
    }
}
