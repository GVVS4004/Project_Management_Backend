package com.fullstack.backend.repository;

import com.fullstack.backend.entity.RefreshToken;
import com.fullstack.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    public Optional<RefreshToken> findByToken(String token);

    public void deleteByToken(String token);

    public void deleteByUser(User user);

    public void deleteByExpiryDateBefore(LocalDateTime now);


}
