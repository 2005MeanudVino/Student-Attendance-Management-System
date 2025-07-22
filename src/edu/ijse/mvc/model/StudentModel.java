package edu.ijse.mvc.model;

import edu.ijse.mvc.db.DBConnection;
import edu.ijse.mvc.dto.StudentDto;

import java.sql.*;
import java.util.ArrayList;

public class StudentModel {

    public boolean addStudent(StudentDto student) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO students (reg_no, name, course_id, contact) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, student.getRegNo());
        pst.setString(2, student.getName());
        pst.setInt(3, student.getCourseId());
        pst.setString(4, student.getContact());
        return pst.executeUpdate() > 0;
    }

    public boolean updateStudent(StudentDto student) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "UPDATE students SET reg_no=?, name=?, course_id=?, contact=? WHERE id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, student.getRegNo());
        pst.setString(2, student.getName());
        pst.setInt(3, student.getCourseId());
        pst.setString(4, student.getContact());
        pst.setInt(5, student.getId());
        return pst.executeUpdate() > 0;
    }

    public boolean deleteStudent(int id) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM students WHERE id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, id);
        return pst.executeUpdate() > 0;
    }

    public ArrayList<StudentDto> getAllStudents() throws SQLException, ClassNotFoundException {
        ArrayList<StudentDto> list = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM students";
        ResultSet rs = con.createStatement().executeQuery(sql);
        while (rs.next()) {
            StudentDto student = new StudentDto(
                rs.getInt("id"),
                rs.getString("reg_no"),
                rs.getString("name"),
                rs.getInt("course_id"),
                rs.getString("contact")
            );
            list.add(student);
        }
        return list;
    }
    
    public StudentDto getStudentById(int id) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM students WHERE id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new StudentDto(
                rs.getInt("id"),
                rs.getString("reg_no"),
                rs.getString("name"),
                rs.getInt("course_id"),
                rs.getString("contact")
            );
        }
        return null;
    }
}
