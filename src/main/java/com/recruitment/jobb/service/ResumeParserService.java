package com.recruitment.jobb.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class ResumeParserService {

    @Value("${resume.parser.api.url}")
    private String apiUrl;

    @Value("${resume.parser.api.key}")
    private String apiKey;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, String> parseResume(Path filePath) {
        try {
            byte[] fileBytes = Files.readAllBytes(filePath);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/octet-stream")
                    .header("apikey", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(fileBytes))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return extractDataFromResponse(response.body());
            } else {
                throw new RuntimeException("Resume parsing failed: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error parsing resume: " + e.getMessage());
        }
    }

    private Map<String, String> extractDataFromResponse(String responseBody) {
        Map<String, String> extractedData = new HashMap<>();
        
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            
            extractedData.put("name", root.path("name").asText(""));
            extractedData.put("email", root.path("email").asText(""));
            extractedData.put("phone", root.path("phone").asText(""));
            extractedData.put("skills", extractArrayAsString(root.path("skills")));
            extractedData.put("education", extractEducation(root.path("education")));
            extractedData.put("experience", extractExperience(root.path("experience")));
            
        } catch (Exception e) {
            throw new RuntimeException("Error extracting data from resume: " + e.getMessage());
        }
        
        return extractedData;
    }

    private String extractArrayAsString(JsonNode arrayNode) {
        if (arrayNode.isArray()) {
            StringBuilder result = new StringBuilder();
            arrayNode.forEach(node -> {
                if (result.length() > 0) result.append(", ");
                result.append(node.asText());
            });
            return result.toString();
        }
        return "";
    }

    private String extractEducation(JsonNode educationNode) {
        if (educationNode.isArray()) {
            StringBuilder result = new StringBuilder();
            educationNode.forEach(node -> {
                if (result.length() > 0) result.append("; ");
                result.append(node.path("name").asText(""));
            });
            return result.toString();
        }
        return "";
    }

    private String extractExperience(JsonNode experienceNode) {
        if (experienceNode.isArray()) {
            StringBuilder result = new StringBuilder();
            experienceNode.forEach(node -> {
                if (result.length() > 0) result.append("; ");
                result.append(node.path("name").asText(""));
            });
            return result.toString();
        }
        return "";
    }
}
