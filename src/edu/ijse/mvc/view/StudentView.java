package edu.ijse.mvc.view;

import edu.ijse.mvc.dto.CourseDto;
import edu.ijse.mvc.dto.StudentDto;
import edu.ijse.mvc.model.CourseModel;
import edu.ijse.mvc.model.StudentModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentView extends JFrame {

    private JTextField txtRegNo, txtName, txtContact;
    private JComboBox<CourseDto> cmbCourses;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable tblStudents;
    private DefaultTableModel tableModel;

    private StudentModel studentModel;
    private CourseModel courseModel;
    private int selectedStudentId = -1;

    public StudentView() {
        studentModel = new StudentModel();
        courseModel = new CourseModel();
        initComponents();
        loadCourses();
        loadStudents();
    }

    private void initComponents() {
        setTitle("Student Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblRegNo = new JLabel("Registration No:");
        JLabel lblName = new JLabel("Name:");
        JLabel lblCourse = new JLabel("Course:");
        JLabel lblContact = new JLabel("Contact:");

        txtRegNo = new JTextField(20);
        txtName = new JTextField(20);
        txtContact = new JTextField(20);
        cmbCourses = new JComboBox<>();

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lblRegNo, gbc);
        gbc.gridx = 1; formPanel.add(txtRegNo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lblName, gbc);
        gbc.gridx = 1; formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lblCourse, gbc);
        gbc.gridx = 1; formPanel.add(cmbCourses, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(lblContact, gbc);
        gbc.gridx = 1; formPanel.add(txtContact, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(btnAdd, gbc);
        gbc.gridx = 1; formPanel.add(btnUpdate, gbc);

        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(btnDelete, gbc);
        gbc.gridx = 1; formPanel.add(btnClear, gbc);

        add(formPanel, BorderLayout.WEST);

        // Right table panel
        tableModel = new DefaultTableModel(new String[]{"ID", "Reg No", "Name", "Course", "Contact"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tblStudents = new JTable(tableModel);
        tblStudents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tblStudents);
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearForm());

        tblStudents.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblStudents.getSelectedRow();
                if (selectedRow != -1) {
                    selectedStudentId = (int) tableModel.getValueAt(selectedRow, 0);
                    txtRegNo.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtName.setText(tableModel.getValueAt(selectedRow, 2).toString());

                    
                    String courseName = tableModel.getValueAt(selectedRow, 3).toString();
                    for (int i = 0; i < cmbCourses.getItemCount(); i++) {
                        if (cmbCourses.getItemAt(i).getCourseName().equals(courseName)) {
                            cmbCourses.setSelectedIndex(i);
                            break;
                        }
                    }

                    txtContact.setText(tableModel.getValueAt(selectedRow, 4).toString());
                }
            }
        });
    }

    private void loadCourses() {
        try {
            ArrayList<CourseDto> courses = courseModel.getAllCourses();
            cmbCourses.removeAllItems();
            for (CourseDto c : courses) {
                cmbCourses.addItem(c);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + ex.getMessage());
        }
    }

    private void loadStudents() {
        try {
            ArrayList<StudentDto> students = studentModel.getAllStudents();
            tableModel.setRowCount(0);
            for (StudentDto s : students) {
                
                CourseDto course = courseModel.getCourseById(s.getCourseId());
                String courseName = course != null ? course.getCourseName() : "Unknown";

                tableModel.addRow(new Object[]{s.getId(), s.getRegNo(), s.getName(), courseName, s.getContact()});
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + ex.getMessage());
        }
    }

    private void addStudent() {
        String regNo = txtRegNo.getText().trim();
        String name = txtName.getText().trim();
        String contact = txtContact.getText().trim();
        CourseDto selectedCourse = (CourseDto) cmbCourses.getSelectedItem();

        if (regNo.isEmpty() || name.isEmpty() || selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields (Reg No, Name, Course)!");
            return;
        }

        try {
            StudentDto student = new StudentDto(0, regNo, name, selectedCourse.getId(), contact);
            if (studentModel.addStudent(student)) {
                JOptionPane.showMessageDialog(this, "Student added successfully!");
                loadStudents();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add student.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to update!");
            return;
        }

        String regNo = txtRegNo.getText().trim();
        String name = txtName.getText().trim();
        String contact = txtContact.getText().trim();
        CourseDto selectedCourse = (CourseDto) cmbCourses.getSelectedItem();

        if (regNo.isEmpty() || name.isEmpty() || selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields (Reg No, Name, Course)!");
            return;
        }

        try {
            StudentDto student = new StudentDto(selectedStudentId, regNo, name, selectedCourse.getId(), contact);
            if (studentModel.updateStudent(student)) {
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
                loadStudents();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update student.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to delete!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (studentModel.deleteStudent(selectedStudentId)) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                loadStudents();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete student.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearForm() {
        selectedStudentId = -1;
        txtRegNo.setText("");
        txtName.setText("");
        txtContact.setText("");
        cmbCourses.setSelectedIndex(-1);
        tblStudents.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentView().setVisible(true);
        });
    }
}
