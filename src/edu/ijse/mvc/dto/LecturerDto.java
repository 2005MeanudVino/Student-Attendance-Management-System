package edu.ijse.mvc.dto;

public class LecturerDto {
    private int id;
    private String employeeId;
    private String name;
    private String subject;

    public LecturerDto() {
    }

    public LecturerDto(int id, String employeeId, String name, String subject) {
        this.id = id;
        this.employeeId = employeeId;
        this.name = name;
        this.subject = subject;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public String getSubject() { return subject; }

    public void setId(int id) { this.id = id; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setName(String name) { this.name = name; }
    public void setSubject(String subject) { this.subject = subject; }

    @Override
    public String toString() {
        return name;  // Shows lecturer name in JComboBox
    }
}
