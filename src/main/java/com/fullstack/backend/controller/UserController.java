package com.fullstack.backend.controller;


import com.fullstack.backend.dto.request.ChangePasswordDTO;
import com.fullstack.backend.dto.request.UpdateProfileDTO;
import com.fullstack.backend.dto.response.MessageResponse;
import com.fullstack.backend.dto.response.UserResponseDTO;
import com.fullstack.backend.entity.Role;
import com.fullstack.backend.security.CustomUserDetails;
import com.fullstack.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor()
@Tag(name="User Management", description = "User management endpoints with role-based access control")
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get all users",
            description = "Admin only - Retrieve all users in the system",
            security = @SecurityRequirement(name="bearerAuth")
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
            }
    )
    public ResponseEntity<com.fullstack.backend.dto.response.ApiResponse<List<UserResponseDTO>>> getAllUsers(){
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(com.fullstack.backend.dto.response.ApiResponse.success("Users retrieved successfully",users));
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Search users",
            description = "Search users by username, first name, last name, or email. Authenticated users can search for other users to add to projects.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Search completed successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
            }
    )
    public ResponseEntity<com.fullstack.backend.dto.response.ApiResponse<List<UserResponseDTO>>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "20") int limit
    ) {
        List<UserResponseDTO> users = userService.searchUsers(query, limit);
        return ResponseEntity.ok(
                com.fullstack.backend.dto.response.ApiResponse.success("Users found", users)
        );
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Get current user profile",
            description = "Any authenticated user can view their own profile",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<com.fullstack.backend.dto.response.ApiResponse<UserResponseDTO>> getCurrentUser() {
        UserResponseDTO user = userService.getCurrentUser();
        return ResponseEntity.ok(
                com.fullstack.backend.dto.response.ApiResponse.success("Profile retrieved successfully", user)
        );
    }


    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Get user by ID",
            description = "Users can view their own profile, Admins can view any profile",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Cannot access this user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<com.fullstack.backend.dto.response.ApiResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(
                com.fullstack.backend.dto.response.ApiResponse.success("User retrieved successfully", user)
        );
    }


    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update user role",
            description = "Admin only - Change a user's role",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<com.fullstack.backend.dto.response.ApiResponse<UserResponseDTO>> updateUserRole(
            @PathVariable Long id,
            @RequestParam Role role
    ) {
        UserResponseDTO user = userService.updateUserRole(id, role);
        return ResponseEntity.ok(
                com.fullstack.backend.dto.response.ApiResponse.success("User role updated successfully", user)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.isOwnerOrAdmin(#id)")
    @Operation(
            summary = "Delete user",
            description = "Admin can delete any user, users can delete themselves",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<com.fullstack.backend.dto.response.ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(
                com.fullstack.backend.dto.response.ApiResponse.success("User deleted successfully", null)
        );
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(
            summary = "Get users by role",
            description = "Admin and Manager can filter users by role",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Manager access required")
    })
    public ResponseEntity<com.fullstack.backend.dto.response.ApiResponse<List<UserResponseDTO>>> getUsersByRole(@PathVariable Role role)
    {
        List<UserResponseDTO> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(
                com.fullstack.backend.dto.response.ApiResponse.success("Users retrieved successfully", users)
        );
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN','MEMBER)")
    @Operation(
            summary = "Update profile",
            description = "Updates the authenticated user's profile information"
    )
    public ResponseEntity<com.fullstack.backend.dto.response.ApiResponse<UserResponseDTO>> updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody UpdateProfileDTO updateProfileDTO){
        UserResponseDTO updatedUser = userService.updateProfile(userDetails.getUser().getId(), updateProfileDTO);

        return ResponseEntity.ok(com.fullstack.backend.dto.response.ApiResponse.success("Profile updated successfully", updatedUser));
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasAnyRole('ADMIN','MEMBER'")
    @Operation(
            summary = "Update profile password",
            description = "Updated the authenticated user's profile password"
    )
    public ResponseEntity<com.fullstack.backend.dto.response.ApiResponse<MessageResponse>> updateProfilePassword(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody ChangePasswordDTO changePasswordDTO){
        userService.updateProfilePassword(userDetails.getUser().getId(), changePasswordDTO);
        MessageResponse message = MessageResponse.of("Password changed successfully");
        return ResponseEntity.ok( com.fullstack.backend.dto.response.ApiResponse.success("Password updated successfully",message));
    }
}
