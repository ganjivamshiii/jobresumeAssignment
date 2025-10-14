package com.recruitment.jobb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "posted_on", nullable = false)
    private LocalDateTime postedOn;
    
    @Column(name = "total_applications")
    private Integer totalApplications = 0;
    
    @Column(name = "company_name")
    private String companyName;
    
    @ManyToOne
    @JoinColumn(name = "posted_by_user_id", nullable = false)
    private User postedBy;
    
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<JobApplication> applications = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        postedOn = LocalDateTime.now();
    }
}