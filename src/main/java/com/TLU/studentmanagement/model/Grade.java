package main.java.com.TLU.studentmanagement.model;

public class Grade {
    private String id;
    private String studentId;
    private String studentName;
    private double midScore;
    private double finalScore;
    private double gpa;
    private String status;
    private String transcriptId;
    private String courseId;

    public Grade() {}

    public Grade(String courseId, double midScore, double finalScore, String transcriptId) {
        this.courseId = courseId;
        this.midScore = midScore;
        this.finalScore = finalScore;
        this.transcriptId = transcriptId;
    }

    public Grade(String id, String courseId, String transcriptId, double midScore, double finalScore) {
        this.id = id;
        this.courseId = courseId;
        this.midScore = midScore;
        this.finalScore = finalScore;
        this.transcriptId = transcriptId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public double getMidScore() {
        return midScore;
    }

    public void setMidScore(double midtermScore) {
        this.midScore = midtermScore;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTranscriptId() {
        return transcriptId;
    }

    public void setTranscriptId(String transcriptId) {
        this.transcriptId = transcriptId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
