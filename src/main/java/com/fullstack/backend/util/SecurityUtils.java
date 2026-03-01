package com.fullstack.backend.util;

import com.fullstack.backend.entity.User;
import com.fullstack.backend.exception.UnauthorizedException;
import com.fullstack.backend.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {} // prevent instantiation

    public static User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }
        Object principal = auth.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            throw new UnauthorizedException("Invalid authentication principal");
        }
        return ((CustomUserDetails) principal).getUser();
    }
}