package service;

import model.FitnessSchedule;

import java.util.LinkedList;
import java.util.Queue;

// Module M3: Schedule management service.
// This class uses a Queue to handle fitness schedules in FIFO order.
public class ScheduleService {

    private Queue<FitnessSchedule> schedules;

    public ScheduleService() {
        schedules = new LinkedList<>();
    }

    // Adds a new schedule to the queue; the first inserted schedule is processed first.
    public void addSchedule(FitnessSchedule schedule) {

        schedules.add(schedule);

        System.out.println("Schedule added.");
    }

    // Removes and processes the earliest schedule in the queue.
    public void processSchedule() {

        if (schedules.isEmpty()) {

            System.out.println("No schedules available.");

        } else {

            FitnessSchedule schedule = schedules.remove();

            schedule.displaySchedule();
        }
    }

    // Displays all pending schedules remaining in the queue.
    public void displaySchedules() {

        if (schedules.isEmpty()) {

            System.out.println("No schedules available.");
            return;
        }

        for (FitnessSchedule schedule : schedules) {

            schedule.displaySchedule();

            System.out.println("----------------------");
        }
    }
}
