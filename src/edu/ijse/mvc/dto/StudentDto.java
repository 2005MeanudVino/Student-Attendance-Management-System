package edu.ijse.mvc.dto;

public class StudentDto {
    private int id;
    private String regNo;
    private String name;
    private int courseId;
    private String contact;

    public StudentDto() {
    }

    public StudentDto(int id, String regNo, String name, int courseId, String contact) {
        this.id = id;
        this.regNo = regNo;
        this.name = name;
        this.courseId = courseId;
        this.contact = contact;
    }

    // Getters & setters
    public int getId() { return id; }
    public String getRegNo() { return regNo; }
    public String getName() { return name; }
    public int getCourseId() { return courseId; }
    public String getContact() { return contact; }

    public void setId(int id) { this.id = id; }
    public void setRegNo(String regNo) { this.regNo = regNo; }
    public void setName(String name) { this.name = name; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public void setContact(String contact) { this.contact = contact; }

    @Override
    public String toString() {
        return name;  // Shows student name in JComboBox
    }
}
