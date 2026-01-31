package com.fullstack.backend.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDTO {

    @Size(max = 50, message = "Username must not exceed 50 characters")
    private String userName;

    @Size(max= 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @Size(max= 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email must not excees 255 characters")
    private String email;

    @Size(max = 255, message = "Profile image URL must not exceed 255 characters")
    private String profileImageUrl;
}
