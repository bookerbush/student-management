package com.schoolapp.student_management;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class UMSSender {

    public void sendSMS(String phone, String studentName, double balance) {
        String username = "vicky";
        String password = "vicky2324";
        String apiKey = "9ecaf1a0db37a3935462c3238e8608e3";
        String appId = "UMSC770525";
        String senderId = "UMS_SMS";
        String email = "gylmueni21@gmail.com";

        try {
            String message = "Hello " + studentName + ",This is Kelvin's Academy, your fee balance is " + balance + ". Kindly clear it.(this is BOB,testing bulk sms,kindly ignore the sms)";

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

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(payload);
            writer.flush();
            writer.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println("UMS Response: " + response);
            }
            reader.close();

            conn.disconnect();
        } catch (Exception e) {
            System.out.println("SMS sending failed: " + e.getMessage());
        }
    }
}
