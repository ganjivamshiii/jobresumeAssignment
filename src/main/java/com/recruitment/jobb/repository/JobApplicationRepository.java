package com.recruitment.jobb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.recruitment.jobb.model.JobApplication;


@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    boolean existsByJobIdAndApplicantId(Long jobId, Long applicantId);
    List<JobApplication> findByJobId(Long jobId);
    List<JobApplication> findByApplicantId(Long applicantId);
    Optional<JobApplication> findByJobIdAndApplicantId(Long jobId, Long applicantId);
}
