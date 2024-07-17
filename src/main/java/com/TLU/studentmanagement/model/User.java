package main.java.com.TLU.studentmanagement.model;

public class User {
    private String id;
    private String fullname;
    private String msv;
    private String year;
    private String gvcn;
    private String gender;
    private String className;
    private String email;
    private String majorId;
    private boolean isAdmin;
    private boolean isGV;
    private boolean deleted;

    public User() {}

    public User(String fullname, String msv, String year, String gvcn, String gender, String className, String email, String majorId) {
        this.fullname = fullname;
        this.msv = msv;
        this.year = year;
        this.gvcn = gvcn;
        this.gender = gender;
        this.className = className;
        this.email = email;
        this.majorId = majorId;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullname;
    }

    public void setFullName(String fullname) {
        this.fullname = fullname;
    }

    public String getMsv() {
        return msv;
    }

    public void setMsv(String msv) {
        this.msv = msv;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGvcn() {
        return gvcn;
    }

    public void setGvcn(String gvcn) {
        this.gvcn = gvcn;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isGv() {
        return isGV;
    }

    public void setGv(boolean GV) {
        isGV = GV;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
