package edu.ijse.mvc.view;

import edu.ijse.mvc.dto.ClassScheduleDto;
import edu.ijse.mvc.dto.CourseDto;
import edu.ijse.mvc.dto.LecturerDto;
import edu.ijse.mvc.model.ClassScheduleModel;
import edu.ijse.mvc.model.CourseModel;
import edu.ijse.mvc.model.LecturerModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ClassScheduleView extends JFrame {

    private JComboBox<CourseDto> cmbCourse;
    private JTextField txtSubject;
    private JComboBox<LecturerDto> cmbLecturer;
    private JTextField txtDate;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable tblSchedules;
    private DefaultTableModel tableModel;

    private ClassScheduleModel scheduleModel;
    private CourseModel courseModel;
    private LecturerModel lecturerModel;

    private int selectedScheduleId = -1;

    public ClassScheduleView() {
        scheduleModel = new ClassScheduleModel();
        courseModel = new CourseModel();
        lecturerModel = new LecturerModel();
        initComponents();
        loadCourses();
        loadLecturers();
        loadSchedules();
    }

    private void initComponents() {
        setTitle("Class Scheduling");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblCourse = new JLabel("Course:");
        JLabel lblSubject = new JLabel("Subject:");
        JLabel lblLecturer = new JLabel("Lecturer:");
        JLabel lblDate = new JLabel("Date (YYYY-MM-DD):");

        cmbCourse = new JComboBox<>();
        txtSubject = new JTextField(20);
        cmbLecturer = new JComboBox<>();
        txtDate = new JTextField(10);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lblCourse, gbc);
        gbc.gridx = 1; formPanel.add(cmbCourse, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lblSubject, gbc);
        gbc.gridx = 1; formPanel.add(txtSubject, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lblLecturer, gbc);
        gbc.gridx = 1; formPanel.add(cmbLecturer, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(lblDate, gbc);
        gbc.gridx = 1; formPanel.add(txtDate, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(btnAdd, gbc);
        gbc.gridx = 1; formPanel.add(btnUpdate, gbc);

        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(btnDelete, gbc);
        gbc.gridx = 1; formPanel.add(btnClear, gbc);

        add(formPanel, BorderLayout.WEST);

        tableModel = new DefaultTableModel(new String[]{"ID", "Course", "Subject", "Lecturer", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblSchedules = new JTable(tableModel);
        tblSchedules.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tblSchedules);
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        btnAdd.addActionListener(e -> addSchedule());
        btnUpdate.addActionListener(e -> updateSchedule());
        btnDelete.addActionListener(e -> deleteSchedule());
        btnClear.addActionListener(e -> clearForm());

        tblSchedules.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblSchedules.getSelectedRow();
                if (selectedRow != -1) {
                    selectedScheduleId = (int) tableModel.getValueAt(selectedRow, 0);
                    // Set form values
                    String courseName = (String) tableModel.getValueAt(selectedRow, 1);
                    selectComboBoxItemByName(cmbCourse, courseName);
                    txtSubject.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    String lecturerName = (String) tableModel.getValueAt(selectedRow, 3);
                    selectComboBoxItemByName(cmbLecturer, lecturerName);
                    txtDate.setText(tableModel.getValueAt(selectedRow, 4).toString());
                }
            }
        });
    }

    private <T> void selectComboBoxItemByName(JComboBox<T> comboBox, String name) {
        for (int i=0; i < comboBox.getItemCount(); i++) {
            T item = comboBox.getItemAt(i);
            if (item.toString().equals(name)) {
                comboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    private void loadCourses() {
        try {
            ArrayList<CourseDto> courses = courseModel.getAllCourses();
            cmbCourse.removeAllItems();
            for (CourseDto c : courses) {
                cmbCourse.addItem(c);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + ex.getMessage());
        }
    }

    private void loadLecturers() {
        try {
            ArrayList<LecturerDto> lecturers = lecturerModel.getAllLecturers();
            cmbLecturer.removeAllItems();
            for (LecturerDto l : lecturers) {
                cmbLecturer.addItem(l);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading lecturers: " + ex.getMessage());
        }
    }

    private void loadSchedules() {
        try {
            ArrayList<ClassScheduleDto> schedules = scheduleModel.getAllSchedules();
            tableModel.setRowCount(0);
            for (ClassScheduleDto s : schedules) {
                // Get course and lecturer names
                CourseDto course = courseModel.getCourseById(s.getCourseId());
                LecturerDto lecturer = lecturerModel.getLecturerById(s.getLecturerId());
                String courseName = course != null ? course.getCourseName() : "Unknown";
                String lecturerName = lecturer != null ? lecturer.getName() : "Unknown";

                tableModel.addRow(new Object[]{
                    s.getId(),
                    courseName,
                    s.getSubject(),
                    lecturerName,
                    s.getClassDate().toString()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading schedules: " + ex.getMessage());
        }
    }

    private void addSchedule() {
        CourseDto selectedCourse = (CourseDto) cmbCourse.getSelectedItem();
        LecturerDto selectedLecturer = (LecturerDto) cmbLecturer.getSelectedItem();
        String subject = txtSubject.getText().trim();
        String dateStr = txtDate.getText().trim();

        if (selectedCourse == null || selectedLecturer == null || subject.isEmpty() || dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            Date classDate = Date.valueOf(dateStr);  // format YYYY-MM-DD
            ClassScheduleDto schedule = new ClassScheduleDto(0, selectedCourse.getId(), subject, selectedLecturer.getId(), classDate);
            if (scheduleModel.addSchedule(schedule)) {
                JOptionPane.showMessageDialog(this, "Schedule added!");
                loadSchedules();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add schedule.");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateSchedule() {
        if (selectedScheduleId == -1) {
            JOptionPane.showMessageDialog(this, "Select a schedule to update!");
            return;
        }

        CourseDto selectedCourse = (CourseDto) cmbCourse.getSelectedItem();
        LecturerDto selectedLecturer = (LecturerDto) cmbLecturer.getSelectedItem();
        String subject = txtSubject.getText().trim();
        String dateStr = txtDate.getText().trim();

        if (selectedCourse == null || selectedLecturer == null || subject.isEmpty() || dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            Date classDate = Date.valueOf(dateStr);
            ClassScheduleDto schedule = new ClassScheduleDto(selectedScheduleId, selectedCourse.getId(), subject, selectedLecturer.getId(), classDate);
            if (scheduleModel.updateSchedule(schedule)) {
                JOptionPane.showMessageDialog(this, "Schedule updated!");
                loadSchedules();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update schedule.");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSchedule() {
        if (selectedScheduleId == -1) {
            JOptionPane.showMessageDialog(this, "Select a schedule to delete!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this schedule?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            if (scheduleModel.deleteSchedule(selectedScheduleId)) {
                JOptionPane.showMessageDialog(this, "Schedule deleted!");
                loadSchedules();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete schedule.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearForm() {
        selectedScheduleId = -1;
        cmbCourse.setSelectedIndex(-1);
        txtSubject.setText("");
        cmbLecturer.setSelectedIndex(-1);
        txtDate.setText("");
        tblSchedules.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClassScheduleView().setVisible(true));
    }
}
