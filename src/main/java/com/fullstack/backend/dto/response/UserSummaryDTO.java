package com.fullstack.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryDTO {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String profileImageUrl;

}
