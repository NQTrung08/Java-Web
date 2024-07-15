package main.java.com.TLU.studentmanagement.util;

import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class HttpUtil {

    private static String accessToken = "";
    private static String refreshToken = "";

    // Cập nhật accessToken và refreshToken
    public static void setTokens(String accessToken, String refreshToken) {
        HttpUtil.accessToken = accessToken;
        HttpUtil.refreshToken = refreshToken;
    }

    // Phương thức chung cho tất cả các yêu cầu HTTP
    private static String sendRequest(String apiUrl, String method, String requestData, String token) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        if (requestData != null) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            // Nếu token hết hạn, làm mới token và thực hiện lại request
            if (refreshAccessToken()) {
                return sendRequest(apiUrl, method, requestData, accessToken);  // Thực hiện lại request với token mới
            } else {
                throw new RuntimeException("Failed to refresh token.");
            }
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode + " - " + conn.getResponseMessage());
        }
    }

    public static String sendGet(String apiUrl) throws Exception {
        return sendRequest(apiUrl, "GET", null, accessToken);
    }

    public static String sendPost(String apiUrl, String requestData) throws Exception {
        return sendRequest(apiUrl, "POST", requestData, accessToken);
    }

    public static String sendPut(String apiUrl, String requestData) throws Exception {
        return sendRequest(apiUrl, "PUT", requestData, accessToken);
    }

    public static String sendDelete(String apiUrl) throws Exception {
        return sendRequest(apiUrl, "DELETE", null, accessToken);
    }

    // Phương thức làm mới accessToken
    private static boolean refreshAccessToken() throws Exception {
        String apiUrl = "http://localhost:8080/api/auth/refresh";
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("refreshToken", refreshToken);

        // Gửi yêu cầu làm mới token với refreshToken
        String response = sendRequest(apiUrl, "POST", jsonInput.toString(), refreshToken);
        JSONObject jsonResponse = new JSONObject(response);

        if (jsonResponse.has("tokens")) {
            String newAccessToken = jsonResponse.getJSONObject("tokens").getString("accessToken");
            String newRefreshToken = jsonResponse.getJSONObject("tokens").getString("refreshToken");

            // Cập nhật accessToken và refreshToken mới
            setTokens(newAccessToken, newRefreshToken);
            return true;
        } else {
            return false;
        }
    }
}
