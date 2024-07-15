package main.java.com.TLU.studentmanagement.view.pages.Semesters;

import javax.swing.*;
import java.awt.*;

public class SemesterPanel extends JPanel {
    public SemesterPanel() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Thông tin Kỳ học", JLabel.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
