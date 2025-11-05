package com.scms.controller;

import com.scms.dto.ApiResponse;
import com.scms.dto.auth.*;
import com.scms.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 컨트롤러
 * 요구사항 ID: AUTH-001 ~ AUTH-007
 *
 * Base URL: /api/auth
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인
     * POST /api/auth/login
     *
     * 요구사항 ID: AUTH-001
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        log.info("Login request for studentId: {}", request.getStudentId());

        LoginResponse response = authService.login(request, httpRequest);

        return ResponseEntity.ok(
                ApiResponse.success("로그인 성공", response)
        );
    }

    /**
     * 로그아웃
     * POST /api/auth/logout
     *
     * 요구사항 ID: AUTH-002
     * Authorization: Bearer {token} 헤더 필요
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        String token = extractToken(request);

        if (token == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<Void>builder()
                            .success(false)
                            .message("토큰이 필요합니다")
                            .build());
        }

        authService.logout(token);

        log.info("Logout successful");

        return ResponseEntity.ok(
                ApiResponse.success("로그아웃 성공")
        );
    }

    /**
     * 회원가입
     * POST /api/auth/signup
     *
     * 요구사항 ID: AUTH-001
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(
            @Valid @RequestBody SignupRequest request) {

        log.info("Signup request for studentId: {}", request.getStudentId());

        authService.signup(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입이 완료되었습니다"));
    }

    /**
     * 토큰 갱신
     * POST /api/auth/refresh
     *
     * 요구사항 ID: AUTH-007
     * Refresh Token으로 새로운 Access Token 발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenRefreshResponse>> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request) {

        log.info("Token refresh request");

        TokenRefreshResponse response = authService.refreshToken(request);

        return ResponseEntity.ok(
                ApiResponse.success("토큰 갱신 성공", response)
        );
    }

    /**
     * 비밀번호 재설정 (임시 비밀번호 발급)
     * POST /api/auth/password-reset
     *
     * 요구사항 ID: AUTH-003
     */
    @PostMapping("/password-reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody PasswordResetRequest request) {

        log.info("Password reset request for studentId: {}", request.getStudentId());

        authService.resetPassword(request);

        return ResponseEntity.ok(
                ApiResponse.success("임시 비밀번호가 이메일로 전송되었습니다")
        );
    }

    /**
     * 헬스 체크
     * GET /api/auth/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(
                ApiResponse.success("Auth API is healthy", "OK")
        );
    }

    /**
     * Request에서 JWT 토큰 추출
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}
