package edu.ijse.mvc.model;

import edu.ijse.mvc.db.DBConnection;
import edu.ijse.mvc.dto.ClassScheduleDto;

import java.sql.*;
import java.util.ArrayList;

public class ClassScheduleModel {

    public boolean addSchedule(ClassScheduleDto schedule) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO class_schedules (course_id, subject, lecturer_id, class_date) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, schedule.getCourseId());
        pst.setString(2, schedule.getSubject());
        pst.setInt(3, schedule.getLecturerId());
        pst.setDate(4, schedule.getClassDate());
        return pst.executeUpdate() > 0;
    }

    public boolean updateSchedule(ClassScheduleDto schedule) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "UPDATE class_schedules SET course_id=?, subject=?, lecturer_id=?, class_date=? WHERE id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, schedule.getCourseId());
        pst.setString(2, schedule.getSubject());
        pst.setInt(3, schedule.getLecturerId());
        pst.setDate(4, schedule.getClassDate());
        pst.setInt(5, schedule.getId());
        return pst.executeUpdate() > 0;
    }

    public boolean deleteSchedule(int id) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM class_schedules WHERE id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, id);
        return pst.executeUpdate() > 0;
    }

    public ArrayList<ClassScheduleDto> getAllSchedules() throws SQLException, ClassNotFoundException {
        ArrayList<ClassScheduleDto> list = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM class_schedules";
        ResultSet rs = con.createStatement().executeQuery(sql);
        while (rs.next()) {
            ClassScheduleDto schedule = new ClassScheduleDto(
                rs.getInt("id"),
                rs.getInt("course_id"),
                rs.getString("subject"),
                rs.getInt("lecturer_id"),
                rs.getDate("class_date")
            );
            list.add(schedule);
        }
        return list;
    }

    public ClassScheduleDto getScheduleById(int id) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM class_schedules WHERE id = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return new ClassScheduleDto(
                rs.getInt("id"),
                rs.getInt("course_id"),
                rs.getString("subject"),
                rs.getInt("lecturer_id"),
                rs.getDate("class_date")
            );
        }
        return null;
    }
}
