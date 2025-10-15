package com.recruitment.jobb.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.recruitment.jobb.dto.ApiResponse;
import com.recruitment.jobb.model.Job;
import com.recruitment.jobb.model.JobApplication;
import com.recruitment.jobb.model.Profile;
import com.recruitment.jobb.service.JobService;
import com.recruitment.jobb.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping
public class ApplicantController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicantController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JobService jobService;

@PostMapping("/uploadResume")
@PreAuthorize("hasRole('APPLICANT')")
public ResponseEntity<ApiResponse> uploadResume(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
    logger.info("uploadResume called with file: {}", file.getOriginalFilename());
    logger.info("Content-Type header: {}", request.getContentType());
    logger.info("Authorization header: {}", request.getHeader("Authorization"));
    logger.info("SecurityContext authentication: {}", SecurityContextHolder.getContext().getAuthentication());

    Profile profile = userService.uploadResume(file);
    logger.info("Resume uploaded successfully for user: {}", profile.getUser().getEmail());

    return ResponseEntity.ok(new ApiResponse(true, "Resume uploaded and processed successfully", profile));
}

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse> getAllJobs() {
        logger.info("getAllJobs called");
        List<Job> jobs = jobService.getAllJobs();
        logger.info("Number of jobs fetched: {}", jobs.size());
        return ResponseEntity.ok(new ApiResponse(true, "Jobs fetched successfully", jobs));
    }

    @GetMapping("/jobs/apply")
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<ApiResponse> applyToJob(@RequestParam("job_id") Long jobId) {
        logger.info("applyToJob called with jobId: {}", jobId);
        JobApplication application = jobService.applyToJob(jobId);
        logger.info("Applied to job successfully for user: {}", application.getApplicant().getEmail());
        return ResponseEntity.ok(new ApiResponse(true, "Applied to job successfully", application));
    }
}
