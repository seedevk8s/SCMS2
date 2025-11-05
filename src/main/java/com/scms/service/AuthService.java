package com.scms.service;

import com.scms.dto.auth.*;
import com.scms.entity.*;
import com.scms.exception.*;
import com.scms.repository.LoginHistoryRepository;
import com.scms.repository.TokenBlacklistRepository;
import com.scms.repository.UserRepository;
import com.scms.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 인증 서비스
 * 요구사항 ID: AUTH-001 ~ AUTH-007
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 로그인
     * 요구사항 ID: AUTH-001
     * - 학번과 비밀번호 검증
     * - JWT 토큰 발급
     * - 로그인 이력 저장
     * - 5회 실패 시 계정 잠금
     */
    @Transactional
    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        User user = userRepository.findByStudentId(request.getStudentId())
                .orElseThrow(() -> {
                    log.warn("Login failed - User not found: {}", request.getStudentId());
                    return new UserNotFoundException("학번 또는 비밀번호가 올바르지 않습니다");
                });

        // 계정 잠금 확인
        if (user.getAccountLocked()) {
            saveLoginHistory(user, httpRequest, false, "계정이 잠김");
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKED);
        }

        // 계정 활성화 확인
        if (!user.getIsActive()) {
            saveLoginHistory(user, httpRequest, false, "비활성화된 계정");
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            user.increaseLoginFailCount(); // 실패 횟수 증가
            userRepository.save(user);
            saveLoginHistory(user, httpRequest, false, "비밀번호 불일치");

            log.warn("Login failed - Invalid password for user: {}. Fail count: {}",
                    user.getStudentId(), user.getLoginFailCount());

            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        // 로그인 성공
        user.resetLoginFailCount();
        userRepository.save(user);
        saveLoginHistory(user, httpRequest, true, null);

        // JWT 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(
                user.getStudentId(),
                user.getRole().name()
        );
        String refreshToken = jwtUtil.generateRefreshToken(user.getStudentId());

        log.info("Login successful for user: {}", user.getStudentId());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L) // 1시간 (초 단위)
                .user(LoginResponse.UserInfo.builder()
                        .userId(user.getId())
                        .studentId(user.getStudentId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build())
                .build();
    }

    /**
     * 로그아웃
     * 요구사항 ID: AUTH-002
     * - Token 블랙리스트 추가
     */
    @Transactional
    public void logout(String token) {
        // 토큰이 이미 블랙리스트에 있는지 확인
        if (tokenBlacklistRepository.existsByToken(token)) {
            log.warn("Token already in blacklist: {}", token);
            return;
        }

        // 토큰에서 사용자 정보 추출
        String studentId = jwtUtil.getStudentIdFromToken(token);
        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new UserNotFoundException());

        // 토큰 만료 시간 추출
        LocalDateTime expiresAt = jwtUtil.getExpirationDateFromToken(token)
                .toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();

        // 블랙리스트에 추가
        TokenBlacklist blacklist = TokenBlacklist.builder()
                .token(token)
                .blacklistedAt(LocalDateTime.now())
                .expiresAt(expiresAt)
                .userId(user.getId())
                .build();

        tokenBlacklistRepository.save(blacklist);
        log.info("Token added to blacklist for user: {}", studentId);
    }

    /**
     * 회원가입
     * 요구사항 ID: AUTH-001
     */
    @Transactional
    public void signup(SignupRequest request) {
        // 비밀번호 확인 검증
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 중복 확인
        if (userRepository.existsByStudentId(request.getStudentId())) {
            throw new BusinessException(ErrorCode.DUPLICATE_STUDENT_ID);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException();
        }

        // 사용자 생성
        User user = User.builder()
                .studentId(request.getStudentId())
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .role(Role.STUDENT) // 기본 역할은 학생
                .isActive(true)
                .loginFailCount(0)
                .accountLocked(false)
                .build();

        userRepository.save(user);
        log.info("New user registered: {}", user.getStudentId());
    }

    /**
     * 토큰 갱신
     * 요구사항 ID: AUTH-007
     * - Refresh Token으로 새로운 Access Token 발급
     */
    @Transactional(readOnly = true)
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        // Refresh Token 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // 블랙리스트 확인
        if (tokenBlacklistRepository.existsByToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN, "로그아웃된 토큰입니다");
        }

        // 토큰 타입 확인
        String tokenType = jwtUtil.getTokenType(refreshToken);
        if (!"REFRESH".equals(tokenType)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN, "Refresh Token이 아닙니다");
        }

        // 사용자 정보 조회
        String studentId = jwtUtil.getStudentIdFromToken(refreshToken);
        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new UserNotFoundException());

        // 새로운 Access Token 생성
        String newAccessToken = jwtUtil.generateAccessToken(
                user.getStudentId(),
                user.getRole().name()
        );

        log.info("Token refreshed for user: {}", studentId);

        return TokenRefreshResponse.builder()
                .accessToken(newAccessToken)
                .tokenType("Bearer")
                .expiresIn(3600L) // 1시간
                .build();
    }

    /**
     * 비밀번호 재설정 (임시 비밀번호 발급)
     * 요구사항 ID: AUTH-003
     */
    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        // 사용자 확인
        User user = userRepository.findByStudentId(request.getStudentId())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다"));

        // 이름 확인
        if (!user.getName().equals(request.getName())) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "사용자 정보가 일치하지 않습니다");
        }

        // 생년월일 확인 (입력된 경우)
        if (request.getDateOfBirth() != null &&
            !request.getDateOfBirth().equals(user.getDateOfBirth())) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "사용자 정보가 일치하지 않습니다");
        }

        // 임시 비밀번호 생성 (실제로는 이메일로 전송해야 함)
        String tempPassword = generateTempPassword();
        user.setPassword(passwordEncoder.encode(tempPassword));

        // 계정 잠금 해제
        if (user.getAccountLocked()) {
            user.unlockAccount();
        }

        userRepository.save(user);

        // TODO: 이메일로 임시 비밀번호 전송 (CMN-003 요구사항)
        log.info("Password reset for user: {}. Temp password: {}", user.getStudentId(), tempPassword);
        log.warn("임시 비밀번호가 생성되었습니다. 실제로는 이메일로 전송해야 합니다.");
    }

    /**
     * 로그인 이력 저장
     */
    private void saveLoginHistory(User user, HttpServletRequest request,
                                   boolean success, String failReason) {
        LoginHistory history = LoginHistory.builder()
                .user(user)
                .loginAt(LocalDateTime.now())
                .ipAddress(getClientIp(request))
                .userAgent(request.getHeader("User-Agent"))
                .success(success)
                .failReason(failReason)
                .build();

        loginHistoryRepository.save(history);
    }

    /**
     * 클라이언트 IP 주소 추출
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 임시 비밀번호 생성
     */
    private String generateTempPassword() {
        // 8자리 랜덤 비밀번호 생성 (영문 대소문자 + 숫자 + 특수문자)
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();

        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }

}
