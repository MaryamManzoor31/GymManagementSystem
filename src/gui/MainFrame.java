package gui;

import java.sql.SQLException;
import java.util.List;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

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

<<<<<<< HEAD:src/gui/MainFrame.java
    JButton regularButton, premiumButton, displayButton;

    JButton addTrainerBtn, showTrainersBtn;
    JButton updateTrainerBtn, deleteTrainerBtn;

    JButton addScheduleBtn, processScheduleBtn;
=======
    JButton regularButton, premiumButton, displayButton, deleteMemberBtn;
    JButton addTrainerBtn, showTrainersBtn, deleteTrainerBtn;
    JButton addScheduleBtn, processScheduleBtn, showSchedulesBtn;
>>>>>>> 81a6a1de56c09288966e895ec2c7bcb6e7a7d461:gui/MainFrame.java
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
        setSize(1000, 900);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Labels for Member Input
        idLabel = new JLabel("Member ID:");
        nameLabel = new JLabel("Name:");
        ageLabel = new JLabel("Age:");
        phoneLabel = new JLabel("Phone:");

        idLabel.setBounds(200, 50, 160, 32);
        nameLabel.setBounds(200, 110, 160, 32);
        ageLabel.setBounds(200, 170, 160, 32);
        phoneLabel.setBounds(200, 230, 160, 32);

        add(idLabel); add(nameLabel); add(ageLabel); add(phoneLabel);

        // Text fields
        idField = new JTextField();
        nameField = new JTextField();
        ageField = new JTextField();
        phoneField = new JTextField();

        idField.setBounds(380, 50, 420, 34);
        nameField.setBounds(380, 110, 420, 34);
        ageField.setBounds(380, 170, 420, 34);
        phoneField.setBounds(380, 230, 420, 34);

        add(idField); add(nameField); add(ageField); add(phoneField);

        // --- ROW 1: Members (ArrayList) ---
        regularButton = new JButton("Add Regular");
        premiumButton = new JButton("Add Premium");
        displayButton = new JButton("Display Members");
        deleteMemberBtn = new JButton("Delete Member");

        regularButton.setBounds(40, 315, 210, 48);
        premiumButton.setBounds(280, 315, 210, 48);
        displayButton.setBounds(520, 315, 210, 48);
        deleteMemberBtn.setBounds(760, 315, 200, 48);

        add(regularButton); add(premiumButton); add(displayButton); add(deleteMemberBtn);

        
        // --- ROW 2: Trainers (MySQL CRUD) ---
        addTrainerBtn = new JButton("Add Trainer");
        showTrainersBtn = new JButton("Show Trainers");
<<<<<<< HEAD:src/gui/MainFrame.java
        updateTrainerBtn = new JButton("Update Trainer");
        deleteTrainerBtn = new JButton("Delete Trainer");

        addTrainerBtn.setBounds(70, 320, 155, 40);
        showTrainersBtn.setBounds(245, 320, 155, 40);
        updateTrainerBtn.setBounds(420, 320, 155, 40);
        deleteTrainerBtn.setBounds(595, 320, 155, 40);

        add(addTrainerBtn);
        add(showTrainersBtn);
        add(updateTrainerBtn);
        add(deleteTrainerBtn);

=======
        deleteTrainerBtn = new JButton("Delete Trainer");
        
        addTrainerBtn.setBounds(140, 395, 210, 48);
        showTrainersBtn.setBounds(395, 395, 210, 48);
        deleteTrainerBtn.setBounds(650, 395, 210, 48);
        
        add(addTrainerBtn); add(showTrainersBtn); add(deleteTrainerBtn);
>>>>>>> 81a6a1de56c09288966e895ec2c7bcb6e7a7d461:gui/MainFrame.java

        // --- ROW 3: Schedules (Queue) ---
        addScheduleBtn = new JButton("Add Schedule");
        processScheduleBtn = new JButton("Process Schedule");
        showSchedulesBtn = new JButton("Display Schedules");

        addScheduleBtn.setBounds(140, 475, 210, 48);
        processScheduleBtn.setBounds(395, 475, 210, 48);
        showSchedulesBtn.setBounds(650, 475, 210, 48);

        add(addScheduleBtn); add(processScheduleBtn); add(showSchedulesBtn);

        // --- ROW 4: Threads, File I/O & Stack ---
        reportBtn = new JButton("Save Report (Thread)");
        recentActionBtn = new JButton("Last Action (Stack)");

        reportBtn.setBounds(200, 555, 270, 48);
        recentActionBtn.setBounds(530, 555, 270, 48);

        add(reportBtn); add(recentActionBtn);

        // --- Output Area ---
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBounds(60, 625, 880, 215);
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

        deleteMemberBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteMember();
            }
        });

        // Demonstrates Vector
        addTrainerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String tName = JOptionPane.showInputDialog("Enter Trainer Name:");
                if (tName == null || tName.trim().isEmpty()) {
                    return;
                }

                String specialization = JOptionPane.showInputDialog(
                        "Enter Trainer Specialization (e.g. Yoga, Strength Training):");
                if (specialization == null || specialization.trim().isEmpty()) {
                    return;
                }

                Trainer trainer = new Trainer(
                        0, tName.trim(), specialization.trim(), "9876543210");
                try {
                    Trainer savedTrainer = trainerService.addTrainer(trainer);
                    recentActions.push("Added Trainer: " + savedTrainer.getName()); // Demonstrates Stack
                    outputArea.setText("Trainer saved to the database with ID "
                            + savedTrainer.getTrainerId() + ".");
                } catch (SQLException ex) {
                    showDatabaseError(ex);
                }
            }
        });

        showTrainersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("");
                for (Trainer trainer : trainerService.getTrainers()) {
                    outputArea.append("ID: " + trainer.getTrainerId() + "\n");
                    outputArea.append("Trainer Name: " + trainer.getName() + "\n");
                    outputArea.append("Specialization: " + trainer.getSpecialization() + "\n");
                    outputArea.append("Phone: " + trainer.getPhone() + "\n");
                    outputArea.append("--------------------\n");
                }
                if (trainerService.getTrainers().isEmpty()) {
                    outputArea.setText("No trainers saved yet.");
                }
            }
        });

        deleteTrainerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteTrainer();
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
                String dateInput = JOptionPane.showInputDialog("Enter date (DD-MM-YYYY):");
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
                    Trainer trainer = null;
                    for (Trainer item : trainerService.getTrainers()) {
                        if (item.getTrainerId() == trainerId) {
                            trainer = item;
                            break;
                        }
                    }
                    if (trainer == null) {
                        throw new IllegalArgumentException(
                                "Trainer ID " + trainerId + " was not found.");
                    }
                    String date = LocalDate.parse(
                            dateInput.trim(),
                            DateTimeFormatter.ofPattern("dd-MM-uuuu")
                                    .withResolverStyle(ResolverStyle.STRICT)
                    ).toString();
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
                FitnessSchedule schedule = scheduleService.processSchedule();
                if (schedule == null) {
                    outputArea.setText("No schedules available.");
                } else {
                    outputArea.setText(formatSchedule(schedule));
                }
            }
        });

        showSchedulesBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displaySchedules();
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
        if (memberService.getMembers().isEmpty()) {
            outputArea.setText("No members saved yet.");
            return;
        }
        for (Member member : memberService.getMembers()) {
            outputArea.append("ID: " + member.getMemberId() + "\n");
            outputArea.append("Name: " + member.getName() + "\n");
            outputArea.append("Age: " + member.getAge() + "\n");
            outputArea.append("Phone: " + member.getPhone() + "\n");
            outputArea.append("Membership Status: " + getMembershipStatus(member) + "\n");
            outputArea.append("--------------------\n");
        }
    }

    private void deleteMember() {
        String input = JOptionPane.showInputDialog(this, "Enter Member ID to delete:");
        if (input == null) {
            return;
        }

        try {
            int memberId = Integer.parseInt(input.trim());
            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Delete member " + memberId + " and all schedules linked to this member?",
                    "Confirm Member Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (confirmation != JOptionPane.YES_OPTION) {
                return;
            }

            memberService.removeMember(memberId);
            scheduleService.removeSchedulesForMember(memberId);
            recentActions.push("Deleted Member ID: " + memberId);
            outputArea.setText("Member " + memberId + " and linked schedules were deleted.");
        } catch (NumberFormatException e) {
            outputArea.setText("Please enter a valid numeric Member ID.");
        } catch (MemberNotFoundException e) {
            outputArea.setText(e.getMessage());
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void deleteTrainer() {
        String input = JOptionPane.showInputDialog(this, "Enter Trainer ID to delete:");
        if (input == null) {
            return;
        }

        try {
            int trainerId = Integer.parseInt(input.trim());
            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Delete trainer " + trainerId + " and all schedules linked to this trainer?",
                    "Confirm Trainer Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (confirmation != JOptionPane.YES_OPTION) {
                return;
            }

            if (!trainerService.removeTrainer(trainerId)) {
                outputArea.setText("Trainer with ID " + trainerId + " was not found.");
                return;
            }

            scheduleService.removeSchedulesForTrainer(trainerId);
            recentActions.push("Deleted Trainer ID: " + trainerId);
            outputArea.setText("Trainer " + trainerId + " and linked schedules were deleted.");
        } catch (NumberFormatException e) {
            outputArea.setText("Please enter a valid numeric Trainer ID.");
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void displaySchedules() {
        outputArea.setText("");
        if (scheduleService.getSchedules().isEmpty()) {
            outputArea.setText("No schedules saved yet.");
            return;
        }

        for (FitnessSchedule schedule : scheduleService.getSchedules()) {
            outputArea.append(formatSchedule(schedule));
            outputArea.append("--------------------\n");
        }
    }

    private String formatSchedule(FitnessSchedule schedule) {
        StringBuilder details = new StringBuilder();
        details.append("Schedule ID: ").append(schedule.getScheduleId()).append("\n");
        details.append("Member Name: ").append(schedule.getMemberName()).append("\n");
        details.append("Membership Status: ")
                .append(getMembershipStatus(schedule.getMemberId())).append("\n");
        details.append("Trainer Name: ").append(schedule.getTrainerName()).append("\n");
        details.append("Activity Name: ").append(schedule.getActivity()).append("\n");
        details.append("Date: ").append(schedule.getDate()).append("\n");
        details.append("Time: ").append(schedule.getTime()).append("\n");
        return details.toString();
    }

    private String getMembershipStatus(int memberId) {
        try {
            return getMembershipStatus(memberService.searchMember(memberId));
        } catch (MemberNotFoundException e) {
            return "Unknown";
        }
    }

    private String getMembershipStatus(Member member) {
        return member instanceof PremiumMember ? "Premium" : "Regular";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
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
            }
        });
    }

    private void showDatabaseError(SQLException e) {
        e.printStackTrace();
        outputArea.setText("Database operation failed: " + e.getMessage());
    }
}
