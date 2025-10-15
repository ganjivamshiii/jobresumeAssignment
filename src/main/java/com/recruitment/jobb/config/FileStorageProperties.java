package com.recruitment.jobb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Component
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private String uploadDir;
    private long maxFileSize;
    private String[] allowedFileTypes;

    public FileStorageProperties() {
        this.uploadDir = "./uploads";
        this.maxFileSize = 10485760; // 10MB default
        this.allowedFileTypes = new String[]{"application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String[] getAllowedFileTypes() {
        return allowedFileTypes;
    }

    public void setAllowedFileTypes(String[] allowedFileTypes) {
        this.allowedFileTypes = allowedFileTypes;
    }
}