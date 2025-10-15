package com.recruitment.jobb.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", "Unauthorized - Authentication token is missing or invalid");
        errorDetails.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        errorDetails.put("error", authException.getClass().getSimpleName());
        errorDetails.put("timestamp", System.currentTimeMillis());
        errorDetails.put("path", request.getRequestURI());
        
        response.getWriter().write(convertErrorToJson(errorDetails));
    }

    private String convertErrorToJson(Map<String, Object> errorDetails) {
        StringBuilder json = new StringBuilder("{");
        errorDetails.forEach((key, value) -> {
            json.append("\"").append(key).append("\":");
            if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else {
                json.append(value);
            }
            json.append(",");
        });
        json.deleteCharAt(json.length() - 1);
        json.append("}");
        return json.toString();
    }
}