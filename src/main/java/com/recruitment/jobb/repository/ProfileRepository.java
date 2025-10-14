package com.recruitment.jobb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.recruitment.jobb.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByUserId(Long userId);
}
