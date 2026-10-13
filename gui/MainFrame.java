package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import model.Member;
import model.RegularMember;
import model.PremiumMember;
import model.Trainer;
import model.FitnessSchedule;

import service.MemberService;
import service.TrainerService;
import service.ScheduleService;

import util.FileManager;
import util.ReportTask;

import exception.DuplicateMemberIdException;
import exception.InvalidAgeException;
import exception.InvalidPhoneException;
import exception.MemberNotFoundException;
import database.DatabaseSetup;

// Module M1: Main GUI module.
// This class creates the gym management dashboard and connects the UI with all services.
public class MainFrame extends JFrame {

    JLabel idLabel, nameLabel, ageLabel, phoneLabel;
    JTextField idField, nameField, ageField, phoneField;

    JButton regularButton, premiumButton, displayButton;
    JButton addTrainerBtn, showTrainersBtn;
    JButton addScheduleBtn, processScheduleBtn;
    JButton reportBtn, recentActionBtn;

    JTextArea outputArea;

    MemberService memberService;
    TrainerService trainerService;
    ScheduleService scheduleService;
    java.util.Stack<String> recentActions;

    public MainFrame() throws SQLException {
        // Initialize all services to demonstrate Collections
        memberService = new MemberService();       // ArrayList
        trainerService = new TrainerService();     // Vector
        scheduleService = new ScheduleService();   // Queue
        recentActions = new java.util.Stack<>();   // Stack
        memberService.loadFromDatabase();
        trainerService.loadFromDatabase();
        scheduleService.loadFromDatabase();

        setTitle("Gym Membership Management System");
        setSize(800, 750);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Labels for Member Input
        idLabel = new JLabel("Member ID:");
        nameLabel = new JLabel("Name:");
        ageLabel = new JLabel("Age:");
        phoneLabel = new JLabel("Phone:");

        idLabel.setBounds(150, 40, 150, 30);
        nameLabel.setBounds(150, 90, 150, 30);
        ageLabel.setBounds(150, 140, 150, 30);
        phoneLabel.setBounds(150, 190, 150, 30);

        add(idLabel); add(nameLabel); add(ageLabel); add(phoneLabel);

        // Text fields
        idField = new JTextField();
        nameField = new JTextField();
        ageField = new JTextField();
        phoneField = new JTextField();

        idField.setBounds(300, 40, 300, 30);
        nameField.setBounds(300, 90, 300, 30);
        ageField.setBounds(300, 140, 300, 30);
        phoneField.setBounds(300, 190, 300, 30);

        add(idField); add(nameField); add(ageField); add(phoneField);

        // --- ROW 1: Members (ArrayList) ---
        regularButton = new JButton("Add Regular");
        premiumButton = new JButton("Add Premium");
        displayButton = new JButton("Display Members");

        regularButton.setBounds(100, 260, 180, 40);
        premiumButton.setBounds(310, 260, 180, 40);
        displayButton.setBounds(520, 260, 180, 40);

        add(regularButton); add(premiumButton); add(displayButton);

        // --- ROW 2: Trainers (Vector) ---
        addTrainerBtn = new JButton("Add Trainer");
        showTrainersBtn = new JButton("Show Trainers");
        
        addTrainerBtn.setBounds(100, 320, 285, 40);
        showTrainersBtn.setBounds(415, 320, 285, 40);
        
        add(addTrainerBtn); add(showTrainersBtn);

        // --- ROW 3: Schedules (Queue) ---
        addScheduleBtn = new JButton("Add Schedule");
        processScheduleBtn = new JButton("Process Schedule");
        
        addScheduleBtn.setBounds(100, 380, 285, 40);
        processScheduleBtn.setBounds(415, 380, 285, 40);

        add(addScheduleBtn); add(processScheduleBtn);

        // --- ROW 4: Threads, File I/O & Stack ---
        reportBtn = new JButton("Save Report (Thread)");
        recentActionBtn = new JButton("Last Action (Stack)");

        reportBtn.setBounds(100, 440, 285, 40);
        recentActionBtn.setBounds(415, 440, 285, 40);

        add(reportBtn); add(recentActionBtn);

        // --- Output Area ---
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBounds(100, 500, 600, 180);
        add(scrollPane);

        // ====== EVENT LISTENERS ======

        regularButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMember(false);
            }
        });

        premiumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMember(true);
            }
        });

        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayMembers();
            }
        });

        // Demonstrates Vector
        addTrainerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String tName = JOptionPane.showInputDialog("Enter Trainer Name:");
                if (tName != null && !tName.trim().isEmpty()) {
                    Trainer trainer = new Trainer(0, tName.trim(), "General Fitness", "9876543210");
                    try {
                        Trainer savedTrainer = trainerService.addTrainer(trainer);
                        recentActions.push("Added Trainer: " + savedTrainer.getName()); // Demonstrates Stack
                        outputArea.setText("Trainer saved to the database with ID "
                                + savedTrainer.getTrainerId() + ".");
                    } catch (SQLException ex) {
                        showDatabaseError(ex);
                    }
                }
            }
        });

        showTrainersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("");
                for (Trainer trainer : trainerService.getTrainers()) {
                    outputArea.append("ID: " + trainer.getTrainerId() + "\n");
                    outputArea.append("Name: " + trainer.getName() + "\n");
                    outputArea.append("Specialization: " + trainer.getSpecialization() + "\n");
                    outputArea.append("Phone: " + trainer.getPhone() + "\n");
                    outputArea.append("--------------------\n");
                }
                if (trainerService.getTrainers().isEmpty()) {
                    outputArea.setText("No trainers saved yet.");
                }
            }
        });

        // Demonstrates Queue
        addScheduleBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String activity = JOptionPane.showInputDialog("Enter Activity (e.g. Yoga):");
                if (activity == null || activity.trim().isEmpty()) {
                    return;
                }
                String memberIdInput = JOptionPane.showInputDialog("Enter an existing Member ID:");
                if (memberIdInput == null) {
                    return;
                }
                String trainerIdInput = JOptionPane.showInputDialog("Enter an existing Trainer ID:");
                if (trainerIdInput == null) {
                    return;
                }
                String dateInput = JOptionPane.showInputDialog("Enter date (YYYY-MM-DD):");
                if (dateInput == null) {
                    return;
                }
                String timeInput = JOptionPane.showInputDialog("Enter time (HH:MM, 24-hour):");
                if (timeInput == null) {
                    return;
                }

                try {
                    int memberId = Integer.parseInt(memberIdInput.trim());
                    int trainerId = Integer.parseInt(trainerIdInput.trim());
                    Member member = memberService.searchMember(memberId);
                    Trainer trainer = trainerService.getTrainers().stream()
                            .filter(item -> item.getTrainerId() == trainerId)
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Trainer ID " + trainerId + " was not found."));
                    String date = LocalDate.parse(dateInput.trim()).toString();
                    String time = LocalTime.parse(timeInput.trim()).toString();
                    FitnessSchedule schedule = new FitnessSchedule(
                            0, memberId, trainerId, member.getName(), trainer.getName(),
                            activity.trim(), date, time);
                    FitnessSchedule savedSchedule = scheduleService.addSchedule(schedule);
                    recentActions.push("Added Schedule: " + savedSchedule.getActivity());
                    outputArea.setText("Schedule saved to the database with ID "
                            + savedSchedule.getScheduleId() + ".");
                } catch (NumberFormatException ex) {
                    outputArea.setText("Member ID and Trainer ID must be valid numbers.");
                } catch (MemberNotFoundException | IllegalArgumentException ex) {
                    outputArea.setText(ex.getMessage());
                } catch (SQLException ex) {
                    showDatabaseError(ex);
                }
            }
        });

        processScheduleBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("Processing next schedule...\nCheck console for details.");
                scheduleService.processSchedule(); // FIFO Processing
            }
        });

        // Demonstrates Threads & File Streams
        reportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String reportText = "Gym Report\nTotal Members: " + Member.getMemberCount();
                
                // 1. Threading & Character Stream
                Thread t = new Thread(new ReportTask(reportText));
                t.start();
                
                // 2. Byte Stream Backup
                FileManager.saveBackup(reportText);
                
                recentActions.push("Generated Report");
                outputArea.setText("Report Thread started and Byte backup saved.\nFiles generated: gym_report.txt and gym_backup.dat.");
            }
        });

        // Demonstrates Stack (LIFO)
        recentActionBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!recentActions.isEmpty()) {
                    outputArea.setText("Last Action: " + recentActions.peek());
                } else {
                    outputArea.setText("No recent actions.");
                }
            }
        });

        setVisible(true);
    }

    private void addMember(boolean premium) {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String phone = phoneField.getText();

            Member member;
            if (premium) {
                member = new PremiumMember(id, name, age, phone);
            } else {
                member = new RegularMember(id, name, age, phone);
            }

            memberService.addMember(member);
            recentActions.push("Added Member: " + name);
            outputArea.setText("Member added successfully.");

        } catch (NumberFormatException e) {
            outputArea.setText("Please enter valid numbers for ID and Age.");
        } catch (DuplicateMemberIdException e) {
            outputArea.setText(e.getMessage());
        } catch (InvalidAgeException e) {
            outputArea.setText(e.getMessage());
        } catch (InvalidPhoneException e) {
            outputArea.setText(e.getMessage());
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void displayMembers() {
        outputArea.setText("");
        for (Member member : memberService.getMembers()) {
            outputArea.append("ID: " + member.getMemberId() + "\n");
            outputArea.append("Name: " + member.getName() + "\n");
            outputArea.append("Age: " + member.getAge() + "\n");
            outputArea.append("Phone: " + member.getPhone() + "\n");
            outputArea.append("--------------------\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseSetup.initializeDatabase();
                new MainFrame();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "Could not connect to or initialize MySQL.\n"
                                + "Check that MySQL is running, the JDBC driver is available, "
                                + "and the configured credentials are correct.\n\n"
                                + e.getMessage(),
                        "Database startup failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void showDatabaseError(SQLException e) {
        e.printStackTrace();
        outputArea.setText("Database operation failed: " + e.getMessage());
    }
}
