package main.java.com.TLU.studentmanagement.view.pages.Majors;

import main.java.com.TLU.studentmanagement.model.Major;
import main.java.com.TLU.studentmanagement.session.UserSession;
import main.java.com.TLU.studentmanagement.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MajorPanel extends JPanel {

    private JButton addButton;
    private JPanel majorListPanel;

    public MajorPanel() {
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Thông tin chuyên ngành", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        addButton = new JButton("Thêm chuyên ngành");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (UserSession.getUser() != null && UserSession.getUser().isAdmin()) {
                    showAddMajorForm();
                } else {
                    JOptionPane.showMessageDialog(null, "Bạn không có quyền thực hiện thao tác này.");
                }
            }
        });
        add(addButton, BorderLayout.SOUTH);

        majorListPanel = new JPanel();
        majorListPanel.setLayout(new BoxLayout(majorListPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(majorListPanel), BorderLayout.CENTER);
    }

    private void showAddMajorForm() {
        JTextField nameField = new JTextField();
        JTextField codeField = new JTextField();

        Object[] message = {
                "Name:", nameField,
                "Code:", codeField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Thêm chuyên ngành", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String code = codeField.getText();

            JSONObject jsonInput = new JSONObject();
            jsonInput.put("name", name);
            jsonInput.put("code", code);

            try {
                String apiUrl = "http://localhost:8080/api/major/create";
                HttpUtil.sendPost(apiUrl, jsonInput.toString());
                loadData(); // Refresh the list after adding new major
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }

    private void loadData() {
        if (UserSession.getUser() != null && UserSession.getUser().isAdmin()) {
            try {
                String apiUrl = "http://localhost:8080/api/major/getAll";
                String response = HttpUtil.sendGet(apiUrl);
                JSONArray majorsArray = new JSONArray(response);

                List<Major> majors = new ArrayList<>();
                for (int i = 0; i < majorsArray.length(); i++) {
                    JSONObject majorObject = majorsArray.getJSONObject(i);
                    Major major = new Major();
                    major.setId(majorObject.getString("_id"));
                    major.setName(majorObject.getString("name"));
                    major.setCode(majorObject.getString("code"));
                    majors.add(major);
                }

                displayMajors(majors);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Bạn không có quyền xem thông tin này.");
        }
    }

    private void displayMajors(List<Major> majors) {
        majorListPanel.removeAll();

        for (Major major : majors) {
            JPanel majorPanel = new JPanel();
            majorPanel.setLayout(new BorderLayout());

            JLabel nameLabel = new JLabel("Name: " + major.getName());
            JLabel codeLabel = new JLabel("Code: " + major.getCode());

            majorPanel.add(nameLabel, BorderLayout.NORTH);
            majorPanel.add(codeLabel, BorderLayout.SOUTH);

            majorListPanel.add(majorPanel);
        }

        majorListPanel.revalidate();
        majorListPanel.repaint();
    }
}
