package main.java.com.TLU.studentmanagement.controller.courses;

import main.java.com.TLU.studentmanagement.model.Course;
import main.java.com.TLU.studentmanagement.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CourseController {

    public static List<Course> getAllCourses() throws Exception {
        String apiUrl = "http://localhost:8080/api/course/getAll";
        String response = HttpUtil.sendGet(apiUrl);
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray coursesArray = jsonResponse.getJSONArray("data");

        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < coursesArray.length(); i++) {
            JSONObject courseObject = coursesArray.getJSONObject(i);
            Course course = new Course();
            course.setId(courseObject.getString("_id"));
            course.setName(courseObject.getString("name"));
            course.setCode(courseObject.getString("code"));
            course.setCredit(courseObject.getInt("credit"));
            course.setCreatedAt(courseObject.getString("createdAt"));
            course.setUpdatedAt(courseObject.getString("updatedAt"));

            // Kiểm tra và gán majorId nếu tồn tại
            if (courseObject.has("majorId")) {
                course.setMajorId(courseObject.getString("majorId"));
            } else {
                course.setMajorId(null); // Hoặc một giá trị mặc định nào đó nếu cần
            }

            courses.add(course);
        }
        return courses;
    }

    public static void createCourse(String name, String code, int credit, String majorId) throws Exception {
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("name", name);
        jsonInput.put("code", code);
        jsonInput.put("credit", credit);
        jsonInput.put("majorId", majorId);

        String apiUrl = "http://localhost:8080/api/course/add-course";
        HttpUtil.sendPost(apiUrl, jsonInput.toString());
    }

    public static void updateCourse(String id, String name, String code, int credit, String majorId) throws Exception {
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("name", name);
        jsonInput.put("code", code);
        jsonInput.put("credit", credit);
        jsonInput.put("majorId", majorId);

        String apiUrl = "http://localhost:8080/api/course/update/" + id;
        HttpUtil.sendPut(apiUrl, jsonInput.toString());
    }

    public static void deleteCourse(String id) throws Exception {
        String apiUrl = "http://localhost:8080/api/course/delete/" + id;
        HttpUtil.sendDelete(apiUrl);
    }
}
