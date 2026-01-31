package com.fullstack.backend.controller;


import com.fullstack.backend.dto.response.ApiResponse;
import com.fullstack.backend.dto.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Tag(name = "Test", description = "Test endpoints for API verification")
public class TestController {

    private final PasswordEncoder passwordEncoder;
    @Operation(
            summary = "Health check endpoint",
            description = "Check if the API is running and accessible"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "API is running",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(
                ApiResponse.success("API is running successfully!")
        );
    }

    @Operation(
            summary = "Protected endpoint example",
            description = "Example of a protected endpoint that requires JWT authentication",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/protected")
    public ResponseEntity<ApiResponse<String>> protectedEndpoint() {
        return ResponseEntity.ok(
                ApiResponse.success("You accessed a protected endpoint!")
        );
    }

    @GetMapping("/admin-only")
    @PreAuthorize("hasRole('Admin')")
    @Operation(summary = "Admin only endpoint", security = @SecurityRequirement(name= "bearerAuth"))
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200" , description = "Success- Admin access granted"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Not an admin")
    })
    public ResponseEntity<ApiResponse<String>> adminOnly(){
        return ResponseEntity.ok(ApiResponse.success("Welcome Admin! You have full access.", null));
    }

    @GetMapping("/manager-or-admin")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Manager or Admin endpoint", security = @SecurityRequirement(name =
            "bearerAuth"))
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success - Manager or Admin access"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    public ResponseEntity<ApiResponse<String>> managerOrAdmin() {
        return ResponseEntity.ok(
                ApiResponse.success("Welcome Manager/Admin!", null)
        );
    }

    @GetMapping("/member-only")
    @PreAuthorize("hasRole('MEMBER')")
    @Operation(summary = "Member only endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<String>> memberOnly() {
        return ResponseEntity.ok(
                ApiResponse.success("Welcome Member!", null)
        );
    }

    @GetMapping("/authenticated")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Any authenticated user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<String>> authenticatedOnly() {
        return ResponseEntity.ok(
                ApiResponse.success("Any authenticated user can access this", null)
        );
    }

    @GetMapping("/hash-password")
    @Operation(
        summary = "Generate BCrypt password hash",
        description = "Utility endpoint to generate BCrypt hashes for seed data. Use this to create valid password hashes for your SQL migrations."
    )
    public ResponseEntity<ApiResponse<String>> generatePasswordHash(
            @RequestParam(defaultValue = "password123") String password) {
        String hash = passwordEncoder.encode(password);
        return ResponseEntity.ok(
                ApiResponse.success("Password hash generated successfully. Use this in your SQL migration files.", hash)
        );
    }
}
