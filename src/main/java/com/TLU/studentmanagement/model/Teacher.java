package main.java.com.TLU.studentmanagement.model;

public class Teacher {
    private String mgv;
    private String fullName;

    public Teacher(String mgv, String fullName) {
        this.mgv = mgv;
        this.fullName = fullName;
    }

    public String getMgv() {
        return mgv;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return fullName; // Hiển thị fullName trong combobox
    }
}
