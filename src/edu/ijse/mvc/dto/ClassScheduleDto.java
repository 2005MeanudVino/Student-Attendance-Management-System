package edu.ijse.mvc.dto;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class ClassScheduleDto {
    private int id;
    private int courseId;
    private String subject;
    private int lecturerId;
    private Date classDate;

    public ClassScheduleDto() {}

    public ClassScheduleDto(int id, int courseId, String subject, int lecturerId, Date classDate) {
        this.id = id;
        this.courseId = courseId;
        this.subject = subject;
        this.lecturerId = lecturerId;
        this.classDate = classDate;
    }

    
    public int getId() { return id; }
    public int getCourseId() { return courseId; }
    public String getSubject() { return subject; }
    public int getLecturerId() { return lecturerId; }
    public Date getClassDate() { return classDate; }

    public void setId(int id) { this.id = id; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setLecturerId(int lecturerId) { this.lecturerId = lecturerId; }
    public void setClassDate(Date classDate) { this.classDate = classDate; }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = classDate != null ? sdf.format(classDate) : "No Date";
        return subject + " (" + dateStr + ")";
    }
}