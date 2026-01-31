package com.fullstack.backend.controller;

import com.fullstack.backend.dto.response.ApiResponse;
import com.fullstack.backend.dto.response.MediaUploadResponse;
import com.fullstack.backend.entity.Media;
import com.fullstack.backend.security.CustomUserDetails;
import com.fullstack.backend.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
@Tag(name= "Media Management", description = "Endpoints for uploading and managing media files")
public class MediaController {
    private final MediaService mediaService;


    @PostMapping("/profile-image")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Upload profile image",
            description = "Upload a profile image for the authenticated user",
            security = @SecurityRequirement(name= "bearerAuth")
    )
    public ResponseEntity<ApiResponse<MediaUploadResponse>> uploadProfileImage(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException{
        Long userId = userDetails.getUser().getId();
        MediaUploadResponse response = mediaService.uploadProfileImage(file, userId);

        return ResponseEntity.ok(ApiResponse.success("Profile image uploaded successfully", response));
    }

    @GetMapping("/profile-image/{userId}")
    @Operation(
            summary = "Get profile image",
            description = "Retrieve profile image for a specific user"
    )
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long userId){
        Media media = mediaService.getProfileImageByUserId(userId);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType(media.getFileType()));
        headers.setContentLength(media.getFileSize());
        headers.setContentDispositionFormData("inline", media.getFileName());

        return new ResponseEntity<>(media.getData(),headers, HttpStatus.OK);
    }

    @DeleteMapping("/profile-image")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Delete profile image",
            description = "Delete the authenticated user's profile image",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Void>> deleteProfileImage(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        mediaService.deleteProfileImage(userId);
        return ResponseEntity.ok(
                ApiResponse.success("Profile image deleted successfully", null)
        );
    }
}
