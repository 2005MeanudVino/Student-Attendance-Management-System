package edu.ijse.mvc.view;

import edu.ijse.mvc.dto.AttendanceDto;
import edu.ijse.mvc.dto.ClassScheduleDto;
import edu.ijse.mvc.dto.CourseDto;
import edu.ijse.mvc.dto.StudentDto;
import edu.ijse.mvc.model.AttendanceModel;
import edu.ijse.mvc.model.ClassScheduleModel;
import edu.ijse.mvc.model.CourseModel;
import edu.ijse.mvc.model.StudentModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AttendanceReportView extends JFrame {

    private JComboBox<StudentDto> cmbStudent;
    private JComboBox<CourseDto> cmbCourse;
    private JTextField txtFromDate;
    private JTextField txtToDate;
    private JButton btnGenerate, btnExport;
    private JTable tblReport;
    private DefaultTableModel tableModel;

    private AttendanceModel attendanceModel;
    private StudentModel studentModel;
    private CourseModel courseModel;

    private ArrayList<AttendanceDto> currentReportData = new ArrayList<>();

    public AttendanceReportView() {
        attendanceModel = new AttendanceModel();
        studentModel = new StudentModel();
        courseModel = new CourseModel();

        initComponents();
        loadStudents();
        loadCourses();

        setTitle("Attendance Reports");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel filterPanel = new JPanel(new FlowLayout());

        cmbStudent = new JComboBox<>();
        filterPanel.add(new JLabel("Student:"));
        filterPanel.add(cmbStudent);

        cmbCourse = new JComboBox<>();
        filterPanel.add(new JLabel("Course:"));
        filterPanel.add(cmbCourse);

        txtFromDate = new JTextField(10);
        txtToDate = new JTextField(10);
        filterPanel.add(new JLabel("From (yyyy-mm-dd):"));
        filterPanel.add(txtFromDate);
        filterPanel.add(new JLabel("To (yyyy-mm-dd):"));
        filterPanel.add(txtToDate);

        btnGenerate = new JButton("Generate Report");
        filterPanel.add(btnGenerate);

        btnExport = new JButton("Export CSV");
        filterPanel.add(btnExport);
        btnExport.setEnabled(false);

        add(filterPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Student ID", "Student Name", "Course", "Date", "Status"}, 0);
        tblReport = new JTable(tableModel);
        add(new JScrollPane(tblReport), BorderLayout.CENTER);

        btnGenerate.addActionListener(e -> generateReport());
        btnExport.addActionListener(e -> exportCsv());
    }

    private void loadStudents() {
        try {
            cmbStudent.removeAllItems();
            cmbStudent.addItem(null); 
            for (StudentDto s : studentModel.getAllStudents()) {
                cmbStudent.addItem(s);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load students");
        }
    }

    private void loadCourses() {
        try {
            cmbCourse.removeAllItems();
            cmbCourse.addItem(null); 
            for (CourseDto c : courseModel.getAllCourses()) {
                cmbCourse.addItem(c);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load courses");
        }
    }

    private void generateReport() {
        Integer studentId = null;
        Integer courseId = null;
        Date fromDate = null;
        Date toDate = null;

        try {
            if (cmbStudent.getSelectedItem() != null) {
                studentId = ((StudentDto) cmbStudent.getSelectedItem()).getId();
            }
            if (cmbCourse.getSelectedItem() != null) {
                courseId = ((CourseDto) cmbCourse.getSelectedItem()).getId();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (!txtFromDate.getText().trim().isEmpty()) {
                java.util.Date d = sdf.parse(txtFromDate.getText().trim());
                fromDate = new Date(d.getTime());
            }
            if (!txtToDate.getText().trim().isEmpty()) {
                java.util.Date d = sdf.parse(txtToDate.getText().trim());
                toDate = new Date(d.getTime());
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd.");
            return;
        }

        try {
            currentReportData = attendanceModel.getAttendanceReport(studentId, courseId, fromDate, toDate);
            populateTable();
            btnExport.setEnabled(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + ex.getMessage());
        }
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        for (AttendanceDto a : currentReportData) {
            try {
                StudentDto student = studentModel.getStudentById(a.getStudentId());
                ClassScheduleDto schedule = new ClassScheduleModel().getScheduleById(a.getClassScheduleId());

                String status = (a.getStatus() == 'P') ? "Present" : "Absent";

                
                String courseName = new CourseModel().getCourseById(schedule.getCourseId()).getCourseName();

                tableModel.addRow(new Object[]{
                    student.getId(),
                    student.getName(),
                    courseName,
                    schedule.getClassDate().toString(),
                    status
                });
            } catch (Exception e) {
                
            }
        }
    }

    private void exportCsv() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter fw = new FileWriter(fileChooser.getSelectedFile())) {
                fw.write("Student ID,Student Name,Course,Date,Status\n");
                for (AttendanceDto a : currentReportData) {
                    StudentDto student = studentModel.getStudentById(a.getStudentId());
                    ClassScheduleDto schedule = new ClassScheduleModel().getScheduleById(a.getClassScheduleId());
                    String status = (a.getStatus() == 'P') ? "Present" : "Absent";

                    String courseName = new CourseModel().getCourseById(schedule.getCourseId()).getCourseName();

                    fw.write(String.format("%d,%s,%s,%s,%s\n",
                            student.getId(),
                            student.getName(),
                            courseName,
                            schedule.getClassDate().toString(),
                            status));
                }
                JOptionPane.showMessageDialog(this, "Report exported!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Export failed: " + e.getMessage());
            }
        }
    }
}
