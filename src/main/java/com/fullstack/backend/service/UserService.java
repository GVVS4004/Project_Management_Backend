package com.fullstack.backend.service;


import com.fullstack.backend.dto.request.ChangePasswordDTO;
import com.fullstack.backend.dto.request.UpdateProfileDTO;
import com.fullstack.backend.dto.response.UserResponseDTO;
import com.fullstack.backend.entity.Role;
import com.fullstack.backend.entity.User;
import com.fullstack.backend.exception.BadRequestException;
import com.fullstack.backend.exception.DuplicateResourceException;
import com.fullstack.backend.exception.ResourceNotFoundException;
import com.fullstack.backend.exception.ForbiddenException;
import com.fullstack.backend.exception.UnauthorizedException;
import com.fullstack.backend.repository.UserRepository;
import com.fullstack.backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll().stream().map(this::convertToUserResponseDTO).collect(Collectors.toList());
    }

    /**
     * Search users by query string across username, firstName, lastName, and email
     * @param query Search term
     * @param limit Maximum number of results to return (default 20)
     * @return List of matching users
     */
    public List<UserResponseDTO> searchUsers(String query, int limit) {
        // Validate limit
        if (limit <= 0 || limit > 100) {
            limit = 20; // Default to 20 if invalid
        }

        List<User> users = userRepository.searchUsers(query, PageRequest.of(0, limit));
        return users.stream()
                .map(this::convertToUserResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Long Id){
        User user = userRepository.findById(Id).orElseThrow(()-> new ResourceNotFoundException("User not found with id:" + Id));
        // Check if current user has permission to view this profile
        checkUserAccessPermission(Id);
        return convertToUserResponseDTO(user);
    }

    public UserResponseDTO getCurrentUser(){
        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        return  convertToUserResponseDTO(user);
    }

    /**
     * Update user role - Admin only operation
     */
    public UserResponseDTO updateUserRole(Long userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " +
                        userId));

        user.setRole(newRole);
        User updatedUser = userRepository.save(user);

        return convertToUserResponseDTO(updatedUser);
    }

    /**
     * Delete user - Admin only or user can delete themselves
     */
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " +
                        userId));

        // Check if current user has permission to delete this user
        checkUserAccessPermission(userId);

        userRepository.delete(user);
    }
    /**
     * Get all users by role - Admin and Manager can access
     */
    public List<UserResponseDTO> getUsersByRole(Role role) {
        return userRepository.findAllByRole(role)
                .stream()
                .map(this::convertToUserResponseDTO)
                .collect(Collectors.toList());
    }

    private void checkUserAccessPermission(Long targetUserId ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if(isAdmin){
            return;
        }
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String currentUserEmail = userDetails.getUser().getEmail();

        User currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow(()-> new UnauthorizedException("User not authenticated"));

        if(!targetUserId.equals(currentUser.getId())){
            throw new ForbiddenException("You dont have permission to access this user's data");
        }
    }


    private String getCurrentUserEmail(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || !auth.isAuthenticated()){

            throw new UnauthorizedException("User not authenticated");
        }
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return userDetails.getUser().getEmail();
    }

    public boolean hasRole(String role){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_"+role));
    }

    public boolean isOwnerOrAdmin(Long resourceOwnerId){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            return true;
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String currentUserEmail = userDetails.getUser().getEmail();

        User currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow(()-> new UnauthorizedException("User not authenticated"));
        return currentUser.getId().equals(resourceOwnerId);
    }
    @Transactional
    public UserResponseDTO updateProfile(Long userId, UpdateProfileDTO dto){

        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with id:" + userId));
        if(dto.getEmail()!= null && !dto.getEmail().equals(user.getEmail())){
            if(userRepository.existsByEmail(dto.getEmail())){
                throw new DuplicateResourceException("Email already exists: "+dto.getEmail());
            }
            user.setEmail(dto.getEmail());
        }
        if(dto.getUserName() != null && !dto.getUserName().equals(user.getUserName())){
            if(userRepository.existsByUserName(dto.getUserName())){
                throw new DuplicateResourceException("Username already exists: "+dto.getUserName());
            }
            user.setUserName(dto.getUserName());
        }

        if(dto.getFirstName()!= null){
            user.setFirstName(dto.getFirstName());
        }
        if(dto.getLastName() != null){
            user.setLastName(dto.getLastName());
        }
        if(dto.getProfileImageUrl()!=null){
            user.setProfileImageUrl(dto.getProfileImageUrl());
        }

        User updatedUser = userRepository.save(user);
        return convertToUserResponseDTO(updatedUser);
    }

    @Transactional
    public void updateProfilePassword(Long userId, ChangePasswordDTO changePasswordDTO){
        if(!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())){
            throw new BadRequestException("New password and confirm password do not match");
        }

        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with id: "+userId));
        if(!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(),user.getPassword())){
            throw new UnauthorizedException("Current password is incorrect");
        }

        if(changePasswordDTO.getCurrentPassword().equals(changePasswordDTO.getNewPassword())){
            throw new BadRequestException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public UserResponseDTO updateProfileImage(Long userId, String imageUrl){
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with id:"+userId));
        user.setProfileImageUrl(imageUrl);
        User updatedUser = userRepository.save(user);
        return convertToUserResponseDTO(updatedUser);
    }
    /**
     * Delete user account (soft delete)
     */
    @Transactional
    public void deleteAccount(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with id: "+ userId));

        user.setEnabled(false);
        userRepository.save(user);
    }


    private UserResponseDTO convertToUserResponseDTO(User user){
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        dto.setEnabled(user.getEnabled());
        dto.setProfileImageUrl(user.getProfileImageUrl());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
