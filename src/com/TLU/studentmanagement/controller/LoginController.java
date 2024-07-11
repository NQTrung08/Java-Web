package com.TLU.studentmanagement.controller;

import com.TLU.studentmanagement.model.User;
import com.TLU.studentmanagement.view.LoginView;
import com.TLU.studentmanagement.util.HttpUtil;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class LoginController {
    private LoginView loginView;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.loginView.addLoginListener(new LoginListener());
    }

    class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            try {
                String apiUrl = "http://localhost:8080/api/auth/login";
                JSONObject jsonInput = new JSONObject();
                jsonInput.put("msv", username);
                jsonInput.put("password", password);

                String response = HttpUtil.sendPost(apiUrl, jsonInput.toString());
//                System.out.println("API Response: " + response);

                JSONObject jsonResponse = new JSONObject(response);

                // Kiểm tra sự tồn tại của accessToken để xác định đăng nhập thành công
                if (jsonResponse.has("tokens") && jsonResponse.getJSONObject("tokens").has("accessToken")) {
                    String userId = jsonResponse.getJSONObject("data").getJSONObject("user").getString("_id");
                    String token = jsonResponse.getJSONObject("tokens").getString("accessToken");
                    HttpUtil.setAdminToken(token);

                    User user = fetchUserDetails(userId);
                    JOptionPane.showMessageDialog(null, "Login successful! Welcome " + user.getFullName());

//                     Lưu thông tin người dùng
                     saveUserDetails(user);
                } else {
                    JOptionPane.showMessageDialog(null, "Login failed: Missing token in response.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }

        private User fetchUserDetails(String userId) throws Exception {
            String apiUrl = "http://localhost:8080/api/user/" + userId;
            String response = HttpUtil.sendGet(apiUrl);
            System.out.println("API Response: " + response);
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject userData = jsonResponse.getJSONObject("data");

            User user = new User();
            user.setId(userData.getString("_id"));
            user.setMsv(userData.getString("msv"));
            user.setFullName(userData.getString("fullname"));
            user.setEmail(userData.getString("email"));
            user.setAdmin(userData.getBoolean("isAdmin"));

            return user;
        }

        private void saveUserDetails(User user) {
            JSONObject userJson = new JSONObject();
            userJson.put("id", user.getId());
            userJson.put("msv", user.getMsv());
            userJson.put("fullName", user.getFullName());
            userJson.put("email", user.getEmail());
            userJson.put("isAdmin", user.isAdmin());

            try (FileWriter file = new FileWriter("user_details.json")) {
                file.write(userJson.toString());
                file.flush();
                System.out.println("User details saved to user_details.json");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
