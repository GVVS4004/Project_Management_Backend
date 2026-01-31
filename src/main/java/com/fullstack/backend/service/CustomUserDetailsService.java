package com.fullstack.backend.service;

import com.fullstack.backend.entity.User;
import com.fullstack.backend.repository.UserRepository;
import com.fullstack.backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with userName: "+ userName));
        return new CustomUserDetails(user);
    }
}
