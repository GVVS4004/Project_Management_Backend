package com.fullstack.backend.dto.request;


import com.fullstack.backend.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    @NotBlank(message = "Username is required")
    @Size(min=3 , max=50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be atleast 6 characters")
    private String password;

    @Size(max = 255, message = "Profile image URL cannot exceed 255 characters")
    private String profileImageUrl;

    private Role role = Role.MEMBER;
}
