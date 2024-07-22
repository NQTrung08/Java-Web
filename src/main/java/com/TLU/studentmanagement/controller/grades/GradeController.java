package main.java.com.TLU.studentmanagement.controller.grades;

import main.java.com.TLU.studentmanagement.model.Grade;
import main.java.com.TLU.studentmanagement.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GradeController {

    private static final String BASE_URL = "http://localhost:8080/api/grade";

    public List<Grade> getAllGrades() {
        List<Grade> grades = new ArrayList<>();
        try {
            String response = HttpUtil.sendGet(BASE_URL + "/getAll");
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonGrade = jsonArray.getJSONObject(i);
                Grade grade = new Grade(
                        jsonGrade.getString("_id"),
                        jsonGrade.getString("courseId"),
                        jsonGrade.getString("transcriptId"),
                        jsonGrade.getDouble("midScore"),
                        jsonGrade.getDouble("finalScore")
                );
                grades.add(grade);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grades;
    }

    public Grade getGradeById(String id) {
        try {
            String response = HttpUtil.sendGet(BASE_URL + "/" + id);
            JSONObject jsonGrade = new JSONObject(response);
            return new Grade(
                    jsonGrade.getString("_id"),
                    jsonGrade.getString("courseId"),
                    jsonGrade.getString("transcriptId"),
                    jsonGrade.getDouble("midScore"),
                    jsonGrade.getDouble("finalScore")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createGrade(Grade grade) {
        try {
            JSONObject jsonGrade = new JSONObject();
            jsonGrade.put("courseId", grade.getCourseId());
            jsonGrade.put("transcriptId", grade.getTranscriptId());
            jsonGrade.put("midScore", grade.getMidScore());
            jsonGrade.put("finalScore", grade.getFinalScore());
            HttpUtil.sendPost(BASE_URL + "/create", jsonGrade.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateGrade(String gradeId, Grade grade) {
        try {
            JSONObject jsonGrade = new JSONObject();
//            jsonGrade.put("courseId", grade.getCourseId());
//            jsonGrade.put("transcriptId", grade.getTranscriptId());
            jsonGrade.put("midScore", grade.getMidScore());
            jsonGrade.put("finalScore ", grade.getFinalScore());

//            System.out.println("midScore " + grade.getMidScore());
//            System.out.println("finalScore " + grade.getFinalScore());
//            System.out.println("grade id: " + gradeId);

            HttpUtil.sendPut(BASE_URL + "/update/" + gradeId, jsonGrade.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteGrade(String gradeId) {
        try {
            HttpUtil.sendDelete(BASE_URL + "/delete/" + gradeId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
