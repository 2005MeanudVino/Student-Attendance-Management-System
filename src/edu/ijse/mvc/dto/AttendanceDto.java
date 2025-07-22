package edu.ijse.mvc.dto;

public class AttendanceDto {
    private int id;
    private int classScheduleId;
    private int studentId;
    private char status;  // 'P' or 'A'

    public AttendanceDto() {}

    public AttendanceDto(int id, int classScheduleId, int studentId, char status) {
        this.id = id;
        this.classScheduleId = classScheduleId;
        this.studentId = studentId;
        this.status = status;
    }

    // Getters and setters
    public int getId() { return id; }
    public int getClassScheduleId() { return classScheduleId; }
    public int getStudentId() { return studentId; }
    public char getStatus() { return status; }

    public void setId(int id) { this.id = id; }
    public void setClassScheduleId(int classScheduleId) { this.classScheduleId = classScheduleId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public void setStatus(char status) { this.status = status; }
}
