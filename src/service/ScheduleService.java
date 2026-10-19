package service;

import dao.ScheduleDao;
import model.FitnessSchedule;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// Keeps schedules in a Queue and stores new schedules in MySQL.
public class ScheduleService {

    private final Queue<FitnessSchedule> schedules = new LinkedList<>();
    private final ScheduleDao scheduleDao = new ScheduleDao();

    public void loadFromDatabase() throws SQLException {
        schedules.clear();
        schedules.addAll(scheduleDao.getAllSchedules());
    }

    public FitnessSchedule addSchedule(FitnessSchedule schedule) throws SQLException {
        int id = scheduleDao.addSchedule(schedule);
        FitnessSchedule saved = new FitnessSchedule(id, schedule.getMemberId(), schedule.getTrainerId(),
                schedule.getMemberName(), schedule.getTrainerName(), schedule.getActivity(),
                schedule.getDate(), schedule.getTime());
        schedules.add(saved);
        return saved;
    }

    public FitnessSchedule processSchedule() {
        if (schedules.isEmpty()) return null;
        return schedules.remove();
    }

    public void displaySchedules() {
        for (FitnessSchedule schedule : schedules) schedule.displaySchedule();
    }

    public List<FitnessSchedule> getSchedules() {
        return new LinkedList<>(schedules);
    }

    public void removeSchedulesForMember(int memberId) {
        Iterator<FitnessSchedule> iterator = schedules.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getMemberId() == memberId) iterator.remove();
        }
    }

    public void removeSchedulesForTrainer(int trainerId) {
        Iterator<FitnessSchedule> iterator = schedules.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getTrainerId() == trainerId) iterator.remove();
        }
    }
}
