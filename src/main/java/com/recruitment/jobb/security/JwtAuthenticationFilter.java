package com.recruitment.jobb.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            jakarta.servlet.FilterChain filterChain) throws ServletException, IOException {

        try {
            String authHeader = request.getHeader(AUTHORIZATION_HEADER);
            log.info("Authorization header: {}", authHeader);
            
            String token = extractTokenFromRequest(request);
            log.info("Extracted token: {}", token);

            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                processAuthenticationToken(token, request);
            }

        } catch (JwtException e) {
            log.error("JWT token processing failed: Invalid token format or signature", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT token processing failed: Invalid argument", e);
        } catch (Exception e) {
            log.error("Unexpected error during JWT authentication", e);
        }

        filterChain.doFilter(request, response);
    }

    private void processAuthenticationToken(String token, HttpServletRequest request) {
        try {
            log.debug("JWT token: {}", token);
            String username = jwtUtil.extractUsername(token);
            log.debug("Username extracted from token: {}", username);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails)) {
                log.debug("JWT token is valid for user: {}", username);
                setAuthentication(userDetails, request);
            } else {
                log.warn("JWT token validation failed for user: {}", username);
            }

        } catch (Exception e) {
            log.error("Error processing authentication token for user extraction", e);
        }
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("Authentication set in SecurityContext for user: {}", userDetails.getUsername());
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}