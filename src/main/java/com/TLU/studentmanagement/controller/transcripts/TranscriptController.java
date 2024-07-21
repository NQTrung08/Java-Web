package main.java.com.TLU.studentmanagement.controller.transcripts;

import main.java.com.TLU.studentmanagement.model.Transcript;
import main.java.com.TLU.studentmanagement.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONObject;

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
        System.out.println(transcripts);
        return transcripts;
    }


    public Transcript getTranscriptById(String id) {
        try {
            String response = HttpUtil.sendGet(BASE_URL + "/" + id);
            JSONObject jsonTranscript = new JSONObject(response);
            Transcript transcript = new Transcript();
            transcript.setId(jsonTranscript.getString("_id"));
            transcript.setDeleted(jsonTranscript.getBoolean("deleted"));

            if (jsonTranscript.has("studentId") && !jsonTranscript.isNull("studentId")) {
                Object studentIdObj = jsonTranscript.get("studentId");
                if (studentIdObj instanceof JSONObject) {
                    JSONObject studentIdJson = (JSONObject) studentIdObj;
                    if (studentIdJson.has("_id")) {
                        transcript.setStudentId(studentIdJson.getString("_id"));
                        transcript.setStudentName(studentIdJson.getString("fullname"));
                    } else {
                        transcript.setStudentId("");
                    }
                } else if (studentIdObj instanceof String) {
                    transcript.setStudentId((String) studentIdObj);
                } else {
                    transcript.setStudentId("");
                }
            } else {
                transcript.setStudentId("");
            }

            if (jsonTranscript.has("semesterId") && !jsonTranscript.isNull("semesterId")) {
                Object semesterIdObj = jsonTranscript.get("semesterId");
                if (semesterIdObj instanceof JSONObject) {
                    JSONObject semesterIdJson = (JSONObject) semesterIdObj;
                    if (semesterIdJson.has("_id")) {
                        transcript.setSemesterId(semesterIdJson.getString("_id"));
                        transcript.setSemesterName(semesterIdJson.getString("name"));
                    } else {
                        transcript.setSemesterId("");
                    }
                } else if (semesterIdObj instanceof String) {
                    transcript.setSemesterId((String) semesterIdObj);
                } else {
                    transcript.setSemesterId("");
                }
            } else {
                transcript.setSemesterId("");
            }

            return transcript;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Transcript> getTranscriptByStudent(String studentId) {
        List<Transcript> transcripts = new ArrayList<>();
        try {
            String response = HttpUtil.sendGet(BASE_URL + "/student/" + studentId);
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonArray = jsonResponse.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonTranscript = jsonArray.getJSONObject(i);
                Transcript transcript = new Transcript();
                transcript.setId(jsonTranscript.getString("_id"));
                transcript.setDeleted(jsonTranscript.getBoolean("deleted"));

                // Parsing studentId and semesterId as objects
                if (jsonTranscript.has("studentId") && !jsonTranscript.isNull("studentId")) {
                    Object studentIdObj = jsonTranscript.get("studentId");
                    if (studentIdObj instanceof JSONObject) {
                        JSONObject studentIdJson = (JSONObject) studentIdObj;
                        if (studentIdJson.has("_id")) {
                            transcript.setStudentId(studentIdJson.getString("_id"));
                            transcript.setStudentName(studentIdJson.getString("fullname"));
                        } else {
                            transcript.setStudentId("");
                        }
                    } else if (studentIdObj instanceof String) {
                        transcript.setStudentId((String) studentIdObj);
                    } else {
                        transcript.setStudentId("");
                    }
                } else {
                    transcript.setStudentId("");
                }

                if (jsonTranscript.has("semesterId") && !jsonTranscript.isNull("semesterId")) {
                    Object semesterIdObj = jsonTranscript.get("semesterId");
                    if (semesterIdObj instanceof JSONObject) {
                        JSONObject semesterIdJson = (JSONObject) semesterIdObj;
                        if (semesterIdJson.has("_id")) {
                            transcript.setSemesterId(semesterIdJson.getString("_id"));
                            transcript.setSemesterName(semesterIdJson.getString("name"));
                        } else {
                            transcript.setSemesterId("");
                        }
                    } else if (semesterIdObj instanceof String) {
                        transcript.setSemesterId((String) semesterIdObj);
                    } else {
                        transcript.setSemesterId("");
                    }
                } else {
                    transcript.setSemesterId("");
                }
                transcripts.add(transcript);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transcripts;
    }

    public Transcript getTranscriptBySemester(String studentId, String semesterId) {
        try {
            String response = HttpUtil.sendGet(BASE_URL + "/student/" + studentId + "/semester/" + semesterId);
            JSONObject jsonTranscript = new JSONObject(response);
            Transcript transcript = new Transcript();
            transcript.setId(jsonTranscript.getString("_id"));
            transcript.setDeleted(jsonTranscript.getBoolean("deleted"));

            if (jsonTranscript.has("studentId") && !jsonTranscript.isNull("studentId")) {
                Object studentIdObj = jsonTranscript.get("studentId");
                if (studentIdObj instanceof JSONObject) {
                    JSONObject studentIdJson = (JSONObject) studentIdObj;
                    if (studentIdJson.has("_id")) {
                        transcript.setStudentId(studentIdJson.getString("_id"));
                        transcript.setStudentName(studentIdJson.getString("fullname"));
                    } else {
                        transcript.setStudentId("");
                    }
                } else if (studentIdObj instanceof String) {
                    transcript.setStudentId((String) studentIdObj);
                } else {
                    transcript.setStudentId("");
                }
            } else {
                transcript.setStudentId("");
            }

            if (jsonTranscript.has("semesterId") && !jsonTranscript.isNull("semesterId")) {
                Object semesterIdObj = jsonTranscript.get("semesterId");
                if (semesterIdObj instanceof JSONObject) {
                    JSONObject semesterIdJson = (JSONObject) semesterIdObj;
                    if (semesterIdJson.has("_id")) {
                        transcript.setSemesterId(semesterIdJson.getString("_id"));
                        transcript.setSemesterName(semesterIdJson.getString("name"));
                    } else {
                        transcript.setSemesterId("");
                    }
                } else if (semesterIdObj instanceof String) {
                    transcript.setSemesterId((String) semesterIdObj);
                } else {
                    transcript.setSemesterId("");
                }
            } else {
                transcript.setSemesterId("");
            }

            return transcript;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void createTranscript(Transcript transcript) {
        try {
            JSONObject jsonTranscript = new JSONObject();
            jsonTranscript.put("studentId", transcript.getStudentId());
            jsonTranscript.put("semesterId", transcript.getSemesterId());

            System.out.println("studentId: " + transcript.getStudentId());
            System.out.println("semesterId: " + transcript.getSemesterId());
            System.out.println(jsonTranscript.toString());

            HttpUtil.sendPost("http://localhost:8080/api/transcript/create", jsonTranscript.toString());
        } catch (Exception e) {
            e.printStackTrace();
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
