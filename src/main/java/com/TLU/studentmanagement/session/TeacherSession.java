package main.java.com.TLU.studentmanagement.session;

import main.java.com.TLU.studentmanagement.model.Teacher;

public class TeacherSession {
    private static Teacher teacher;

    public static Teacher getTeacher() {
        return teacher;
    }

    public static void setTeacher(Teacher teacher) {
        TeacherSession.teacher = teacher;
    }

    public static void clear() {
        teacher = null;
    }
}
