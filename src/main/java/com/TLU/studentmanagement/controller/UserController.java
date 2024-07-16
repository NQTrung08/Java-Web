package main.java.com.TLU.studentmanagement.controller;

import main.java.com.TLU.studentmanagement.model.User;
import main.java.com.TLU.studentmanagement.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserController {

    public static List<User> getAllUsers() throws Exception {
        String response = HttpUtil.sendGet("http://localhost:8080/api/user/getAll");

        // Debug: Log the raw response
        System.out.println("API Response: " + response);

        JSONObject responseObject = new JSONObject(response);
        JSONArray jsonArray = responseObject.getJSONArray("data");

        List<User> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            User user = new User(
                    jsonObject.optString("_id", "N/A"),
                    jsonObject.optString("fullname", "N/A"),
                    jsonObject.optString("msv", "N/A"),
                    jsonObject.optString("major", "N/A"),
                    jsonObject.optString("year", "N/A"),
                    jsonObject.optString("email", "N/A"),
                    jsonObject.optString("gender", "N/A"),
                    jsonObject.optString("gvcn", "N/A"), // Assuming you have a gvcn field in your User model
                    jsonObject.optString("class", "N/A") // Assuming you have a class field in your User model
            );
            users.add(user);
        }
        return users;
    }

    // The rest of the methods remain unchanged
    public static void createUser(String fullname, String msv, String major, String year, String gvcn, String gender, String className, String email, String majorId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fullname", fullname);
        jsonObject.put("msv", msv);
        jsonObject.put("major", major);
        jsonObject.put("year", year);
        jsonObject.put("gvcn", gvcn);
        jsonObject.put("gender", gender);
        jsonObject.put("className", className);
        jsonObject.put("email", email);
        jsonObject.put("majorId", majorId);

        HttpUtil.sendPost("http://localhost:8080/api/user/create-user", jsonObject.toString());
    }

    public static void updateUser(String id, String fullname, String msv, String major, String year, String gvcn, String gender, String className, String email, String majorId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fullname", fullname);
        jsonObject.put("msv", msv);
        jsonObject.put("major", major);
        jsonObject.put("year", year);
        jsonObject.put("gvcn", gvcn);
        jsonObject.put("gender", gender);
        jsonObject.put("className", className);
        jsonObject.put("email", email);
        jsonObject.put("majorId", majorId);

        HttpUtil.sendPut("http://localhost:8080/api/user/update-profile/" + id, jsonObject.toString());
    }

    public static void deleteUser(String id) throws Exception {
        HttpUtil.sendDelete("http://localhost:8080/api/user/delete/" + id);
    }

//    public static String getGvcnNameById(String gvcn) {
//    }
}
