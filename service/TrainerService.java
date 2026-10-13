package service;

import dao.TrainerDao;
import model.Trainer;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

// Module M3: Trainer management service.
// This class manages trainers using a Vector, which is synchronized and suitable for thread-safe storage.
public class TrainerService {

    private Vector<Trainer> trainers;
    private final TrainerDao trainerDao;

    public TrainerService() {
        trainers = new Vector<>();
        trainerDao = new TrainerDao();
    }

    public void loadFromDatabase() throws SQLException {
        trainers.clear();
        trainers.addAll(trainerDao.getAllTrainers());
    }

    // Adds a trainer to the vector collection.
    public Trainer addTrainer(Trainer trainer) throws SQLException {
        int trainerId = trainerDao.addTrainer(trainer);
        Trainer savedTrainer = new Trainer(
                trainerId, trainer.getName(), trainer.getSpecialization(), trainer.getPhone());
        trainers.add(savedTrainer);

        System.out.println("Trainer added successfully.");
        return savedTrainer;
    }

    // Displays every trainer stored in the vector.
    public void displayTrainers() {

        if (trainers.isEmpty()) {

            System.out.println("No trainers available.");
            return;
        }

        for (Trainer trainer : trainers) {

            trainer.displayDetails();

            System.out.println("----------------------");
        }
    }

    public List<Trainer> getTrainers() {
        return trainers;
    }
}
