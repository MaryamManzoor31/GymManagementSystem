package gui;

import java.sql.SQLException;
import java.util.List;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import exception.InvalidAgeException;
import exception.InvalidPhoneException;

// Module M1: Main GUI module.
// This class creates the gym management dashboard and connects the UI with all services.
public class MainFrame extends JFrame {

    JLabel idLabel, nameLabel, ageLabel, phoneLabel;
    JTextField idField, nameField, ageField, phoneField;

    JButton regularButton, premiumButton, displayButton;

    JButton addTrainerBtn, showTrainersBtn;
    JButton updateTrainerBtn, deleteTrainerBtn;

    JButton addScheduleBtn, processScheduleBtn;
    JButton reportBtn, recentActionBtn;

    JTextArea outputArea;

    MemberService memberService;
    TrainerService trainerService;
    ScheduleService scheduleService;
    java.util.Stack<String> recentActions;

    public MainFrame() {
        // Initialize all services to demonstrate Collections
        memberService = new MemberService();       // ArrayList
        trainerService = new TrainerService();     // Vector
        scheduleService = new ScheduleService();   // Queue
        recentActions = new java.util.Stack<>();   // Stack

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

        
        // --- ROW 2: Trainers (MySQL CRUD) ---
        addTrainerBtn = new JButton("Add Trainer");
        showTrainersBtn = new JButton("Show Trainers");
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
                if(tName != null && !tName.isEmpty()) {
                    Trainer t = new Trainer(1, tName, "General Fitness", "9876543210");
                    trainerService.addTrainer(t);
                    recentActions.push("Added Trainer: " + tName); // Demonstrates Stack
                    outputArea.setText("Trainer added to Vector.");
                }
            }
        });

        showTrainersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("Trainers details printed to the Console.\n");
                trainerService.displayTrainers();
            }
        });

        // Demonstrates Queue
        addScheduleBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String activity = JOptionPane.showInputDialog("Enter Activity (e.g. Yoga):");
                if(activity != null && !activity.isEmpty()) {
                    FitnessSchedule fs = new FitnessSchedule(1, "Member", "Trainer", activity, "Today", "Morning");
                    scheduleService.addSchedule(fs);
                    recentActions.push("Added Schedule: " + activity);
                    outputArea.setText("Schedule added to Queue.");
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
        } catch (InvalidAgeException e) {
            outputArea.setText(e.getMessage());
        } catch (InvalidPhoneException e) {
            outputArea.setText(e.getMessage());
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
        new MainFrame();
    }
}
