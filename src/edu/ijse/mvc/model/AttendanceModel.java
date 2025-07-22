package edu.ijse.mvc.model;

import edu.ijse.mvc.db.DBConnection;
import edu.ijse.mvc.dto.AttendanceDto;

import java.sql.*;
import java.util.ArrayList;

public class AttendanceModel {

    public boolean markAttendance(AttendanceDto attendance) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        // Check if attendance record exists, update if yes, insert if no
        String checkSql = "SELECT id FROM attendance WHERE class_schedule_id=? AND student_id=?";
        PreparedStatement checkPst = con.prepareStatement(checkSql);
        checkPst.setInt(1, attendance.getClassScheduleId());
        checkPst.setInt(2, attendance.getStudentId());
        ResultSet rs = checkPst.executeQuery();
        if (rs.next()) {
            // Update existing record
            String updateSql = "UPDATE attendance SET status=? WHERE id=?";
            PreparedStatement updatePst = con.prepareStatement(updateSql);
            updatePst.setString(1, String.valueOf(attendance.getStatus()));
            updatePst.setInt(2, rs.getInt("id"));
            return updatePst.executeUpdate() > 0;
        } else {
            // Insert new record
            String insertSql = "INSERT INTO attendance (class_schedule_id, student_id, status) VALUES (?, ?, ?)";
            PreparedStatement insertPst = con.prepareStatement(insertSql);
            insertPst.setInt(1, attendance.getClassScheduleId());
            insertPst.setInt(2, attendance.getStudentId());
            insertPst.setString(3, String.valueOf(attendance.getStatus()));
            return insertPst.executeUpdate() > 0;
        }
    }

    public ArrayList<AttendanceDto> getAttendanceBySchedule(int classScheduleId) throws SQLException, ClassNotFoundException {
        ArrayList<AttendanceDto> list = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM attendance WHERE class_schedule_id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, classScheduleId);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            AttendanceDto attendance = new AttendanceDto(
                rs.getInt("id"),
                rs.getInt("class_schedule_id"),
                rs.getInt("student_id"),
                rs.getString("status").charAt(0)
            );
            list.add(attendance);
        }
        return list;
    }
    
    // New method for attendance reporting with flexible filters
    public ArrayList<AttendanceDto> getAttendanceReport(
        Integer studentId,       // nullable filter
        Integer courseId,        // nullable filter
        java.sql.Date fromDate,  // nullable filter
        java.sql.Date toDate     // nullable filter
    ) throws SQLException, ClassNotFoundException {

        ArrayList<AttendanceDto> list = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();

        StringBuilder sql = new StringBuilder(
            "SELECT a.* FROM attendance a " +
            "JOIN class_schedules cs ON a.class_schedule_id = cs.id " +
            "WHERE 1=1 "
        );

        if (studentId != null) sql.append("AND a.student_id = ? ");
        if (courseId != null) sql.append("AND cs.course_id = ? ");
        if (fromDate != null) sql.append("AND cs.date >= ? ");
        if (toDate != null) sql.append("AND cs.date <= ? ");

        PreparedStatement pst = con.prepareStatement(sql.toString());

        int index = 1;
        if (studentId != null) pst.setInt(index++, studentId);
        if (courseId != null) pst.setInt(index++, courseId);
        if (fromDate != null) pst.setDate(index++, fromDate);
        if (toDate != null) pst.setDate(index++, toDate);

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            AttendanceDto attendance = new AttendanceDto(
                rs.getInt("id"),
                rs.getInt("class_schedule_id"),
                rs.getInt("student_id"),
                rs.getString("status").charAt(0)
            );
            list.add(attendance);
        }
        return list;
    }
}
