package com.fullstack.backend.dto.request;

import com.fullstack.backend.entity.ProjectRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberRoleDTO {

    @NotNull(message = "Role is required")
    private ProjectRole role;
}
