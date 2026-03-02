package com.fullstack.backend.controller;

import com.fullstack.backend.dto.response.ApiResponse;
import com.fullstack.backend.dto.response.EditorMediaResponseDTO;
import com.fullstack.backend.entity.EditorMedia;
import com.fullstack.backend.service.EditorMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class EditorMediaController {
    private final EditorMediaService editorMediaService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<EditorMediaResponseDTO>> upload(
            @RequestParam("file")MultipartFile file
            ){
        EditorMedia media = editorMediaService.upload(file);
        EditorMediaResponseDTO responseDTO = new EditorMediaResponseDTO();

        responseDTO.setId(media.getId());
        responseDTO.setUrl("/media/" + media.getId());
        responseDTO.setContentType(media.getContentType());
        responseDTO.setFileSize(media.getFileSize());

        return ResponseEntity.ok(ApiResponse.success("Media uploaded successfully", responseDTO));
    }

    @GetMapping("/{mediaId}")
    public ResponseEntity<Void> serve(@PathVariable UUID mediaId) {
        String presignedUrl = editorMediaService.getPresignedUrl(mediaId);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(presignedUrl))
                .build();
    }
}
