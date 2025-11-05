package com.scms.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청 DTO
 * 요구사항 ID: AUTH-001
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "학번은 필수입니다")
    private String studentId;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;

}
