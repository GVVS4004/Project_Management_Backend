package com.fullstack.backend.service;

import com.fullstack.backend.entity.EditorMedia;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface EditorMediaService {

    EditorMedia upload(MultipartFile file);

    String getPresignedUrl(UUID mediaId);

    void linkMediaToTask(Long taskId, String htmlContent);

    void linkMediaToComment(Long commentId, String htmlContent);

    List<UUID> extractMediaIds(String htmlContent);
}