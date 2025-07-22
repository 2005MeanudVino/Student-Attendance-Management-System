package edu.ijse.mvc.model;

import edu.ijse.mvc.db.DBConnection;
import edu.ijse.mvc.dto.LecturerDto;

import java.sql.*;
import java.util.ArrayList;

public class LecturerModel {

    public boolean addLecturer(LecturerDto lecturer) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO lecturers (employee_id, name, subject) VALUES (?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, lecturer.getEmployeeId());
        pst.setString(2, lecturer.getName());
        pst.setString(3, lecturer.getSubject());
        return pst.executeUpdate() > 0;
    }

    public boolean updateLecturer(LecturerDto lecturer) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "UPDATE lecturers SET employee_id=?, name=?, subject=? WHERE id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, lecturer.getEmployeeId());
        pst.setString(2, lecturer.getName());
        pst.setString(3, lecturer.getSubject());
        pst.setInt(4, lecturer.getId());
        return pst.executeUpdate() > 0;
    }

    public boolean deleteLecturer(int id) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM lecturers WHERE id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, id);
        return pst.executeUpdate() > 0;
    }

    public ArrayList<LecturerDto> getAllLecturers() throws SQLException, ClassNotFoundException {
        ArrayList<LecturerDto> list = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM lecturers";
        ResultSet rs = con.createStatement().executeQuery(sql);
        while (rs.next()) {
            LecturerDto lecturer = new LecturerDto(
                rs.getInt("id"),
                rs.getString("employee_id"),
                rs.getString("name"),
                rs.getString("subject")
            );
            list.add(lecturer);
        }
        return list;
    }
    
    public LecturerDto getLecturerById(int id) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM lecturers WHERE id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new LecturerDto(
                rs.getInt("id"),
                rs.getString("employee_id"),
                rs.getString("name"),
                rs.getString("subject")
            );
        }
        return null;
    }
}
