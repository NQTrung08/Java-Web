package main.java.com.TLU.studentmanagement.view.pages.Courses;

import javax.swing.*;
import java.awt.*;

public class CoursesPanel extends JPanel {
    public CoursesPanel() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Thông tin môn học", JLabel.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
