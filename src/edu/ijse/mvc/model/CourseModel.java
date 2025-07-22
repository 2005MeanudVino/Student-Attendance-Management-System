package edu.ijse.mvc.model;

import edu.ijse.mvc.db.DBConnection;
import edu.ijse.mvc.dto.CourseDto;

import java.sql.*;
import java.util.ArrayList;

public class CourseModel {

    public boolean addCourse(CourseDto course) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO courses (course_code, course_name, subject) VALUES (?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, course.getCourseCode());
        pst.setString(2, course.getCourseName());
        pst.setString(3, course.getSubject());
        return pst.executeUpdate() > 0;
    }

    public boolean updateCourse(CourseDto course) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "UPDATE courses SET course_code=?, course_name=?, subject=? WHERE id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, course.getCourseCode());
        pst.setString(2, course.getCourseName());
        pst.setString(3, course.getSubject());
        pst.setInt(4, course.getId());
        return pst.executeUpdate() > 0;
    }

    public boolean deleteCourse(int id) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM courses WHERE id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, id);
        return pst.executeUpdate() > 0;
    }

    public ArrayList<CourseDto> getAllCourses() throws SQLException, ClassNotFoundException {
        ArrayList<CourseDto> list = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM courses";
        ResultSet rs = con.createStatement().executeQuery(sql);
        while (rs.next()) {
            CourseDto course = new CourseDto(
                rs.getInt("id"),
                rs.getString("course_code"),
                rs.getString("course_name"),
                rs.getString("subject")
            );
            list.add(course);
        }
        return list;
    }

    public CourseDto getCourseById(int id) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM courses WHERE id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new CourseDto(
                rs.getInt("id"),
                rs.getString("course_code"),
                rs.getString("course_name"),
                rs.getString("subject")
            );
        }
        return null;
    }
}
