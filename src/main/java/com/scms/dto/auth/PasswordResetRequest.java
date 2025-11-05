package com.scms.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 비밀번호 찾기 요청 DTO
 * 요구사항 ID: AUTH-003
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {

    @NotBlank(message = "이름은 필수입니다")
    private String name;

    @NotBlank(message = "학번은 필수입니다")
    private String studentId;

    private LocalDate dateOfBirth;

}
