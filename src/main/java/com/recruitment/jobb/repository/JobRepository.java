package com.recruitment.jobb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.recruitment.jobb.model.Job;
public interface JobRepository  extends JpaRepository<Job,Long>{
}
