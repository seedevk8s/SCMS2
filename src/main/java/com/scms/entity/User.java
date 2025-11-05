package com.scms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 사용자 엔티티
 * 요구사항 ID: AUTH-001, AUTH-002, MY-001
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_student_id", columnList = "student_id"),
        @Index(name = "idx_email", columnList = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotBlank(message = "학번은 필수입니다")
    @Column(name = "student_id", unique = true, nullable = false, length = 20)
    private String studentId;

    @NotBlank(message = "이름은 필수입니다")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address", length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "login_fail_count", nullable = false)
    @Builder.Default
    private Integer loginFailCount = 0;

    @Column(name = "account_locked")
    @Builder.Default
    private Boolean accountLocked = false;

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    /**
     * 로그인 실패 횟수 증가
     * 5회 실패 시 계정 잠금 (AUTH-001 요구사항)
     */
    public void increaseLoginFailCount() {
        this.loginFailCount++;
        if (this.loginFailCount >= 5) {
            this.accountLocked = true;
            this.lockedAt = LocalDateTime.now();
        }
    }

    /**
     * 로그인 성공 시 실패 횟수 초기화
     */
    public void resetLoginFailCount() {
        this.loginFailCount = 0;
        this.accountLocked = false;
        this.lockedAt = null;
        this.lastLoginAt = LocalDateTime.now();
    }

    /**
     * 계정 잠금 해제
     */
    public void unlockAccount() {
        this.accountLocked = false;
        this.loginFailCount = 0;
        this.lockedAt = null;
    }
}
