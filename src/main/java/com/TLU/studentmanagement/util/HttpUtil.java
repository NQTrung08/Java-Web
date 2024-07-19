package main.java.com.TLU.studentmanagement.util;

import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class HttpUtil {

    private static String accessToken = "";
    private static String refreshToken = "";
    private static boolean refreshingToken = false;

    // Cập nhật accessToken và refreshToken
    public static void setTokens(String accessToken, String refreshToken) {
        HttpUtil.accessToken = accessToken;
        HttpUtil.refreshToken = refreshToken;
    }

    // Phương thức chung cho tất cả các yêu cầu HTTP có token
    private static String sendRequest(String apiUrl, String method, String requestData, String token) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        if (token != null && !token.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

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
            throw new RuntimeException("Unauthorized");
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode + " - " + conn.getResponseMessage());
        }
    }

    // Phương thức chung cho tất cả các yêu cầu HTTP không cần token
    private static String sendRequestWithoutToken(String apiUrl, String method, String requestData) throws Exception {
        return sendRequest(apiUrl, method, requestData, null);
    }

    public static String sendRequestWithRefresh(String apiUrl, String method, String requestData) throws Exception {
        try {
            return sendRequest(apiUrl, method, requestData, accessToken);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Unauthorized")) {
                if (!refreshingToken && refreshAccessToken()) {
                    return sendRequest(apiUrl, method, requestData, accessToken);  // Thực hiện lại request với token mới
                } else {
                    throw new RuntimeException("Failed to refresh token.");
                }
            } else {
                throw e;
            }
        }
    }

    public static String sendGet(String apiUrl) throws Exception {
        return sendRequestWithRefresh(apiUrl, "GET", null);
    }

    public static String sendPost(String apiUrl, String requestData) throws Exception {
        return sendRequestWithRefresh(apiUrl, "POST", requestData);
    }

    public static String sendPut(String apiUrl, String requestData) throws Exception {
        return sendRequestWithRefresh(apiUrl, "PUT", requestData);
    }

    public static String sendDelete(String apiUrl) throws Exception {
        return sendRequestWithRefresh(apiUrl, "DELETE", null);
    }

    // Phương thức làm mới accessToken
    private static boolean refreshAccessToken() throws Exception {
        refreshingToken = true;
        try {
            String apiUrl = "http://localhost:8080/api/auth/refresh";
            JSONObject jsonInput = new JSONObject();
            jsonInput.put("refreshToken", refreshToken);

            // Gửi yêu cầu làm mới token với refreshToken mà không cần accessToken
            String response = sendRequestWithoutToken(apiUrl, "POST", jsonInput.toString());
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
        } finally {
            refreshingToken = false;
        }
    }
}
