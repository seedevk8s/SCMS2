package com.scms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * JWT 토큰 블랙리스트
 * 요구사항 ID: AUTH-002 (로그아웃 시 토큰 무효화)
 */
@Entity
@Table(name = "token_blacklist", indexes = {
        @Index(name = "idx_token", columnList = "token")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blacklist_id")
    private Long id;

    @Column(name = "token", nullable = false, length = 1000)
    private String token;

    @Column(name = "blacklisted_at", nullable = false)
    private LocalDateTime blacklistedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "user_id")
    private Long userId;

}
