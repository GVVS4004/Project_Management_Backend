    package com.fullstack.backend.controller;

    import com.fullstack.backend.dto.request.UserLoginDTO;
    import com.fullstack.backend.dto.request.UserRegistrationDTO;
    import com.fullstack.backend.dto.response.ApiResponse;
    import com.fullstack.backend.dto.response.AuthResponse;
    import com.fullstack.backend.dto.response.UserResponseDTO;
    import com.fullstack.backend.service.AuthService;
    import jakarta.servlet.http.Cookie;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.http.ResponseCookie;
    import org.springframework.http.HttpHeaders;


    @RestController
    @RequestMapping("/auth")
    @RequiredArgsConstructor
    public class AuthController {

        private final AuthService authService;

        @PostMapping("/register")
        public ResponseEntity<ApiResponse<UserResponseDTO>> register(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO, HttpServletResponse response, HttpServletRequest request){

            try{
                AuthResponse authResponse = authService.register(userRegistrationDTO, request);
                setAuthCookie(response, authResponse.getAccessToken());
                setRefreshTokenCookie(response, authResponse.getRefreshToken());
                return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("User registered successfully", authResponse.getUser()));
            }
            catch(RuntimeException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
            }
        }

        /**
         * Login user
         *
         * POST /api/auth/login
         *
         * @param loginDTO User login credentials
         * @return AuthResponse with JWT token and user info
         */
        @PostMapping("/login")
        public ResponseEntity<ApiResponse<UserResponseDTO>> login(@Valid @RequestBody UserLoginDTO loginDTO, HttpServletResponse response, HttpServletRequest request) {
            try {
                AuthResponse authResponse = authService.login(loginDTO, request);
                setAuthCookie(response, authResponse.getAccessToken());
                setRefreshTokenCookie(response, authResponse.getRefreshToken());
                System.out.println(authResponse);
                return ResponseEntity.ok(ApiResponse.success("Login successfull", authResponse.getUser()));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Invalid email or password"));
            }
        }

        /**
         * Logout user
         *
         * POST /api/auth/logout
         *
         * Note: With JWT, logout is handled client-side by removing the token
         * This endpoint is for consistency and can be extended later
         * (e.g., token blacklisting, refresh token invalidation)
         *
         * @return Success message
         */
        @PostMapping("/logout")
        public ResponseEntity<ApiResponse> logout(HttpServletRequest request, HttpServletResponse response) {
            try {
                // Get refresh token from cookie
                String refreshToken = getRefreshTokenFromCookie(request);

                // Delete refresh token from database
                authService.logout(refreshToken);

                // Clear cookies from browser
                clearAuthCookies(response);

                return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
            } catch (Exception e) {
                // Even if there's an error, clear the cookies
                clearAuthCookies(response);
                return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
            }
        }

        @PostMapping("/refresh")
        public ResponseEntity<ApiResponse<UserResponseDTO>> refreshToken(HttpServletRequest request,
                                                                         HttpServletResponse response) {
            try {
                // Read refresh token from cookie
                String refreshToken = getRefreshTokenFromCookie(request);

                if (refreshToken == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(ApiResponse.error("Refresh token not found"));
                }

                // Generate new access token
                AuthResponse authResponse = authService.refreshAccessToken(refreshToken);

                // Set new access token cookie
                setAuthCookie(response, authResponse.getAccessToken());

                return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully",
                        authResponse.getUser()));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Invalid or expired refresh token"));
            }
        }

        @GetMapping("/verify")
        public ResponseEntity<ApiResponse<String>> verify(){
            return ResponseEntity.ok(ApiResponse.success("Token is valid","authenticated"));
        }
        /**
         * Health check endpoint
         *
         * GET /api/auth/health
         *
         * @return Status message
         */
        @GetMapping("/health")
        public ResponseEntity<ApiResponse> health() {
            return ResponseEntity.ok(ApiResponse.success("Auth service is running"));
        }


        private void setAuthCookie(HttpServletResponse response, String token){
            ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                    .httpOnly(true)
                    .secure(false)  // Set to true in production with HTTPS
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .sameSite("Lax")  // This is the key fix!
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }

        private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false)  // Set to true in production with HTTPS
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)  // 7 days
                    .sameSite("Lax")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        private void clearAuthCookies(HttpServletResponse response) {
            // Clear access token
            ResponseCookie accessCookie = ResponseCookie.from("accessToken", "")
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(0)
                    .sameSite("Lax")
                    .build();

            // Clear refresh token
            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(0)
                    .sameSite("Lax")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        }

        private String getRefreshTokenFromCookie(HttpServletRequest request) {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("refreshToken".equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
            return null;
        }
    }
