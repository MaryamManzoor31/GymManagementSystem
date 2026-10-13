package service;

import dao.ScheduleDao;
import model.FitnessSchedule;

import java.sql.SQLException;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

// Module M3: Schedule management service.
// This class uses a Queue to handle fitness schedules in FIFO order.
public class ScheduleService {

    private Queue<FitnessSchedule> schedules;
    private final ScheduleDao scheduleDao;

    public ScheduleService() {
        schedules = new LinkedList<>();
        scheduleDao = new ScheduleDao();
    }

    public void loadFromDatabase() throws SQLException {
        schedules.clear();
        schedules.addAll(scheduleDao.getAllSchedules());
    }

    // Adds a new schedule to the queue; the first inserted schedule is processed first.
    public FitnessSchedule addSchedule(FitnessSchedule schedule) throws SQLException {
        int scheduleId = scheduleDao.addSchedule(schedule);
        FitnessSchedule savedSchedule = new FitnessSchedule(
                scheduleId,
                schedule.getMemberId(),
                schedule.getTrainerId(),
                schedule.getMemberName(),
                schedule.getTrainerName(),
                schedule.getActivity(),
                schedule.getDate(),
                schedule.getTime()
        );
        schedules.add(savedSchedule);

        System.out.println("Schedule added.");
        return savedSchedule;
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

    public List<FitnessSchedule> getSchedules() {
        return new LinkedList<>(schedules);
    }
}
