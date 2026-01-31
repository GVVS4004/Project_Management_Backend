package com.fullstack.backend.service;


import com.fullstack.backend.dto.request.UserLoginDTO;
import com.fullstack.backend.dto.request.UserRegistrationDTO;
import com.fullstack.backend.dto.response.AuthResponse;
import com.fullstack.backend.dto.response.UserResponseDTO;
import com.fullstack.backend.entity.RefreshToken;
import com.fullstack.backend.entity.User;
import com.fullstack.backend.exception.ResourceNotFoundException;
import com.fullstack.backend.repository.RefreshTokenRepository;
import com.fullstack.backend.repository.UserRepository;
import com.fullstack.backend.security.CustomUserDetails;
import com.fullstack.backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public AuthResponse register(UserRegistrationDTO registrationDTO, HttpServletRequest request){
        if(userRepository.existsByEmail(registrationDTO.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        if(userRepository.existsByUserName(registrationDTO.getUsername())){
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUserName(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setProfileImageUrl(registrationDTO.getProfileImageUrl());
        user.setRole(registrationDTO.getRole());
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        CustomUserDetails userDetails = new CustomUserDetails(savedUser);

        String token = jwtUtil.generateToken(userDetails);
        String deviceInfo = getDeviceInfo(request);
        String ipAddress = getIpAddress(request);
        RefreshToken refreshToken = createRefreshToken(userDetails, deviceInfo, ipAddress);

        UserResponseDTO userResponse = convertToUserResponseDTO(savedUser);
        return AuthResponse.builder()
                .accessToken(token)
                .user(userResponse)
                .expiresIn(expiration)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    /**
     * Authenticate user and generate JWT token
     *
     * @param loginDTO User login credentials
     * @return AuthResponse with JWT token and user info
     * @throws RuntimeException if authentication fails
     */
    public AuthResponse login(UserLoginDTO loginDTO,HttpServletRequest request) {
        // Authenticate user

        String userName = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(()-> new ResourceNotFoundException("User not found with email:"+loginDTO.getEmail())).getUserName();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userName,
                        loginDTO.getPassword()
                )
        );

        // Get authenticated user details
        CustomUserDetails userDetails = (CustomUserDetails)
                authentication.getPrincipal();
        User user = userDetails.getUser();

        // Generate JWT token
        String token = jwtUtil.generateToken(userDetails);
        String deviceInfo = getDeviceInfo(request);
        String ipAddress = getIpAddress(request);
        RefreshToken refreshToken = createRefreshToken(userDetails, deviceInfo, ipAddress);
        // Convert to response DTO
        UserResponseDTO userResponse = convertToUserResponseDTO(user);

        return AuthResponse.builder().accessToken(token).user(userResponse).expiresIn(expiration).refreshToken(refreshToken.getToken()).build();
    }


    @Transactional
    public void logout (String refreshTokenString){
        if(refreshTokenString!=null && !refreshTokenString.isEmpty()){
            refreshTokenRepository.deleteByToken(refreshTokenString);
        }
    }

    /**
     * Convert User entity to UserResponseDTO
     * Helper method to avoid exposing sensitive data
     */
    private UserResponseDTO convertToUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUserName());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setProfileImageUrl(user.getProfileImageUrl());
        dto.setRole(user.getRole());
        dto.setEnabled(user.getEnabled());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    private String getDeviceInfo(HttpServletRequest request){
        String userAgent = request .getHeader("User-Agent");
        return userAgent != null ? userAgent : "Unknown Device";
    }

    private String getIpAddress(HttpServletRequest request){
        String ipAddress = request.getHeader("X-Forwarded-For");
        if(ipAddress == null || ipAddress.isEmpty()){
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public RefreshToken createRefreshToken(CustomUserDetails customUserDetails, String deviceInfo, String ipAddress){

        User user = customUserDetails.getUser();

        String tokenString = jwtUtil.generateRefreshToken(customUserDetails);

        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(refreshExpiration / 1000);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(tokenString)
                .user(user)
                .expiryDate(expiryDate)
                .deviceInfo(deviceInfo)
                .ipAddress(ipAddress)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public AuthResponse refreshAccessToken(String refreshTokenString){

        if(!jwtUtil.isRefreshToken(refreshTokenString)){
            throw new RuntimeException("Invalid token type");
        }

        String username = jwtUtil.extractUsername(refreshTokenString);

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenString).orElseThrow(()-> new RuntimeException("Refresh token not found"));

        if(refreshToken.isExpired()){

            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }
        User user = refreshToken.getUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // Validate token against user
        if (!jwtUtil.validateRefreshToken(refreshTokenString, userDetails)) {
            throw new RuntimeException("Invalid refresh token");
        }

        // 6. Generate new access token
        String newAccessToken = jwtUtil.generateToken(userDetails);

        // 7. Return response
        UserResponseDTO userResponse = convertToUserResponseDTO(user);
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .user(userResponse)
                .expiresIn(expiration)
                .refreshToken(refreshTokenString)  // Return same refresh token
                .build();
    }

}
