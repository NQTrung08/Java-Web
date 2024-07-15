package main.java.com.TLU.studentmanagement.view.pages.Semesters;

import main.java.com.TLU.studentmanagement.controller.semesters.SemesterController;
import main.java.com.TLU.studentmanagement.model.Semester;

import javax.swing.*;

public class UpdateSemesterForm {

    public static void showUpdateSemesterForm(Semester semester) {
        JTextField semesterField = new JTextField(semester.getSemester());
        JTextField groupField = new JTextField(semester.getGroup());
        JTextField yearField = new JTextField(semester.getYear());

        Object[] message = {
                "Học kỳ:", semesterField,
                "Nhóm:", groupField,
                "Năm:", yearField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Cập nhật học kỳ", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String updatedSemester = semesterField.getText();
            String updatedGroup = groupField.getText();
            String updatedYear = yearField.getText();

            try {
                SemesterController.updateSemester(semester.getId(), updatedSemester, updatedGroup, updatedYear);
                // Refresh the semester list
                // You need to call a method from SemesterPanel to refresh the list
                // For example, you can use a callback or an event system
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }
}
