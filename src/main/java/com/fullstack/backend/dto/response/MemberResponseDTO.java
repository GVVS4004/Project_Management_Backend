package com.fullstack.backend.dto.response;

import com.fullstack.backend.entity.ProjectRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {

    private Long projectId;
    private UserSummaryDTO user;
    private ProjectRole role;
    private LocalDateTime joinedAt;
}
