package com.schoolapp.student_management;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class CustomisedSMSSender {

    /**
     * Sends an SMS and returns the raw response from the provider.
     *
     * @param phone   recipient's phone number
     * @param message message text
     * @return provider's raw response or error message
     */
    public String sendSMS(String phone, String message) {
        String username = "vicky";
        String password = "vicky2324";
        String apiKey = "9ecaf1a0db37a3935462c3238e8608e3";
        String appId = "UMSC770525";
        String senderId = "UMS_SMS"; // ✅ Keeping as you have it
        String email = "gylmueni21@gmail.com";

        try {
            String payload = "{"
                    + "\"email\": \"" + email + "\","
                    + "\"api_key\": \"" + apiKey + "\","
                    + "\"app_id\": \"" + appId + "\","
                    + "\"phone\": \"" + phone + "\","
                    + "\"sender_id\": \"" + senderId + "\","
                    + "\"message\": \"" + message + "\""
                    + "}";

            URL url = new URL("https://comms.umeskiasoftwares.com/api/v1/sms/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            conn.setRequestProperty("Authorization", "Basic " + encodedAuth);

            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write(payload);
                writer.flush();
            }

            StringBuilder responseBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
            }

            conn.disconnect();

            String rawResponse = responseBuilder.toString();
            System.out.println("Customised SMS Response: " + rawResponse); // ✅ still logs
            return rawResponse; // ✅ return to MessageService
        } catch (Exception e) {
            String errorMsg = "Customised SMS sending failed: " + e.getMessage();
            System.out.println(errorMsg);
            return errorMsg; // ✅ return error message
        }
    }
}
