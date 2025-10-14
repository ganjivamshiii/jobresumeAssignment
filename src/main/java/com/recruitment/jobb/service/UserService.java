package com.recruitment.jobb.service;

import com.recruitment.jobb.exception.ResourceNotFoundException;
import com.recruitment.jobb.model.Profile;
import com.recruitment.jobb.model.User;
import com.recruitment.jobb.model.User.UserType;
import com.recruitment.jobb.repository.ProfileRepository;
import com.recruitment.jobb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ResumeParserService resumeParserService;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public Profile uploadResume(MultipartFile file) {
        User currentUser = getCurrentUser();
        
        if (currentUser.getUserType() != User.UserType.APPLICANT) {
            throw new RuntimeException("Only applicants can upload resumes");
        }

        String filename = fileStorageService.storeFile(file);
        Path filePath = fileStorageService.getFilePath(filename);

        Map<String, String> parsedData = resumeParserService.parseResume(filePath);

        Profile profile = profileRepository.findByUserId(currentUser.getId())
                .orElse(new Profile());
        
        profile.setUser(currentUser);
        profile.setResumeFileAddress(filename);
        profile.setName(parsedData.getOrDefault("name", ""));
        profile.setEmail(parsedData.getOrDefault("email", ""));
        profile.setPhone(parsedData.getOrDefault("phone", ""));
        profile.setSkills(parsedData.getOrDefault("skills", ""));
        profile.setEducation(parsedData.getOrDefault("education", ""));
        profile.setExperience(parsedData.getOrDefault("experience", ""));

        return profileRepository.save(profile);
    }

    public List<User> getAllApplicants() {
        return userRepository.findByUserType(User.UserType.APPLICANT);
    }

    public Profile getApplicantProfile(Long applicantId) {
        User user = userRepository.findById(applicantId)
                .orElseThrow(() -> new ResourceNotFoundException("Applicant not found"));
        
        if (user.getUserType() != User.UserType.APPLICANT) {
            throw new RuntimeException("User is not an applicant");
        }

        return profileRepository.findByUserId(applicantId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for this applicant"));
    }
}