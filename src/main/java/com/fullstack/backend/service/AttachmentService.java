package com.fullstack.backend.service;

import com.fullstack.backend.dto.response.AttachmentResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {
    AttachmentResponseDTO uploadAttachment(Long taskId, MultipartFile file);
    List<AttachmentResponseDTO> getAttachmentsByTaskId(Long taskId);
    String getDownloadUrl(Long attachmentId);
    void deleteAttachment(Long attachmentId);
}
