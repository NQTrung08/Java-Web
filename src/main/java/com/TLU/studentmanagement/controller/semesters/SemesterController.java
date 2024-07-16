package main.java.com.TLU.studentmanagement.controller.semesters;

import main.java.com.TLU.studentmanagement.model.Semester;
import main.java.com.TLU.studentmanagement.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SemesterController {

    public static List<Semester> getAllSemesters() throws Exception {
        String apiUrl = "http://localhost:8080/api/semester/getAll";
        String response = HttpUtil.sendGet(apiUrl);
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray semestersArray = jsonResponse.getJSONArray("data");

        List<Semester> semesters = new ArrayList<>();
        for (int i = 0; i < semestersArray.length(); i++) {
            JSONObject semesterObject = semestersArray.getJSONObject(i);
            Semester semester = new Semester();
            semester.setId(semesterObject.getString("_id"));
            semester.setSemester(semesterObject.getString("semester"));
            semester.setGroup(semesterObject.getString("group"));
            semester.setYear(semesterObject.getString("year"));
            semesters.add(semester);
        }
        return semesters;
    }

    public static void createSemester(String semester, String group, String year) throws Exception {
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("semester", semester);
        jsonInput.put("group", group);
        jsonInput.put("year", year);

        String apiUrl = "http://localhost:8080/api/semester/create";
        HttpUtil.sendPost(apiUrl, jsonInput.toString());
    }

    public static void updateSemester(String id, String semester, String group, String year) throws Exception {
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("semester", semester);
        jsonInput.put("group", group);
        jsonInput.put("year", year);

        String apiUrl = "http://localhost:8080/api/semester/update/" + id;
        HttpUtil.sendPut(apiUrl, jsonInput.toString());
    }

    public static void deleteSemester(String id) throws Exception {
        String apiUrl = "http://localhost:8080/api/semester/delete/" + id;
        HttpUtil.sendDelete(apiUrl);
    }
}
