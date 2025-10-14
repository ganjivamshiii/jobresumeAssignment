package com.recruitment.jobb.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.recruitment.jobb.dto.ApiResponse;
import com.recruitment.jobb.dto.JobRequest;
import com.recruitment.jobb.model.Job;
import com.recruitment.jobb.model.JobApplication;
import com.recruitment.jobb.model.Profile;
import com.recruitment.jobb.model.User;
import com.recruitment.jobb.service.JobService;
import com.recruitment.jobb.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private JobService jobService;

    @Autowired
    private UserService userService;

    @PostMapping("/job")
    public ResponseEntity<ApiResponse> createJob(@Valid @RequestBody JobRequest jobRequest) {
        Job job = jobService.createJob(jobRequest);
        return ResponseEntity.ok(new ApiResponse(true, "Job created successfully", job));
    }

    @GetMapping("/job/{job_id}")
    public ResponseEntity<ApiResponse> getJobDetails(@PathVariable("job_id") Long jobId) {
        Job job = jobService.getJobById(jobId);
        List<JobApplication> applications = jobService.getJobApplications(jobId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("job", job);
        response.put("applications", applications);
        
        return ResponseEntity.ok(new ApiResponse(true, "Job details fetched successfully", response));
    }

    @GetMapping("/applicants")
    public ResponseEntity<ApiResponse> getAllApplicants() {
        List<User> applicants = userService.getAllApplicants();
        return ResponseEntity.ok(new ApiResponse(true, "Applicants fetched successfully", applicants));
    }

    @GetMapping("/applicant/{applicant_id}")
    public ResponseEntity<ApiResponse> getApplicantProfile(@PathVariable("applicant_id") Long applicantId) {
        Profile profile = userService.getApplicantProfile(applicantId);
        return ResponseEntity.ok(new ApiResponse(true, "Applicant profile fetched successfully", profile));
    }
}