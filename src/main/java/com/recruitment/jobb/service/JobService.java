package com.recruitment.jobb.service;


import com.recruitment.jobb.dto.JobRequest;
import com.recruitment.jobb.exception.ResourceNotFoundException;
import com.recruitment.jobb.model.Job;
import com.recruitment.jobb.model.JobApplication;
import com.recruitment.jobb.model.User;
import com.recruitment.jobb.repository.JobApplicationRepository;
import com.recruitment.jobb.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Job createJob(JobRequest jobRequest) {
        User currentUser = userService.getCurrentUser();
        
        if (currentUser.getUserType() != User.UserType.ADMIN) {
            throw new RuntimeException("Only admin users can create jobs");
        }

        Job job = new Job();
        job.setTitle(jobRequest.getTitle());
        job.setDescription(jobRequest.getDescription());
        job.setCompanyName(jobRequest.getCompanyName());
        job.setPostedBy(currentUser);
        job.setTotalApplications(0);

        return jobRepository.save(job);
    }

    public Job getJobById(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<JobApplication> getJobApplications(Long jobId) {
        Job job = getJobById(jobId);
        return jobApplicationRepository.findByJobId(jobId);
    }

    @Transactional
    public JobApplication applyToJob(Long jobId) {
        User currentUser = userService.getCurrentUser();
        
        if (currentUser.getUserType() != User.UserType.APPLICANT) {
            throw new RuntimeException("Only applicants can apply to jobs");
        }

        Job job = getJobById(jobId);

        if (jobApplicationRepository.existsByJobIdAndApplicantId(jobId, currentUser.getId())) {
            throw new RuntimeException("You have already applied to this job");
        }

        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setApplicant(currentUser);

        JobApplication savedApplication = jobApplicationRepository.save(application);

        job.setTotalApplications(job.getTotalApplications() + 1);
        jobRepository.save(job);

        return savedApplication;
    }
}