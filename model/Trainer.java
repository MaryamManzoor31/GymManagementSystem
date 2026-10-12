package model;

// Module M2: Trainer model.
// This class stores trainer information and follows the same display contract as members.
public class Trainer implements Displayable {

    private int trainerId;
    private String name;
    private String specialization;
    private String phone;

    public Trainer(int trainerId, String name,
                   String specialization, String phone) {

        this.trainerId = trainerId;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public void displayDetails() {

        System.out.println("Trainer ID: " + trainerId);
        System.out.println("Trainer Name: " + name);
        System.out.println("Specialization: " + specialization);
        System.out.println("Phone: " + phone);
    }
}
