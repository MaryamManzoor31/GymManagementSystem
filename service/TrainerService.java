package service;

import model.Trainer;

import java.util.Vector;

// Module M3: Trainer management service.
// This class manages trainers using a Vector, which is synchronized and suitable for thread-safe storage.
public class TrainerService {

    private Vector<Trainer> trainers;

    public TrainerService() {
        trainers = new Vector<>();
    }

    // Adds a trainer to the vector collection.
    public void addTrainer(Trainer trainer) {

        trainers.add(trainer);

        System.out.println("Trainer added successfully.");
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
}
