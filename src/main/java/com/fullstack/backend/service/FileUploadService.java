package com.fullstack.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;

public interface FileUploadService {
    void upload(String storageKey, MultipartFile file) throws IOException;
    String generatePresignedUrl(String storageKey, Duration expiration);
    void delete(String storageKey);
}
