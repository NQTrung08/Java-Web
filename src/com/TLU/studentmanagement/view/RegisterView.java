package com.TLU.studentmanagement.view;

import javax.swing.*;
import java.awt.*;
import com.toedter.calendar.JDateChooser;
import java.util.Date;

public class RegisterView extends JFrame {

    private JPanel panel;
    private JLabel nameLabel, emailLabel, passwordLabel, confirmPasswordLabel, genderLabel, birthdayLabel, addressLabel, phoneLabel, educationLabel;
    private JTextField nameField, emailField, addressField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> genderComboBox;
    private JDateChooser birthdayDateChooser;
    private JComboBox<String> educationComboBox;
    private JButton registerButton;

    public RegisterView() {
        setTitle("Register");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        createComponents();
        addComponents();
        addListeners();
    }

    private void createComponents() {
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        nameLabel = new JLabel("Name:");
        emailLabel = new JLabel("Email:");
        passwordLabel = new JLabel("Password:");
        confirmPasswordLabel = new JLabel("Confirm Password:");
        genderLabel = new JLabel("Gender:");
        birthdayLabel = new JLabel("Birthday:");
        addressLabel = new JLabel("Address:");
        phoneLabel = new JLabel("Phone:");
        educationLabel = new JLabel("Education:");

        nameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        birthdayDateChooser = new JDateChooser();
        addressField = new JTextField(20);
        phoneField = new JTextField(20);
        educationComboBox = new JComboBox<>(new String[]{"High School", "Bachelor", "Master", "PhD"});

        registerButton = new JButton("Register");
    }

    private void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(genderLabel, gbc);

        gbc.gridx = 1;
        panel.add(genderComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(birthdayLabel, gbc);

        gbc.gridx = 1;
        panel.add(birthdayDateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(addressLabel, gbc);

        gbc.gridx = 1;
        panel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(educationLabel, gbc);

        gbc.gridx = 1;
        panel.add(educationComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(registerButton, gbc);

        add(panel);
    }

    private void addListeners() {
        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String gender = (String) genderComboBox.getSelectedItem();
            Date birthday = birthdayDateChooser.getDate();
            String address = addressField.getText();
            String phone = phoneField.getText();
            String education = (String) educationComboBox.getSelectedItem();

            // Perform validation
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all the required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Password and Confirm Password do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Perform registration logic here
            System.out.println("Name: " + name);
            System.out.println("Email: " + email);
            System.out.println("Password: " + password);
            System.out.println("Confirm Password: " + confirmPassword);
            System.out.println("Gender: " + gender);
            System.out.println("Birthday: " + birthday);
            System.out.println("Address: " + address);
            System.out.println("Phone: " + phone);
            System.out.println("Education: " + education);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterView::new);
    }
}