package model;

// Module M2: Fitness schedule model.
// This class stores one gym activity booking and displays the schedule details.
public class FitnessSchedule {

    private int scheduleId;
    private int memberId;
    private int trainerId;
    private String memberName;
    private String trainerName;
    private String activity;
    private String date;
    private String time;

    public FitnessSchedule(int scheduleId,
                           int memberId,
                           int trainerId,
                           String memberName,
                           String trainerName,
                           String activity,
                           String date,
                           String time) {

        this.scheduleId = scheduleId;
        this.memberId = memberId;
        this.trainerId = trainerId;
        this.memberName = memberName;
        this.trainerName = trainerName;
        this.activity = activity;
        this.date = date;
        this.time = time;
    }

    public FitnessSchedule(int scheduleId,
                           String memberName,
                           String trainerName,
                           String activity,
                           String date,
                           String time) {

        this.scheduleId = scheduleId;
        this.memberName = memberName;
        this.trainerName = trainerName;
        this.activity = activity;
        this.date = date;
        this.time = time;
        this.memberId = 0;
        this.trainerId = 0;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public String getActivity() {
        return activity;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public void displaySchedule() {

        System.out.println("Schedule ID: " + scheduleId);
        System.out.println("Member: " + memberName);
        System.out.println("Trainer: " + trainerName);
        System.out.println("Activity: " + activity);
        System.out.println("Date: " + date);
        System.out.println("Time: " + time);
    }
}
