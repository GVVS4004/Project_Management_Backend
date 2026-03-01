package com.fullstack.backend.service;

import com.fullstack.backend.dto.response.AttachmentResponseDTO;
import com.fullstack.backend.entity.Attachment;
import com.fullstack.backend.entity.Role;
import com.fullstack.backend.entity.Task;
import com.fullstack.backend.entity.User;
import com.fullstack.backend.exception.ForbiddenException;
import com.fullstack.backend.exception.ResourceNotFoundException;
import com.fullstack.backend.repository.AttachmentRepository;
import com.fullstack.backend.repository.TaskRepository;
import com.fullstack.backend.util.SecurityUtils;
import com.fullstack.backend.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AttachmentServiceImpl implements AttachmentService{

    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;
    private final FileUploadService fileUploadService;

    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofMinutes(30);

    private void checkCanDeleteAttachment(Attachment attachment) {
        User currentUser = SecurityUtils.getCurrentUser();
        boolean isUploader = attachment.getUploadedBy().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        if (!isUploader && !isAdmin) {
            throw new ForbiddenException("You can only delete your own attachments");
        }
    }

    private AttachmentResponseDTO convertToResponseDTO(Attachment attachment) {
        AttachmentResponseDTO dto = new AttachmentResponseDTO();
        dto.setId(attachment.getId());
        dto.setFileName(attachment.getFileName());
        dto.setFileSize(attachment.getFileSize());
        dto.setContentType(attachment.getContentType());
        dto.setTaskId(attachment.getTask().getId());
        dto.setUploadedBy(UserMapper.toSummaryDTO(attachment.getUploadedBy()));
        dto.setCreatedAt(attachment.getCreatedAt());
        return dto;
    }
    @Override
    public AttachmentResponseDTO uploadAttachment(Long taskId, MultipartFile file) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()-> new ResourceNotFoundException("Task not found with id: " + taskId));
        User currentUser = SecurityUtils.getCurrentUser();

        String storedFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String storageKey = "attachments/tasks/" + taskId + "/" + storedFileName;

        try {
            fileUploadService.upload(storageKey, file);
        }catch (IOException e){
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setStoredFileName(storedFileName);
        attachment.setFileSize(file.getSize());
        attachment.setContentType(file.getContentType());
        attachment.setStorageKey(storageKey);
        attachment.setTask(task);
        attachment.setUploadedBy(currentUser);

        attachmentRepository.save(attachment);
        return convertToResponseDTO(attachment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttachmentResponseDTO> getAttachmentsByTaskId(Long taskId) {
        if(!taskRepository.existsById(taskId)){
            throw new ResourceNotFoundException("Task not found with id: " + taskId);
        }
        return attachmentRepository.findByTaskIdOrderByCreatedAtDesc(taskId)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public String getDownloadUrl(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found with id: " + attachmentId));
        return fileUploadService.generatePresignedUrl(attachment.getStorageKey(), PRESIGNED_URL_EXPIRATION);
    }

    @Override
    public void deleteAttachment(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId).orElseThrow(()-> new ResourceNotFoundException("Attachment not found with id: "+ attachmentId));
        checkCanDeleteAttachment(attachment);

        fileUploadService.delete(attachment.getStorageKey());
        attachmentRepository.delete(attachment);
    }
}
