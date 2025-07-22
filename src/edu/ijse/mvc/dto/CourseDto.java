package edu.ijse.mvc.dto;

public class CourseDto {
    private int id;
    private String courseCode;
    private String courseName;
    private String subject;

    public CourseDto() {
    }

    public CourseDto(int id, String courseCode, String courseName, String subject) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.subject = subject;
    }

    public int getId() {
        return id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSubject() {
        return subject;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return courseName; 
    }
}
