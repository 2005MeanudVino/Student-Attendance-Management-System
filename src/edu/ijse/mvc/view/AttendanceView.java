package edu.ijse.mvc.view;

import edu.ijse.mvc.dto.AttendanceDto;
import edu.ijse.mvc.dto.ClassScheduleDto;
import edu.ijse.mvc.dto.StudentDto;
import edu.ijse.mvc.model.AttendanceModel;
import edu.ijse.mvc.model.ClassScheduleModel;
import edu.ijse.mvc.model.StudentModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class AttendanceView extends JFrame {

    private JComboBox<ClassScheduleDto> cmbClassSchedule;
    private JTable tblStudents;
    private DefaultTableModel tableModel;
    private JButton btnSave;

    private AttendanceModel attendanceModel;
    private ClassScheduleModel scheduleModel;
    private StudentModel studentModel;

    private HashMap<Integer, Character> attendanceMap = new HashMap<>(); 

    public AttendanceView() {
        attendanceModel = new AttendanceModel();
        scheduleModel = new ClassScheduleModel();
        studentModel = new StudentModel();
        initComponents();
        loadClassSchedules();

        
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Attendance Marking");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Class:"));
        cmbClassSchedule = new JComboBox<>();
        topPanel.add(cmbClassSchedule);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Student ID", "Name", "Present"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) return Boolean.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 2; 
            }
        };
        tblStudents = new JTable(tableModel);
        add(new JScrollPane(tblStudents), BorderLayout.CENTER);

        btnSave = new JButton("Save Attendance");
        add(btnSave, BorderLayout.SOUTH);

        cmbClassSchedule.addActionListener(e -> loadStudentsForSchedule());

        btnSave.addActionListener(e -> saveAttendance());
    }

    private void loadClassSchedules() {
        try {
            ArrayList<ClassScheduleDto> schedules = scheduleModel.getAllSchedules();
            cmbClassSchedule.removeAllItems();
            for (ClassScheduleDto s : schedules) {
                cmbClassSchedule.addItem(s);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error loading schedules: " + ex.getMessage());
        }
    }

    private void loadStudentsForSchedule() {
        ClassScheduleDto selectedSchedule = (ClassScheduleDto) cmbClassSchedule.getSelectedItem();
        if (selectedSchedule == null) return;

        try {
            
            ArrayList<StudentDto> students = studentModel.getAllStudents();

            
            tableModel.setRowCount(0);
            attendanceMap.clear();

            
            ArrayList<AttendanceDto> attendanceList = attendanceModel.getAttendanceBySchedule(selectedSchedule.getId());
            HashMap<Integer, Character> existingAttendance = new HashMap<>();
            for (AttendanceDto a : attendanceList) {
                existingAttendance.put(a.getStudentId(), a.getStatus());
            }

            for (StudentDto student : students) {
                char status = existingAttendance.getOrDefault(student.getId(), 'A'); // default absent
                boolean present = status == 'P';
                attendanceMap.put(student.getId(), status);
                tableModel.addRow(new Object[]{student.getId(), student.getName(), present});
            }

        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + ex.getMessage());
        }
    }

    private void saveAttendance() {
        ClassScheduleDto selectedSchedule = (ClassScheduleDto) cmbClassSchedule.getSelectedItem();
        if (selectedSchedule == null) {
            JOptionPane.showMessageDialog(this, "Select a class schedule first!");
            return;
        }

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int studentId = (int) tableModel.getValueAt(i, 0);
            boolean present = (boolean) tableModel.getValueAt(i, 2);
            char status = present ? 'P' : 'A';

            AttendanceDto attendance = new AttendanceDto(0, selectedSchedule.getId(), studentId, status);
            try {
                attendanceModel.markAttendance(attendance);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to save attendance for student ID " + studentId);
            }
        }

        JOptionPane.showMessageDialog(this, "Attendance saved!");
    }
}
