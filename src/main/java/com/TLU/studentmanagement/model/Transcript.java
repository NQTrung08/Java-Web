package main.java.com.TLU.studentmanagement.model;

import java.util.List;

public class Transcript {
    private String id;
    private String studentId;
    private String semesterId;
    private boolean deleted;
    private List<String> grades;
    private String studentName;
    private String semesterName;

    public Transcript() {}

    public Transcript(String studentId, String semesterId) {
        this.studentId = studentId;
        this.semesterId = semesterId;
    }

    public String getId() {
        return id;
    }

    public void setId(String _id) {
        this.id = _id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<String> getGrades() {
        return grades;
    }

    public void setGrades(List<String> grades) {
        this.grades = grades;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    @Override
    public String toString() {
        return "Transcript{" +
                "studentId='" + studentId + '\'' +
                ", semesterId='" + semesterId + '\'' +
                '}';
    }

}
