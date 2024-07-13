package main.java.com.TLU.studentmanagement.view.pages.Information;

import javax.swing.*;
import java.awt.*;

public class PersonalInfoPanel extends JPanel {
    public PersonalInfoPanel() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Thông tin cá nhân", JLabel.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
