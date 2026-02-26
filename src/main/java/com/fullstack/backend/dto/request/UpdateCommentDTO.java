package com.fullstack.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentDTO {
    @Size(max = 5000, message = "The content size should be less than 5000 characters")
    @NotBlank
    private String content;
}
