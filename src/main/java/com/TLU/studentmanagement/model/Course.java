package main.java.com.TLU.studentmanagement.model;

import java.util.Date;

public class Course {
    private String id;
    private boolean deleted;
    private String name;
    private String code;
    private int credit;
    private String majorId; // Thêm thuộc tính majorId
    private String createdAt;
    private String updatedAt;

    public Course() {}

    public Course(String id, boolean deleted, String name, String code, int credit, String majorId, String createdAt, String updatedAt) {
        this.id = id;
        this.deleted = deleted;
        this.name = name;
        this.code = code;
        this.credit = credit;
        this.majorId = majorId; // Thêm thuộc tính majorId
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Course(String id, String name, String code, int credit, String majorId, String createdAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.credit = credit;
        this.majorId = majorId; // Thêm thuộc tính majorId
        this.createdAt = createdAt;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getMajorId() { // Thêm phương thức getter cho majorId
        return majorId;
    }

    public void setMajorId(String majorId) { // Thêm phương thức setter cho majorId
        this.majorId = majorId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", deleted=" + deleted +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", credit=" + credit +
                ", majorId='" + majorId + '\'' + // Thêm majorId vào phương thức toString
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
