package main.java.com.TLU.studentmanagement.view.pages.Student;

import javax.swing.*;
import java.awt.*;

public class StudentsPanel extends JPanel {
    public StudentsPanel() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Thông tin sinh viên", JLabel.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
