package com.fullstack.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditorMediaResponseDTO {
    private UUID id;
    private String url;
    private String contentType;
    private Long fileSize;
}