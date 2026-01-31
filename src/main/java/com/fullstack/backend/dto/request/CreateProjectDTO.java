package com.fullstack.backend.dto.request;

import com.fullstack.backend.entity.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectDTO {
    @NotBlank(message = "Project name is required")
    @Size(max = 100)
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
