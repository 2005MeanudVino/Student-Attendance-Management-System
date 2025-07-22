package edu.ijse.mvc.view;

import javax.swing.*;

public class LecturerMenu extends JFrame {

    public LecturerMenu() {
        setTitle("Lecturer Dashboard");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JButton btnAttendance = new JButton("Mark Attendance");
        btnAttendance.setBounds(100, 50, 200, 40);
        add(btnAttendance);

        //JButton btnProfile = new JButton("My Profile");
        //btnProfile.setBounds(100, 110, 200, 40);
        //add(btnProfile);

        btnAttendance.addActionListener(e -> new AttendanceView());

        //btnProfile.addActionListener(e -> JOptionPane.showMessageDialog(this, "Profile Page Coming Soon!"));

        setLocationRelativeTo(null);

        
        setVisible(true);
    }
}
