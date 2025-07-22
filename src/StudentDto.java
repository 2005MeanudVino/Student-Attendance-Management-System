package edu.ijse.mvc.dto;

public class StudentDto {
    private int id;
    private String name;
    private String regNo;
    private String contact;
    private int courseId;

    public StudentDto(int id, String name, String regNo, String contact, int courseId) {
        this.id = id;
        this.name = name;
        this.regNo = regNo;
        this.contact = contact;
        this.courseId = courseId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getContact() {
        return contact;
    }

    public int getCourseId() {
        return courseId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
