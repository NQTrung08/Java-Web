package com.TLU.studentmanagement.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpUtil {

    /**
     * Gửi yêu cầu HTTP GET đến apiUrl và trả về phản hồi từ server dưới dạng chuỗi.
     * @param apiUrl Địa chỉ URL của API
     * @return Chuỗi phản hồi từ server
     * @throws Exception Ngoại lệ xảy ra trong quá trình gửi yêu cầu
     */
    public static String sendGet(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
    }

    /**
     * Gửi yêu cầu HTTP DELETE đến apiUrl và trả về phản hồi từ server dưới dạng chuỗi.
     * @param apiUrl Địa chỉ URL của API
     * @return Chuỗi phản hồi từ server
     * @throws Exception Ngoại lệ xảy ra trong quá trình gửi yêu cầu
     */
    public static String sendDelete(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
    }

    /**
     * Gửi yêu cầu HTTP PUT đến apiUrl với các tham số parameters và trả về phản hồi từ server dưới dạng chuỗi.
     * @param apiUrl Địa chỉ URL của API
     * @param parameters Tham số của yêu cầu PUT
     * @return Chuỗi phản hồi từ server
     * @throws Exception Ngoại lệ xảy ra trong quá trình gửi yêu cầu
     */
    public static String sendPut(String apiUrl, Map<String, String> parameters) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : parameters.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(param.getValue(), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(postDataBytes);
        }

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
    }

    /**
     * Gửi yêu cầu HTTP POST đến apiUrl với dữ liệu requestData và trả về phản hồi từ server dưới dạng chuỗi.
     * @param apiUrl Địa chỉ URL của API
     * @param requestData Dữ liệu gửi đi dưới dạng chuỗi
     * @return Chuỗi phản hồi từ server
     * @throws Exception Ngoại lệ xảy ra trong quá trình gửi yêu cầu
     */
    public static String sendPost(String apiUrl, String requestData) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = requestData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
    }
}

