package com.scms.repository;

import com.scms.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 토큰 블랙리스트 Repository
 * 요구사항 ID: AUTH-002
 */
@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {

    /**
     * 토큰으로 블랙리스트 조회
     */
    Optional<TokenBlacklist> findByToken(String token);

    /**
     * 토큰이 블랙리스트에 존재하는지 확인
     */
    boolean existsByToken(String token);

    /**
     * 만료된 토큰 삭제
     */
    void deleteByExpiresAtBefore(LocalDateTime dateTime);

}
