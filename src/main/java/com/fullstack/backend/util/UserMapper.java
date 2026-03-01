package com.fullstack.backend.util;

import com.fullstack.backend.dto.response.UserSummaryDTO;
import com.fullstack.backend.entity.User;

public final class UserMapper {

    private UserMapper() {}

    public static UserSummaryDTO toSummaryDTO(User user) {
        if (user == null) return null;
        UserSummaryDTO dto = new UserSummaryDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUserName());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setProfileImageUrl(user.getProfileImageUrl());
        return dto;
    }
}