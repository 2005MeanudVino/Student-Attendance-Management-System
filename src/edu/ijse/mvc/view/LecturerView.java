package edu.ijse.mvc.view;

import edu.ijse.mvc.dto.LecturerDto;
import edu.ijse.mvc.model.LecturerModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class LecturerView extends JFrame {

    private JTextField txtEmployeeId, txtName, txtSubject;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable tblLecturers;
    private DefaultTableModel tableModel;

    private LecturerModel lecturerModel;
    private int selectedLecturerId = -1;

    public LecturerView() {
        lecturerModel = new LecturerModel();
        initComponents();
        loadLecturers();
    }

    private void initComponents() {
        setTitle("Lecturer Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblEmployeeId = new JLabel("Employee ID:");
        JLabel lblName = new JLabel("Name:");
        JLabel lblSubject = new JLabel("Subject:");

        txtEmployeeId = new JTextField(20);
        txtName = new JTextField(20);
        txtSubject = new JTextField(20);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lblEmployeeId, gbc);
        gbc.gridx = 1; formPanel.add(txtEmployeeId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lblName, gbc);
        gbc.gridx = 1; formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lblSubject, gbc);
        gbc.gridx = 1; formPanel.add(txtSubject, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(btnAdd, gbc);
        gbc.gridx = 1; formPanel.add(btnUpdate, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(btnDelete, gbc);
        gbc.gridx = 1; formPanel.add(btnClear, gbc);

        add(formPanel, BorderLayout.WEST);

        tableModel = new DefaultTableModel(new String[]{"ID", "Employee ID", "Name", "Subject"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tblLecturers = new JTable(tableModel);
        tblLecturers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tblLecturers);
        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addLecturer());
        btnUpdate.addActionListener(e -> updateLecturer());
        btnDelete.addActionListener(e -> deleteLecturer());
        btnClear.addActionListener(e -> clearForm());

        tblLecturers.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblLecturers.getSelectedRow();
                if (selectedRow != -1) {
                    selectedLecturerId = (int) tableModel.getValueAt(selectedRow, 0);
                    txtEmployeeId.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtName.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtSubject.setText(tableModel.getValueAt(selectedRow, 3).toString());
                }
            }
        });
    }

    private void loadLecturers() {
        try {
            ArrayList<LecturerDto> lecturers = lecturerModel.getAllLecturers();
            tableModel.setRowCount(0);
            for (LecturerDto l : lecturers) {
                tableModel.addRow(new Object[]{l.getId(), l.getEmployeeId(), l.getName(), l.getSubject()});
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error loading lecturers: " + ex.getMessage());
        }
    }

    private void addLecturer() {
        String empId = txtEmployeeId.getText().trim();
        String name = txtName.getText().trim();
        String subject = txtSubject.getText().trim();

        if (empId.isEmpty() || name.isEmpty() || subject.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            LecturerDto lecturer = new LecturerDto(0, empId, name, subject);
            if (lecturerModel.addLecturer(lecturer)) {
                JOptionPane.showMessageDialog(this, "Lecturer added successfully!");
                loadLecturers();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add lecturer.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateLecturer() {
        if (selectedLecturerId == -1) {
            JOptionPane.showMessageDialog(this, "Select a lecturer to update!");
            return;
        }

        String empId = txtEmployeeId.getText().trim();
        String name = txtName.getText().trim();
        String subject = txtSubject.getText().trim();

        if (empId.isEmpty() || name.isEmpty() || subject.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            LecturerDto lecturer = new LecturerDto(selectedLecturerId, empId, name, subject);
            if (lecturerModel.updateLecturer(lecturer)) {
                JOptionPane.showMessageDialog(this, "Lecturer updated successfully!");
                loadLecturers();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update lecturer.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteLecturer() {
        if (selectedLecturerId == -1) {
            JOptionPane.showMessageDialog(this, "Select a lecturer to delete!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this lecturer?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (lecturerModel.deleteLecturer(selectedLecturerId)) {
                JOptionPane.showMessageDialog(this, "Lecturer deleted successfully!");
                loadLecturers();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete lecturer.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearForm() {
        selectedLecturerId = -1;
        txtEmployeeId.setText("");
        txtName.setText("");
        txtSubject.setText("");
        tblLecturers.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LecturerView().setVisible(true);
        });
    }
}
