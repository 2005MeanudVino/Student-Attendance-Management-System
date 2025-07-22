package edu.ijse.mvc.view;

import javax.swing.*;

public class AdminView extends JFrame {

    public AdminView() {
        setTitle("Admin Dashboard - SAMS");
        setSize(400, 350);  
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JButton btnStudents = new JButton("Manage Students");
        btnStudents.setBounds(100, 30, 200, 30);
        add(btnStudents);

        JButton btnLecturers = new JButton("Manage Lecturers");
        btnLecturers.setBounds(100, 70, 200, 30);
        add(btnLecturers);

        JButton btnCourses = new JButton("Manage Courses");
        btnCourses.setBounds(100, 110, 200, 30);
        add(btnCourses);

        JButton btnClassSchedule = new JButton("Class Scheduling"); // New button
        btnClassSchedule.setBounds(100, 150, 200, 30);
        add(btnClassSchedule);

        JButton btnAttendance = new JButton("View Attendance");
        btnAttendance.setBounds(100, 190, 200, 30);
        add(btnAttendance);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(150, 240, 100, 30);
        add(btnLogout);

        // Open Student Management window
        btnStudents.addActionListener(e -> {
            StudentView studentView = new StudentView();
            studentView.setVisible(true);
            this.dispose();
        });

        
        btnLecturers.addActionListener(e -> {
            LecturerView lecturerView = new LecturerView();
            lecturerView.setVisible(true);
            this.dispose();
        });

       
        btnCourses.addActionListener(e -> {
            CourseView courseView = new CourseView();
            courseView.setVisible(true);
            this.dispose();
        });

        // Open Class Scheduling window
        btnClassSchedule.addActionListener(e -> {
            ClassScheduleView scheduleView = new ClassScheduleView();
            scheduleView.setVisible(true);
            this.dispose();
        });

        
        btnAttendance.addActionListener(e -> {
            AttendanceReportView attendanceReportView = new AttendanceReportView();
            attendanceReportView.setVisible(true);
            this.dispose();
        });

   
        btnLogout.addActionListener(e -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
            this.dispose();
        });

        setLocationRelativeTo(null); 
        setVisible(true);
    }
}
