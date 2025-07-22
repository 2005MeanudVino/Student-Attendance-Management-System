package edu.ijse.mvc.view;

import edu.ijse.mvc.dto.CourseDto;
import edu.ijse.mvc.model.CourseModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class CourseView extends JFrame {

    private JTextField txtCourseCode, txtCourseName, txtSubject;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable tblCourses;
    private DefaultTableModel tableModel;

    private CourseModel courseModel;
    private int selectedCourseId = -1;

    public CourseView() {
        courseModel = new CourseModel();
        initComponents();
        loadCourses();
    }

    private void initComponents() {
        setTitle("Course Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblCourseCode = new JLabel("Course Code:");
        JLabel lblCourseName = new JLabel("Course Name:");
        JLabel lblSubject = new JLabel("Subject:");

        txtCourseCode = new JTextField(20);
        txtCourseName = new JTextField(20);
        txtSubject = new JTextField(20);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblCourseCode, gbc);
        gbc.gridx = 1;
        formPanel.add(txtCourseCode, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblCourseName, gbc);
        gbc.gridx = 1;
        formPanel.add(txtCourseName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblSubject, gbc);
        gbc.gridx = 1;
        formPanel.add(txtSubject, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(btnAdd, gbc);
        gbc.gridx = 1;
        formPanel.add(btnUpdate, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(btnDelete, gbc);
        gbc.gridx = 1;
        formPanel.add(btnClear, gbc);

        add(formPanel, BorderLayout.WEST);

        // Table panel
        tableModel = new DefaultTableModel(new String[]{"ID", "Course Code", "Course Name", "Subject"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // disable editing in table
            }
        };

        tblCourses = new JTable(tableModel);
        tblCourses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tblCourses);
        add(scrollPane, BorderLayout.CENTER);

        // Event listeners
        btnAdd.addActionListener(e -> addCourse());
        btnUpdate.addActionListener(e -> updateCourse());
        btnDelete.addActionListener(e -> deleteCourse());
        btnClear.addActionListener(e -> clearForm());

        tblCourses.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblCourses.getSelectedRow();
                if (selectedRow != -1) {
                    selectedCourseId = (int) tableModel.getValueAt(selectedRow, 0);
                    txtCourseCode.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtCourseName.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtSubject.setText(tableModel.getValueAt(selectedRow, 3).toString());
                }
            }
        });
    }

    private void loadCourses() {
        try {
            ArrayList<CourseDto> courses = courseModel.getAllCourses();
            tableModel.setRowCount(0);
            for (CourseDto c : courses) {
                tableModel.addRow(new Object[]{c.getId(), c.getCourseCode(), c.getCourseName(), c.getSubject()});
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + ex.getMessage());
        }
    }

    private void addCourse() {
        String code = txtCourseCode.getText().trim();
        String name = txtCourseName.getText().trim();
        String subject = txtSubject.getText().trim();

        if (code.isEmpty() || name.isEmpty() || subject.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            CourseDto course = new CourseDto(0, code, name, subject);
            if (courseModel.addCourse(course)) {
                JOptionPane.showMessageDialog(this, "Course added successfully!");
                loadCourses();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add course.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateCourse() {
        if (selectedCourseId == -1) {
            JOptionPane.showMessageDialog(this, "Select a course to update!");
            return;
        }

        String code = txtCourseCode.getText().trim();
        String name = txtCourseName.getText().trim();
        String subject = txtSubject.getText().trim();

        if (code.isEmpty() || name.isEmpty() || subject.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            CourseDto course = new CourseDto(selectedCourseId, code, name, subject);
            if (courseModel.updateCourse(course)) {
                JOptionPane.showMessageDialog(this, "Course updated successfully!");
                loadCourses();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update course.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteCourse() {
        if (selectedCourseId == -1) {
            JOptionPane.showMessageDialog(this, "Select a course to delete!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this course?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (courseModel.deleteCourse(selectedCourseId)) {
                JOptionPane.showMessageDialog(this, "Course deleted successfully!");
                loadCourses();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete course.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearForm() {
        selectedCourseId = -1;
        txtCourseCode.setText("");
        txtCourseName.setText("");
        txtSubject.setText("");
        tblCourses.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CourseView().setVisible(true);
        });
    }
}
