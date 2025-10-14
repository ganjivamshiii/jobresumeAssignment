package com.recruitment.jobb.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.recruitment.jobb.dto.ApiResponse;
import com.recruitment.jobb.model.Job;
import com.recruitment.jobb.model.JobApplication;
import com.recruitment.jobb.model.Profile;
import com.recruitment.jobb.service.JobService;
import com.recruitment.jobb.service.UserService;

@RestController
@RequestMapping
public class ApplicantController {

    @Autowired
    private UserService userService;

    @Autowired
    private JobService jobService;

    @PostMapping("/uploadResume")
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<ApiResponse> uploadResume(@RequestParam("file") MultipartFile file) {
        Profile profile = userService.uploadResume(file);
        return ResponseEntity.ok(new ApiResponse(true, "Resume uploaded and processed successfully", profile));
    }

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(new ApiResponse(true, "Jobs fetched successfully", jobs));
    }

    @GetMapping("/jobs/apply")
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<ApiResponse> applyToJob(@RequestParam("job_id") Long jobId) {
        JobApplication application = jobService.applyToJob(jobId);
        return ResponseEntity.ok(new ApiResponse(true, "Applied to job successfully", application));
    }
}