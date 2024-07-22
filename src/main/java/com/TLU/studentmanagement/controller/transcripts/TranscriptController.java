package main.java.com.TLU.studentmanagement.controller.transcripts;

import main.java.com.TLU.studentmanagement.model.Grade;
import main.java.com.TLU.studentmanagement.model.Transcript;
import main.java.com.TLU.studentmanagement.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import raven.toast.Notifications;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.swing.*;
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
        Transcript transcript = new Transcript();
        transcript.setStudentId(studentId);
        transcript.setSemesterId(semesterId);

        try {
            // Gửi yêu cầu GET đến API
            String response = HttpUtil.sendGet("http://localhost:8080/api/transcript/student/" + studentId + "/semester/" + semesterId);

            // Phân tích JSON
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonArray = jsonResponse.getJSONArray("data");

            // Danh sách lưu các điểm
            List<Grade> grades = new ArrayList<>();

            // Lặp qua các đối tượng điểm trong mảng JSON
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonGrade = jsonArray.getJSONObject(i);

                Grade grade = new Grade();
//                grade.getString("courseCode"),
//                grade.getInt("credit"),
                grade.setId(jsonGrade.getString("gradeId"));
                grade.setCourse(jsonGrade.getString("courseName"));
                grade.setMidScore(jsonGrade.getDouble("midScore"));
                grade.setFinalScore(jsonGrade.getDouble("finalScore"));
                grade.setAverageScore(jsonGrade.getDouble("averageScore"));
                grade.setStatus(jsonGrade.getString("status"));

                grades.add(grade);
            }

            // Gán danh sách điểm vào đối tượng Transcript
            transcript.setGrades(grades);
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

    public int updateTranscript(String transcriptId, Transcript transcript) {
        try {
            // Tạo JSON object từ đối tượng Transcript
            JSONObject jsonTranscript = new JSONObject();
            jsonTranscript.put("studentId", transcript.getStudentId());
            jsonTranscript.put("semesterId", transcript.getSemesterId());

            System.out.println("transcriptId " + transcriptId);

            // Gửi yêu cầu PUT đến server để cập nhật bảng điểm
            String response = HttpUtil.sendPut(BASE_URL + "/update/" + transcriptId, jsonTranscript.toString());

            // Xử lý phản hồi từ server
            if (response != null) {
                return 1; // Cập nhật thành công
            }
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse == null || jsonResponse.getInt("code") == 404 || jsonResponse.getString("status") == "error") {
                return -1; // Bảng điểm đã tồn tại
            } else {
                return 0; // Cập nhật thất bại
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Thất bại trong quá trình cập nhật
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

        System.out.println("keyword: " + keyword);

        try {
            String url = BASE_URL + "/search?keyword=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString());

            System.out.println("url: " + url);

            String response = HttpUtil.sendPost(url, null);
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonArray = jsonResponse.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonTranscript = jsonArray.getJSONObject(i);
                Transcript transcript = new Transcript();
                transcript.setId(jsonTranscript.getString("_id"));
                transcript.setDeleted(jsonTranscript.getBoolean("deleted"));

                // Lấy thông tin sinh viên
                JSONObject studentObj = jsonTranscript.getJSONObject("student");
                transcript.setStudentId(studentObj.getString("_id"));

                // Lấy thông tin học kỳ
                JSONObject semesterObj = jsonTranscript.getJSONObject("semester");
                transcript.setSemesterId(semesterObj.getString("_id"));

                transcripts.add(transcript);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transcripts;
    }


}
