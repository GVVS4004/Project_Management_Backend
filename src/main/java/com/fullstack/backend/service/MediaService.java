package com.fullstack.backend.service;


import com.fullstack.backend.dto.response.MediaUploadResponse;
import com.fullstack.backend.entity.Media;
import com.fullstack.backend.exception.FileSizeExceededException;
import com.fullstack.backend.exception.InvalidFileException;
import com.fullstack.backend.exception.ResourceNotFoundException;
import com.fullstack.backend.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MediaService {

    private static final Logger logger = LoggerFactory.getLogger(MediaService.class);
    private final MediaRepository mediaRepository;

    private static final long MAX_FILE_SIZE = 5*1024*1024;

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/gif",
            "image/webp"
    );


    public MediaUploadResponse uploadProfileImage(MultipartFile file , Long userId) throws IOException{
        logger.info("Uploading profile image for user ID:{}", userId);

        validateFile(file);

        if(mediaRepository.existsByUserId(userId)){
            logger.info("Deleting existing profile image for user ID: {}", userId);
            mediaRepository.deleteByUserId(userId);
        }

        Media media = new Media();
        media.setFileName(file.getOriginalFilename());
        media.setFileType(file.getContentType());
        media.setFileSize(file.getSize());
        media.setData(file.getBytes());
        media.setUserId(userId);

        Media savedMedia = mediaRepository.save(media);
        logger.info("Profile image uploaded successfully with ID: {}", savedMedia.getId());

        return convertToResponse(savedMedia);

    }

    public Media getProfileImageByUserId(Long userId){
        logger.info("Fetching profile image for user ID: {}", userId);
        return mediaRepository.findByUserId(userId).orElseThrow(()-> new ResourceNotFoundException("Profile image for the user with user ID: "+userId));
    }

    public void deleteProfileImage(Long userId){
        logger.info("Deleting profile image for user ID: {}", userId);
        if(!mediaRepository.existsByUserId(userId)){
            throw new ResourceNotFoundException("Profile image not found for user ID: "+userId);
        }
        mediaRepository.deleteByUserId(userId);
    }

    private void validateFile(MultipartFile file){
        if(file.isEmpty()){
            throw new InvalidFileException("File is empty");
        }

        if(file.getSize() > MAX_FILE_SIZE){
            throw new FileSizeExceededException(String.format("File size exceeds maximum allowed size of %d MB", MAX_FILE_SIZE/(1024*1024)));
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new InvalidFileException(
                    "Invalid file type. Allowed types: " + String.join(", ", ALLOWED_CONTENT_TYPES)
            );
        }
        logger.info("File validation passed: {}", file.getOriginalFilename());
    }

    private MediaUploadResponse convertToResponse(Media media) {
        MediaUploadResponse response = new MediaUploadResponse();
        response.setId(media.getId());
        response.setFileName(media.getFileName());
        response.setFileType(media.getFileType());
        response.setFileSize(media.getFileSize());
        response.setDownloadUrl("/api/media/profile-image/" + media.getUserId());
        response.setUploadedAt(media.getCreatedAt());
        return response;
    }
}
