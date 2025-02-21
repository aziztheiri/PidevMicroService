package com.example.pidevmicroservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InfobipSmsService {

    @Value("${infobip.api.key}")
    private String apiKey;

    @Value("${infobip.base.url}")
    private String baseUrl;

    @Value("${infobip.sender}")
    private String sender;

    private final RestTemplate restTemplate;
    @Async
    public void sendSms(String toPhoneNumber, String text) {
        String url = baseUrl + "/sms/1/text/single";

        // Prepare HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "App " + apiKey);  // Infobip expects "App <API_KEY>"

        // Prepare payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("from", sender);
        payload.put("to", toPhoneNumber);
        payload.put("text", text);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            // You can log or process the response if needed
            System.out.println("SMS sent successfully: " + response.getBody());
        } catch (Exception ex) {
            // Log the exception; do not block main flow if sending SMS is not critical
            System.err.println("Error sending SMS: " + ex.getMessage());
        }
    }
}

