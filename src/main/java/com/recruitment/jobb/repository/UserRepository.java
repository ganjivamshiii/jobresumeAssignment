package com.recruitment.jobb.repository;

import org.springframework.stereotype.Repository;
import com.recruitment.jobb.model.User;
import com.recruitment.jobb.model.User.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByUserType(UserType userType);
}

