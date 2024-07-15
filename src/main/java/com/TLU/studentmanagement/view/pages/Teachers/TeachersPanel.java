package main.java.com.TLU.studentmanagement.view.pages.Teachers;

import javax.swing.*;
import java.awt.*;

public class TeachersPanel extends JPanel {
    public TeachersPanel() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Thông tin giáo viên", JLabel.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
