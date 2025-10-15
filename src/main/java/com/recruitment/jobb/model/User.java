package com.recruitment.jobb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
     @Id
     @GeneratedValue(strategy=GenerationType.IDENTITY)
     private Long id;

     @Column(nullable=false)
     private String name;

     @Column(nullable =false)
     private String email;

     @Column(nullable=false, unique=true)
     private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private UserType userType;

    @Column(nullable= false)
    private String passwordHash;

    @Column(name="profile_headline")
    private String profileHeadline;

    @OneToOne(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval=true)
    private Profile profile;

    @Column(name="created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate(){
        createdAt=LocalDateTime.now();
    
    }
     public enum UserType{
        ADMIN,
        APPLICANT
     }
}
