package main.java.com.TLU.studentmanagement.model;

public class User {
    private String id;
    private String fullname;
    private String msv;
    private String major;
    private String year;
    private String email;
    private String gender;
    private boolean isAdmin;
    private boolean isGv;
    private String majorId;
    private String className;
    private String gvcn;
    private String gvcnId;

    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setGvcn(String gvcn) {
        this.gvcn = gvcn;
    }

    public String getGvcn() {
        return gvcn;
    }

    public String getGvcnId() {
        return gvcnId;
    }

    public void setGvcnId(String gvcnId) {
        this.gvcnId = gvcnId;
    }

    public boolean isGv() {
        return isGv;
    }

    public void setGv(boolean gv) {
        isGv = gv;
    }

    public User(String id, String fullname, String msv, String major, String year, String email, String gender) {
        this.id = id;
        this.fullname = fullname;
        this.msv = msv;
        this.major = major;
        this.year = year;
        this.email = email;
        this.gender = gender;
    }

    public User(String id, String fullname, String msv, String major, String year, String email, String gender, String gvcn, String className) {
        this.id = id;
        this.fullname = fullname;
        this.msv = msv;
        this.major = major;
        this.year = year;
        this.email = email;
        this.gender = gender;
        this.gvcn = gvcn;
        this.className = className;
    }

    public User() {}

    // Getters and setters
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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }



    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", fullname='" + fullname + '\'' +
                ", msv='" + msv + '\'' +
                ", major='" + major + '\'' +
                ", year='" + year + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

}