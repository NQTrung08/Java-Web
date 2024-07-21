package main.java.com.TLU.studentmanagement.controller.transcripts;

import main.java.com.TLU.studentmanagement.model.Grade;
import main.java.com.TLU.studentmanagement.model.Transcript;
import main.java.com.TLU.studentmanagement.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import raven.toast.Notifications;

import java.util.ArrayList;
import java.util.List;

public class TranscriptController {

    private static final String BASE_URL = "http://localhost:8080/api/transcript";

    public List<Transcript> getAllTranscripts() {
        List<Transcript> transcripts = new ArrayList<>();
        try {
            String response = HttpUtil.sendGet(BASE_URL + "/getAll");
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonArray = jsonResponse.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonTranscript = jsonArray.getJSONObject(i);
                Transcript transcript = new Transcript();
                transcript.setId(jsonTranscript.getString("_id"));
                transcript.setDeleted(jsonTranscript.getBoolean("deleted"));

                // Parsing studentId and studentName
                if (jsonTranscript.has("student") && !jsonTranscript.isNull("student")) {
                    JSONObject studentJson = jsonTranscript.getJSONObject("student");
                    if (studentJson.has("_id")) {
                        transcript.setStudentId(studentJson.getString("_id"));
                    }
                    if (studentJson.has("fullname")) {
                        transcript.setStudentName(studentJson.getString("fullname"));
                    }
                }

                // Parsing semesterId and semesterName
                if (jsonTranscript.has("semester") && !jsonTranscript.isNull("semester")) {
                    JSONObject semesterJson = jsonTranscript.getJSONObject("semester");
                    if (semesterJson.has("_id")) {
                        transcript.setSemesterId(semesterJson.getString("_id"));
                    }
                    if (semesterJson.has("semester") && semesterJson.has("group") && semesterJson.has("year")) {
                        String semesterName = semesterJson.getString("semester") + " - " + semesterJson.getString("group") + " - Năm học: " + semesterJson.getString("year");
                        transcript.setSemesterName(semesterName);
                    }
                }
                transcripts.add(transcript);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(transcripts);
        return transcripts;
    }

    public Transcript getTranscriptById(String transcriptId) {
        try {
            String response = HttpUtil.sendGet("http://localhost:8080/api/transcript/" + transcriptId);
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject jsonTranscript = jsonResponse.getJSONObject("data");

            // Parse transcript details
            Transcript transcript = new Transcript();
            transcript.setId(jsonTranscript.getString("_id"));
            transcript.setDeleted(jsonTranscript.getBoolean("deleted"));
            transcript.setCreatedAt(jsonTranscript.getString("createdAt"));
            transcript.setUpdatedAt(jsonTranscript.getString("updatedAt"));

            // Parse student details
            JSONObject studentJson = jsonTranscript.getJSONObject("student");
            transcript.setStudentId(studentJson.getString("_id"));
            transcript.setStudentName(studentJson.getString("fullname"));

            // Parse semester details
            JSONObject semesterJson = jsonTranscript.getJSONObject("semester");
            String semesterName = semesterJson.getString("semester") + " - " + semesterJson.getString("group") + " - Năm học: " + semesterJson.getString("year");
            transcript.setSemesterId(semesterJson.getString("_id"));
            transcript.setSemesterName(semesterName);

            // Parse grades
            JSONArray gradesArray = jsonTranscript.getJSONArray("grades");
            List<Grade> grades = new ArrayList<>();
            for (int i = 0; i < gradesArray.length(); i++) {
                JSONObject gradeJson = gradesArray.getJSONObject(i);
                Grade grade = new Grade();
                grade.setCourse(gradeJson.getString("course"));
                grade.setMidScore(gradeJson.getDouble("midScore"));
                grade.setFinalScore(gradeJson.getDouble("finalScore"));
                grade.setAverageScore(gradeJson.getDouble("averageScore"));
                grade.setStatus(gradeJson.getString("status"));
                grades.add(grade);
            }
            transcript.setGrades(grades);

            return transcript;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Transcript> getTranscriptByStudentId(String studentId) {
        List<Transcript> transcripts = new ArrayList<>();
        try {
            String response = HttpUtil.sendGet("http://localhost:8080/api/transcript/student/" + studentId);
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonArray = jsonResponse.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonTranscript = jsonArray.getJSONObject(i);

                Transcript transcript = new Transcript();
                transcript.setCourse(jsonTranscript.getString("course"));
                transcript.setMidScore(jsonTranscript.getDouble("midScore"));
                transcript.setFinalScore(jsonTranscript.getDouble("finalScore"));
                transcript.setAverageScore(jsonTranscript.getDouble("averageScore"));
                transcript.setStatus(jsonTranscript.getString("status"));

                transcripts.add(transcript);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transcripts;
    }

    public Transcript getTranscriptBySemesterStudent(String studentId, String semesterId) {
        Transcript transcript = null;
        try {
            String response = HttpUtil.sendGet("http://localhost:8080/api/transcript/student/" + studentId + "/semester/" + semesterId);
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonArray = jsonResponse.getJSONArray("data");

            if (jsonArray.length() > 0) {
                JSONObject jsonTranscript = jsonArray.getJSONObject(0);

                transcript = new Transcript();
                transcript.setId(jsonTranscript.getString("_id"));
                transcript.setStudentId(studentId);
                transcript.setSemesterId(semesterId);

                JSONArray gradesArray = jsonTranscript.getJSONArray("grades");
                List<Grade> grades = new ArrayList<>();
                for (int i = 0; i < gradesArray.length(); i++) {
                    JSONObject jsonGrade = gradesArray.getJSONObject(i);
                    Grade grade = new Grade();
                    grade.setCourse(jsonGrade.getString("course"));
                    grade.setMidScore(jsonGrade.getDouble("midScore"));
                    grade.setFinalScore(jsonGrade.getDouble("finalScore"));
                    grade.setAverageScore(jsonGrade.getDouble("averageScore"));
                    grade.setStatus(jsonGrade.getString("status"));
                    grades.add(grade);
                }
                transcript.setGrades(grades);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transcript;
    }



    public int createTranscript(Transcript transcript) {
        try {
            JSONObject jsonTranscript = new JSONObject();
            jsonTranscript.put("studentId", transcript.getStudentId());
            jsonTranscript.put("semesterId", transcript.getSemesterId());

//            System.out.println("studentId: " + transcript.getStudentId());
//            System.out.println("semesterId: " + transcript.getSemesterId());
//            System.out.println(jsonTranscript.toString());

            String response = HttpUtil.sendPost("http://localhost:8080/api/transcript/create", jsonTranscript.toString());

//            System.out.println("Rsp: " + response);

            // Kiểm tra phản hồi từ server
            if (response == null || response.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Không nhận được phản hồi từ server.");
                return 0; // Lỗi do không có phản hồi
            }

            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.has("code") && jsonResponse.getInt("code") == 400 && jsonResponse.getString("message").equals("Transcript already exists")) {
                return -1; // Trùng lặp
            }

            return 1; // Thành công
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Lỗi khác
        }
    }



    public void updateTranscript(String transcriptId, Transcript transcript) {
        try {
            JSONObject jsonTranscript = new JSONObject();
            jsonTranscript.put("studentId", transcript.getStudentId());
            jsonTranscript.put("semesterId", transcript.getSemesterId());
            HttpUtil.sendPut(BASE_URL + "/update/" + transcriptId, jsonTranscript.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTranscript(String transcriptId) {
        try {
            HttpUtil.sendDelete(BASE_URL + "/delete/" + transcriptId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restoreTranscript(String transcriptId) {
        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("transcriptId", transcriptId);
            HttpUtil.sendPut(BASE_URL + "/restore", jsonRequest.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Transcript> searchTranscripts(String keyword) {
        List<Transcript> transcripts = new ArrayList<>();
        try {
            String url = BASE_URL + "/search?keyword=" + keyword;
            String response = HttpUtil.sendGet(url);
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonTranscript = jsonArray.getJSONObject(i);
                Transcript transcript = new Transcript();
                transcript.setId(jsonTranscript.getString("_id"));
                transcript.setDeleted(jsonTranscript.getBoolean("deleted"));
                transcript.setStudentId(jsonTranscript.optString("studentId", ""));
                transcript.setSemesterId(jsonTranscript.optString("semesterId", ""));
                transcripts.add(transcript);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transcripts;
    }

}
