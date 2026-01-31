package com.fullstack.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private UserResponseDTO user;

    public AuthResponse(String accessToken, String refreshToken, Long expiresIn, UserResponseDTO user){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
        this.expiresIn = expiresIn;
        this.user = user;
    }

    public AuthResponse withoutRefreshToken(String accessToken, Long expiresIn, UserResponseDTO user){
        return new AuthResponse(accessToken, null, expiresIn, user);
    }

    public static AuthResponseBuilder builder(){
        return new AuthResponseBuilder();
    }

    public static class AuthResponseBuilder{
        private String accessToken;
        private String refreshToken;
        private Long expiresIn;
        private UserResponseDTO user;

        public AuthResponseBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public AuthResponseBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public AuthResponseBuilder expiresIn(Long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public AuthResponseBuilder user(UserResponseDTO user) {
            this.user = user;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(accessToken, refreshToken, expiresIn, user);
        }

    }
}
