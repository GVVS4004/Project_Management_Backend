package com.fullstack.backend.controller;

import com.fullstack.backend.dto.response.ApiResponse;
import com.fullstack.backend.dto.response.AttachmentResponseDTO;
import com.fullstack.backend.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping(value = "/{taskId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AttachmentResponseDTO>> uploadAttachment(
            @PathVariable Long taskId, @RequestParam("file") MultipartFile file){
        AttachmentResponseDTO responseDTO = attachmentService.uploadAttachment(taskId,file);
        return ResponseEntity.ok(ApiResponse.success("File uploaded successfully", responseDTO));
    }

    @GetMapping("/{taskId}/attachments")
    public ResponseEntity<ApiResponse<List<AttachmentResponseDTO>>> getAttachments(@PathVariable Long taskId){
        List<AttachmentResponseDTO> responseDTOS = attachmentService.getAttachmentsByTaskId(taskId);
        return ResponseEntity.ok(ApiResponse.success("Attachments fetched successfully", responseDTOS));
    }

    @GetMapping("/attachments/{attachmentId}/download")
    public ResponseEntity<ApiResponse<String>> getDownloadUrl(@PathVariable Long attachmentId) {
        String downloadUrl = attachmentService.getDownloadUrl(attachmentId);
        return ResponseEntity.ok(ApiResponse.success("Download URL generated", downloadUrl));
    }

    @DeleteMapping("/attachments/{attachmentId}")
    public ResponseEntity<ApiResponse<Void>> deleteAttachment(@PathVariable Long attachmentId) {
        attachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.ok(ApiResponse.success("Attachment deleted successfully", null));
    }
}
