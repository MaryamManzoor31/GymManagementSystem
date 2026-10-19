package gui;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
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
import dao.MembershipPlanDao;
import model.MembershipPlan;

// Main Swing window. JDBC calls are handled by the existing service classes.
public class MainFrame extends JFrame {

    JLabel idLabel, nameLabel, ageLabel, phoneLabel;
    JTextField idField, nameField, ageField, phoneField;

    JButton regularButton, premiumButton, displayButton, updateMemberBtn, deleteMemberBtn;
    JButton addTrainerBtn, showTrainersBtn, updateTrainerBtn, deleteTrainerBtn;
    JButton addScheduleBtn, processScheduleBtn, showSchedulesBtn, updateScheduleBtn, deleteScheduleBtn;
    JButton addPlanBtn, showPlansBtn, updatePlanBtn, deletePlanBtn;
    MembershipPlanDao planDao = new MembershipPlanDao();
    JButton reportBtn, recentActionBtn;
    JTextArea outputArea;

    MemberService memberService;
    TrainerService trainerService;
    ScheduleService scheduleService;
    java.util.Stack<String> recentActions;

    public MainFrame() throws SQLException {
        memberService = new MemberService();
        trainerService = new TrainerService();
        scheduleService = new ScheduleService();
        recentActions = new java.util.Stack<String>();

        memberService.loadFromDatabase();
        trainerService.loadFromDatabase();
        scheduleService.loadFromDatabase();

        setTitle("Gym Membership Management System");
        setSize(1020, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel(null);
        contentPanel.setPreferredSize(new Dimension(1000, 960));
        setContentPane(new JScrollPane(contentPanel));

        idLabel = new JLabel("Member ID:");
        nameLabel = new JLabel("Name:");
        ageLabel = new JLabel("Age:");
        phoneLabel = new JLabel("Phone:");
        idLabel.setBounds(180, 35, 160, 32);
        nameLabel.setBounds(180, 85, 160, 32);
        ageLabel.setBounds(180, 135, 160, 32);
        phoneLabel.setBounds(180, 185, 160, 32);
        contentPanel.add(idLabel); contentPanel.add(nameLabel);
        contentPanel.add(ageLabel); contentPanel.add(phoneLabel);

        idField = new JTextField();
        nameField = new JTextField();
        ageField = new JTextField();
        phoneField = new JTextField();
        idField.setBounds(350, 35, 440, 32);
        nameField.setBounds(350, 85, 440, 32);
        ageField.setBounds(350, 135, 440, 32);
        phoneField.setBounds(350, 185, 440, 32);
        contentPanel.add(idField); contentPanel.add(nameField);
        contentPanel.add(ageField); contentPanel.add(phoneField);

        // Members: Create, Read, Update, Delete
        regularButton = new JButton("Add Regular Member");
        premiumButton = new JButton("Add Premium Member");
        displayButton = new JButton("Display Members");
        updateMemberBtn = new JButton("Update Member");
        deleteMemberBtn = new JButton("Delete Member");
        JButton[] memberButtons = {regularButton, premiumButton, displayButton, updateMemberBtn, deleteMemberBtn};
        for (int i = 0; i < memberButtons.length; i++) {
            memberButtons[i].setBounds(15 + i * 195, 245, 185, 42);
            contentPanel.add(memberButtons[i]);
        }

        // Trainers: Create, Read, Update, Delete
        addTrainerBtn = new JButton("Add Trainer");
        showTrainersBtn = new JButton("Show Trainers");
        updateTrainerBtn = new JButton("Update Trainer");
        deleteTrainerBtn = new JButton("Delete Trainer");
        JButton[] trainerButtons = {addTrainerBtn, showTrainersBtn, updateTrainerBtn, deleteTrainerBtn};
        for (int i = 0; i < trainerButtons.length; i++) {
            trainerButtons[i].setBounds(35 + i * 240, 315, 220, 42);
            contentPanel.add(trainerButtons[i]);
        }

        // Schedules: existing add, queue processing, and display actions
        addScheduleBtn = new JButton("Add Schedule");
        processScheduleBtn = new JButton("Process Next");
        showSchedulesBtn = new JButton("Display Schedules");
        updateScheduleBtn = new JButton("Update Schedule");
        deleteScheduleBtn = new JButton("Delete Schedule");
        JButton[] scheduleButtons = {addScheduleBtn, processScheduleBtn, showSchedulesBtn, updateScheduleBtn, deleteScheduleBtn};
        for (int i = 0; i < scheduleButtons.length; i++) {
            scheduleButtons[i].setBounds(15 + i * 195, 385, 185, 42);
            contentPanel.add(scheduleButtons[i]);
        }

        // Membership plans: JDBC CRUD operations for the existing membership_plans table.
        addPlanBtn = new JButton("Add Plan");
        showPlansBtn = new JButton("Show Plans");
        updatePlanBtn = new JButton("Update Plan");
        deletePlanBtn = new JButton("Delete Plan");
        JButton[] planButtons = {addPlanBtn, showPlansBtn, updatePlanBtn, deletePlanBtn};
        for (int i = 0; i < planButtons.length; i++) {
            planButtons[i].setBounds(35 + i * 240, 455, 220, 42);
            contentPanel.add(planButtons[i]);
        }

        reportBtn = new JButton("Save Report (Thread)");
        recentActionBtn = new JButton("Last Action (Stack)");
        reportBtn.setBounds(210, 525, 250, 42);
        recentActionBtn.setBounds(530, 525, 250, 42);
        contentPanel.add(reportBtn); contentPanel.add(recentActionBtn);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBounds(60, 595, 880, 320);
        contentPanel.add(scrollPane);

        regularButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { addMember(false); }
        });
        premiumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { addMember(true); }
        });
        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { displayMembers(); }
        });
        updateMemberBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { updateMember(); }
        });
        deleteMemberBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { deleteMember(); }
        });

        addTrainerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { addTrainer(); }
        });
        showTrainersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { displayTrainers(); }
        });
        updateTrainerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { updateTrainer(); }
        });
        deleteTrainerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { deleteTrainer(); }
        });

        addScheduleBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { addSchedule(); }
        });
        processScheduleBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FitnessSchedule schedule = scheduleService.processSchedule();
                if (schedule == null) outputArea.setText("No schedules available.");
                else outputArea.setText(formatSchedule(schedule));
            }
        });
        showSchedulesBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { displaySchedules(); }
        });
        updateScheduleBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { updateSchedule(); }
        });
        deleteScheduleBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { deleteSchedule(); }
        });

        addPlanBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { addPlan(); }
        });
        showPlansBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { displayPlans(); }
        });
        updatePlanBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { updatePlan(); }
        });
        deletePlanBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { deletePlan(); }
        });

        reportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String reportText = "Gym Report\nTotal Members: " + Member.getMemberCount();
                Thread thread = new Thread(new ReportTask(reportText));
                thread.start();
                FileManager.saveBackup(reportText);
                recentActions.push("Generated Report");
                outputArea.setText("Report started and backup saved.\nFiles: gym_report.txt and gym_backup.dat.");
            }
        });
        recentActionBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (recentActions.empty()) outputArea.setText("No recent actions.");
                else outputArea.setText("Last Action: " + recentActions.peek());
            }
        });

        setVisible(true);
    }

    private void addMember(boolean premium) {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String phone = phoneField.getText().trim();
            Member member = premium
                    ? new PremiumMember(id, name, age, phone)
                    : new RegularMember(id, name, age, phone);
            memberService.addMember(member);
            recentActions.push("Added Member: " + name);
            outputArea.setText("Member added to MySQL successfully.");
        } catch (NumberFormatException e) {
            outputArea.setText("Enter a valid numeric Member ID and Age.");
        } catch (DuplicateMemberIdException | InvalidAgeException | InvalidPhoneException | IllegalArgumentException e) {
            outputArea.setText(e.getMessage());
        } catch (SQLException e) { showDatabaseError(e); }
    }

    private void updateMember() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            int age = Integer.parseInt(ageField.getText().trim());
            memberService.updateMember(id, nameField.getText(), age, phoneField.getText());
            recentActions.push("Updated Member ID: " + id);
            outputArea.setText("Member updated in MySQL successfully.");
        } catch (NumberFormatException e) {
            outputArea.setText("Enter a valid numeric Member ID and Age in the fields above.");
        } catch (MemberNotFoundException | InvalidAgeException | InvalidPhoneException | IllegalArgumentException e) {
            outputArea.setText(e.getMessage());
        } catch (SQLException e) { showDatabaseError(e); }
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
        if (input == null) return;
        try {
            int id = Integer.parseInt(input.trim());
            int confirm = JOptionPane.showConfirmDialog(this, "Delete member " + id + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
            memberService.removeMember(id);
            scheduleService.removeSchedulesForMember(id);
            recentActions.push("Deleted Member ID: " + id);
            outputArea.setText("Member deleted from MySQL.");
        } catch (NumberFormatException e) {
            outputArea.setText("Enter a valid numeric Member ID.");
        } catch (MemberNotFoundException e) { outputArea.setText(e.getMessage()); }
        catch (SQLException e) { showDatabaseError(e); }
    }

    private void addTrainer() {
        try {
            String idText = JOptionPane.showInputDialog(this, "Enter Trainer ID:");
            if (idText == null) return;
            int id = Integer.parseInt(idText.trim());
            String name = JOptionPane.showInputDialog(this, "Enter Trainer Name:");
            if (name == null || name.trim().isEmpty()) return;
            String specialization = JOptionPane.showInputDialog(this, "Enter Specialization:");
            if (specialization == null) return;
            String phone = JOptionPane.showInputDialog(this, "Enter Phone (optional, up to 10 digits):");
            if (phone == null) return;
            if (!phone.trim().isEmpty() && !phone.trim().matches("\\d{1,10}")) {
                outputArea.setText("Phone must contain up to 10 digits."); return;
            }
            Trainer trainer = new Trainer(id, name.trim(), specialization.trim(), phone.trim());
            trainerService.addTrainer(trainer);
            recentActions.push("Added Trainer: " + name.trim());
            outputArea.setText("Trainer added to MySQL successfully. ID: " + id);
        } catch (NumberFormatException e) { outputArea.setText("Trainer ID must be a valid number."); }
        catch (SQLException e) { showDatabaseError(e); }
    }

    private void displayTrainers() {
        try { trainerService.loadFromDatabase(); }
        catch (SQLException e) { showDatabaseError(e); return; }
        outputArea.setText("");
        if (trainerService.getTrainers().isEmpty()) { outputArea.setText("No trainers saved yet."); return; }
        for (Trainer trainer : trainerService.getTrainers()) {
            outputArea.append("ID: " + trainer.getTrainerId() + "\n");
            outputArea.append("Name: " + trainer.getName() + "\n");
            outputArea.append("Specialization: " + trainer.getSpecialization() + "\n");
            outputArea.append("Phone: " + trainer.getPhone() + "\n");
            outputArea.append("--------------------\n");
        }
    }

    private void updateTrainer() {
        try {
            String idText = JOptionPane.showInputDialog(this, "Enter Trainer ID to update:");
            if (idText == null) return;
            int id = Integer.parseInt(idText.trim());
            Trainer old = trainerService.getTrainerById(id);
            if (old == null) { outputArea.setText("Trainer ID not found."); return; }
            String name = JOptionPane.showInputDialog(this, "Name:", old.getName());
            if (name == null || name.trim().isEmpty()) return;
            String specialization = JOptionPane.showInputDialog(this, "Specialization:", old.getSpecialization());
            if (specialization == null) return;
            String phone = JOptionPane.showInputDialog(this, "Phone:", old.getPhone());
            if (phone == null) return;
            if (!phone.trim().isEmpty() && !phone.trim().matches("\\d{1,10}")) {
                outputArea.setText("Phone must contain up to 10 digits."); return;
            }
            boolean updated = trainerService.updateTrainer(new Trainer(id, name.trim(), specialization.trim(), phone.trim()));
            outputArea.setText(updated ? "Trainer updated in MySQL." : "Trainer ID not found.");
            if (updated) recentActions.push("Updated Trainer ID: " + id);
        } catch (NumberFormatException e) { outputArea.setText("Trainer ID must be a valid number."); }
        catch (SQLException e) { showDatabaseError(e); }
    }

    private void deleteTrainer() {
        String input = JOptionPane.showInputDialog(this, "Enter Trainer ID to delete:");
        if (input == null) return;
        try {
            int id = Integer.parseInt(input.trim());
            int confirm = JOptionPane.showConfirmDialog(this, "Delete trainer " + id + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
            if (trainerService.removeTrainer(id)) {
                scheduleService.removeSchedulesForTrainer(id);
                recentActions.push("Deleted Trainer ID: " + id);
                outputArea.setText("Trainer deleted from MySQL.");
            } else outputArea.setText("Trainer ID not found.");
        } catch (NumberFormatException e) { outputArea.setText("Enter a valid numeric Trainer ID."); }
        catch (SQLException e) { showDatabaseError(e); }
    }

    private void addSchedule() {
        String activity = JOptionPane.showInputDialog(this, "Enter Activity (e.g. Yoga):");
        if (activity == null || activity.trim().isEmpty()) return;
        String memberIdInput = JOptionPane.showInputDialog(this, "Enter an existing Member ID:");
        if (memberIdInput == null) return;
        String trainerIdInput = JOptionPane.showInputDialog(this, "Enter an existing Trainer ID:");
        if (trainerIdInput == null) return;
        String dateInput = JOptionPane.showInputDialog(this, "Enter date (DD-MM-YYYY):");
        if (dateInput == null) return;
        String timeInput = JOptionPane.showInputDialog(this, "Enter time (HH:MM, 24-hour):");
        if (timeInput == null) return;
        try {
            int memberId = Integer.parseInt(memberIdInput.trim());
            int trainerId = Integer.parseInt(trainerIdInput.trim());
            Member member = memberService.searchMember(memberId);
            Trainer trainer = trainerService.getTrainerById(trainerId);
            if (trainer == null) throw new IllegalArgumentException("Trainer ID " + trainerId + " was not found.");
            String date = LocalDate.parse(dateInput.trim(), DateTimeFormatter.ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT)).toString();
            String time = LocalTime.parse(timeInput.trim()).toString();
            FitnessSchedule schedule = new FitnessSchedule(0, memberId, trainerId,
                    member.getName(), trainer.getName(), activity.trim(), date, time);
            FitnessSchedule saved = scheduleService.addSchedule(schedule);
            recentActions.push("Added Schedule: " + saved.getActivity());
            outputArea.setText("Schedule saved to MySQL with ID " + saved.getScheduleId() + ".");
        } catch (NumberFormatException e) { outputArea.setText("Member ID and Trainer ID must be valid numbers."); }
        catch (MemberNotFoundException | IllegalArgumentException e) { outputArea.setText(e.getMessage()); }
        catch (SQLException e) { showDatabaseError(e); }
    }

    private void displaySchedules() {
        try { scheduleService.loadFromDatabase(); }
        catch (SQLException e) { showDatabaseError(e); return; }
        outputArea.setText("");
        if (scheduleService.getSchedules().isEmpty()) { outputArea.setText("No schedules saved yet."); return; }
        for (FitnessSchedule schedule : scheduleService.getSchedules()) {
            outputArea.append(formatSchedule(schedule));
            outputArea.append("--------------------\n");
        }
    }

    private void updateSchedule() {
        String input = JOptionPane.showInputDialog(this, "Enter Schedule ID to update:");
        if (input == null) return;
        try {
            int id = Integer.parseInt(input.trim());
            FitnessSchedule old = new dao.ScheduleDao().getScheduleById(id);
            if (old == null) { outputArea.setText("Schedule ID not found."); return; }
            String activity = JOptionPane.showInputDialog(this, "Activity:", old.getActivity());
            if (activity == null || activity.trim().isEmpty()) return;
            String dateDefault = LocalDate.parse(old.getDate()).format(DateTimeFormatter.ofPattern("dd-MM-uuuu"));
            String dateText = JOptionPane.showInputDialog(this, "Date (DD-MM-YYYY):", dateDefault);
            if (dateText == null) return;
            String timeText = JOptionPane.showInputDialog(this, "Time (HH:MM):", old.getTime());
            if (timeText == null) return;
            String date = LocalDate.parse(dateText.trim(), DateTimeFormatter.ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT)).toString();
            String time = LocalTime.parse(timeText.trim()).toString();
            FitnessSchedule updated = new FitnessSchedule(id, old.getMemberName(), old.getTrainerName(),
                    activity.trim(), date, time);
            if (new dao.ScheduleDao().updateSchedule(updated)) {
                scheduleService.loadFromDatabase();
                recentActions.push("Updated Schedule ID: " + id);
                outputArea.setText("Schedule updated in MySQL.");
            } else outputArea.setText("Schedule ID not found.");
        } catch (NumberFormatException e) { outputArea.setText("Schedule ID must be numeric."); }
        catch (java.time.format.DateTimeParseException e) { outputArea.setText("Enter a valid date (DD-MM-YYYY) and time (HH:MM)."); }
        catch (SQLException e) { showDatabaseError(e); }
    }

    private void deleteSchedule() {
        String input = JOptionPane.showInputDialog(this, "Enter Schedule ID to delete:");
        if (input == null) return;
        try {
            int id = Integer.parseInt(input.trim());
            int confirm = JOptionPane.showConfirmDialog(this, "Delete schedule " + id + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
            if (new dao.ScheduleDao().deleteSchedule(id)) {
                scheduleService.loadFromDatabase();
                recentActions.push("Deleted Schedule ID: " + id);
                outputArea.setText("Schedule deleted from MySQL.");
            } else outputArea.setText("Schedule ID not found.");
        } catch (NumberFormatException e) { outputArea.setText("Schedule ID must be numeric."); }
        catch (SQLException e) { showDatabaseError(e); }
    }

    private void addPlan() {
        try {
            String idText = JOptionPane.showInputDialog(this, "Plan ID:");
            if (idText == null) return;
            int id = Integer.parseInt(idText.trim());
            String name = JOptionPane.showInputDialog(this, "Plan Name:");
            if (name == null || name.trim().isEmpty()) return;
            String durationText = JOptionPane.showInputDialog(this, "Duration in months:");
            if (durationText == null) return;
            String priceText = JOptionPane.showInputDialog(this, "Price:");
            if (priceText == null) return;
            int duration = Integer.parseInt(durationText.trim());
            double price = Double.parseDouble(priceText.trim());
            if (duration <= 0 || price < 0) { outputArea.setText("Duration must be positive and price cannot be negative."); return; }
            if (planDao.addPlan(new MembershipPlan(id, name.trim(), duration, price))) {
                recentActions.push("Added Membership Plan: " + name.trim());
                outputArea.setText("Membership plan saved to MySQL.");
            }
        } catch (NumberFormatException e) { outputArea.setText("Plan ID, duration and price must be valid numbers."); }
        catch (SQLException e) { showDatabaseError(e); }
    }

    private void displayPlans() {
        try {
            java.util.List<MembershipPlan> plans = planDao.getAllPlans();
            outputArea.setText("");
            if (plans.isEmpty()) { outputArea.setText("No membership plans saved yet."); return; }
            for (MembershipPlan plan : plans) {
                outputArea.append("Plan ID: " + plan.getPlanId() + "\n");
                outputArea.append("Plan Name: " + plan.getPlanName() + "\n");
                outputArea.append("Duration: " + plan.getDuration() + " months\n");
                outputArea.append("Price: Rs. " + plan.getPrice() + "\n");
                outputArea.append("--------------------\n");
            }
        } catch (SQLException e) { showDatabaseError(e); }
    }

    private void updatePlan() {
        try {
            String idText = JOptionPane.showInputDialog(this, "Plan ID to update:");
            if (idText == null) return;
            int id = Integer.parseInt(idText.trim());
            MembershipPlan old = planDao.getPlanById(id);
            if (old == null) { outputArea.setText("Plan ID not found."); return; }
            String name = JOptionPane.showInputDialog(this, "Plan Name:", old.getPlanName());
            if (name == null || name.trim().isEmpty()) return;
            String durationText = JOptionPane.showInputDialog(this, "Duration in months:", old.getDuration());
            if (durationText == null) return;
            String priceText = JOptionPane.showInputDialog(this, "Price:", old.getPrice());
            if (priceText == null) return;
            int duration = Integer.parseInt(durationText.trim());
            double price = Double.parseDouble(priceText.trim());
            if (duration <= 0 || price < 0) { outputArea.setText("Duration must be positive and price cannot be negative."); return; }
            if (planDao.updatePlan(new MembershipPlan(id, name.trim(), duration, price))) {
                recentActions.push("Updated Membership Plan ID: " + id);
                outputArea.setText("Membership plan updated in MySQL.");
            } else outputArea.setText("Plan ID not found.");
        } catch (NumberFormatException e) { outputArea.setText("Plan ID, duration and price must be valid numbers."); }
        catch (SQLException e) { showDatabaseError(e); }
    }

    private void deletePlan() {
        String input = JOptionPane.showInputDialog(this, "Plan ID to delete:");
        if (input == null) return;
        try {
            int id = Integer.parseInt(input.trim());
            int confirm = JOptionPane.showConfirmDialog(this, "Delete membership plan " + id + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
            if (planDao.deletePlan(id)) {
                recentActions.push("Deleted Membership Plan ID: " + id);
                outputArea.setText("Membership plan deleted from MySQL.");
            } else outputArea.setText("Plan ID not found.");
        } catch (NumberFormatException e) { outputArea.setText("Plan ID must be numeric."); }
        catch (SQLException e) { showDatabaseError(e); }
    }

    private String formatSchedule(FitnessSchedule schedule) {
        return "Schedule ID: " + schedule.getScheduleId() + "\n"
                + "Member Name: " + schedule.getMemberName() + "\n"
                + "Trainer Name: " + schedule.getTrainerName() + "\n"
                + "Activity: " + schedule.getActivity() + "\n"
                + "Date: " + schedule.getDate() + "\n"
                + "Time: " + schedule.getTime() + "\n";
    }

    private String getMembershipStatus(Member member) {
        return member instanceof PremiumMember ? "Premium" : "Regular";
    }

    private void showDatabaseError(SQLException e) {
        outputArea.setText("Database operation failed: " + e.getMessage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    DatabaseSetup.initializeDatabase();
                    new MainFrame();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Could not connect to MySQL.\n" + e.getMessage(),
                            "Database startup failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
