package com.fullstack.backend.service;

import com.fullstack.backend.entity.EditorMedia;
import com.fullstack.backend.entity.User;
import com.fullstack.backend.exception.InvalidFileException;
import com.fullstack.backend.exception.ResourceNotFoundException;
import com.fullstack.backend.repository.CommentRepository;
import com.fullstack.backend.repository.EditorMediaRepository;
import com.fullstack.backend.repository.TaskRepository;
import com.fullstack.backend.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
@Transactional
public class EditorMediaServiceImpl implements EditorMediaService{

    private final EditorMediaRepository editorMediaRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final FileUploadService fileUploadService;

    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofMinutes(30);

    private static final Pattern MEDIA_UUID_PATTERN =
            Pattern.compile("/media/([0-9a-fA-F\\-]{36})");

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(                                                                                                                                                                                     "image/png",
            "image/jpeg",
            "image/gif",
            "image/webp",
            "image/svg+xml"
    );

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    @Override
    public EditorMedia upload(MultipartFile file) {
        User currentUser = SecurityUtils.getCurrentUser();

        UUID mediaId = UUID.randomUUID();
        String extension = getExtension(file.getOriginalFilename());
        String storageKey = "editor/media/" + mediaId + extension;


        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new InvalidFileException("Only images are allowed (PNG, JPEG, GIF, WebP, SVG)");
        }

        try {
            fileUploadService.upload(storageKey, file);
        }
        catch (IOException e){
            throw new RuntimeException("Failed to upload media: " + e.getMessage(),e);
        }

        EditorMedia media = new EditorMedia();
        media.setStorageKey(storageKey);
        media.setContentType(file.getContentType());
        media.setFileSize(file.getSize());
        media.setUploadedBy(currentUser);

        return editorMediaRepository.save(media);
    }

    @Override
    @Transactional(readOnly = true)
    public String getPresignedUrl(UUID mediaId) {
        EditorMedia media = editorMediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + mediaId));
        return fileUploadService.generatePresignedUrl(media.getStorageKey(), PRESIGNED_URL_EXPIRATION);
    }

    @Override
    public void linkMediaToTask(Long taskId, String htmlContent) {
        if (htmlContent == null || htmlContent.isBlank()){
            List<EditorMedia> existing = editorMediaRepository.findByTaskIdAndCommentIdIsNull(taskId);
            for (EditorMedia media : existing) {
                media.setTask(null);
            }
            editorMediaRepository.saveAll(existing);
            return;
        }
        List<UUID> currentIds = extractMediaIds(htmlContent);
        List<EditorMedia> previouslyLinked = editorMediaRepository.findByTaskIdAndCommentIdIsNull(taskId);
        for (EditorMedia media : previouslyLinked) {
            if (!currentIds.contains(media.getId())) {
                media.setTask(null);
                fileUploadService.delete(media.getStorageKey());
                editorMediaRepository.delete(media);
            }
        }

        if (!currentIds.isEmpty()){
            List<EditorMedia> toLink = editorMediaRepository.findByIdIn(currentIds);
            var task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
            for (EditorMedia media : toLink) {
                if (media.getTask() == null) {
                    media.setTask(task);
                }
            }
            editorMediaRepository.saveAll(toLink);
        }
    }

    @Override
    public void linkMediaToComment(Long commentId, String htmlContent) {
        if (htmlContent == null || htmlContent.isBlank()) {
            List<EditorMedia> existing = editorMediaRepository.findByCommentId(commentId);
            for (EditorMedia media : existing) {
                media.setComment(null);
                fileUploadService.delete(media.getStorageKey());
                editorMediaRepository.delete(media);
            }
            return;
        }

        List<UUID> currentIds = extractMediaIds(htmlContent);
        List<EditorMedia> previouslyLinked = editorMediaRepository.findByCommentId(commentId);
        for (EditorMedia media : previouslyLinked) {
            if (!currentIds.contains(media.getId())) {
                media.setComment(null);
                fileUploadService.delete(media.getStorageKey());
                editorMediaRepository.delete(media);
            }
        }
        if (!currentIds.isEmpty()) {
            List<EditorMedia> toLink = editorMediaRepository.findByIdIn(currentIds);
            var comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
            for (EditorMedia media : toLink) {
                if (media.getComment() == null) {
                    media.setComment(comment);
                }
            }
            editorMediaRepository.saveAll(toLink);
        }
    }

    @Override
    public List<UUID> extractMediaIds(String htmlContent) {
        if (htmlContent == null || htmlContent.isBlank()) {
            return Collections.emptyList();
        }
        List<UUID> ids = new ArrayList<>();
        Matcher matcher = MEDIA_UUID_PATTERN.matcher(htmlContent);
        while (matcher.find()) {
            ids.add(UUID.fromString(matcher.group(1)));
        }
        return ids;
    }
}
