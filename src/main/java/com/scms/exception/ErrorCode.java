package com.scms.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 정의
 * 요구사항 ID: CMN-001, CMN-002
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통 (COMMON)
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 오류가 발생했습니다"),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "엔티티를 찾을 수 없습니다"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C004", "잘못된 타입입니다"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "C005", "접근 권한이 없습니다"),

    // 인증/인가 (AUTH)
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "인증이 필요합니다"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A002", "이메일 또는 비밀번호가 올바르지 않습니다"),
    ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "A003", "계정이 잠겼습니다. 관리자에게 문의하세요"),
    ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, "A004", "비활성화된 계정입니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A005", "유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A006", "만료된 토큰입니다"),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A007", "토큰을 찾을 수 없습니다"),

    // 사용자 (USER)
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "U002", "이미 사용중인 이메일입니다"),
    DUPLICATE_STUDENT_ID(HttpStatus.CONFLICT, "U003", "이미 사용중인 학번입니다"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "U004", "비밀번호가 올바르지 않습니다"),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "U005", "비밀번호가 일치하지 않습니다"),

    // 비교과 프로그램 (PROGRAM)
    PROGRAM_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "프로그램을 찾을 수 없습니다"),
    PROGRAM_FULL(HttpStatus.BAD_REQUEST, "P002", "프로그램 정원이 초과되었습니다"),
    ALREADY_ENROLLED(HttpStatus.CONFLICT, "P003", "이미 신청한 프로그램입니다"),
    APPLICATION_CLOSED(HttpStatus.BAD_REQUEST, "P004", "신청 기간이 아닙니다"),
    ENROLLMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "P005", "신청 내역을 찾을 수 없습니다"),
    CANNOT_CANCEL_ENROLLMENT(HttpStatus.BAD_REQUEST, "P006", "신청을 취소할 수 없습니다"),

    // 역량 진단 (COMPETENCY)
    COMPETENCY_NOT_FOUND(HttpStatus.NOT_FOUND, "CO001", "역량을 찾을 수 없습니다"),
    ASSESSMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "CO002", "진단을 찾을 수 없습니다"),
    ASSESSMENT_CLOSED(HttpStatus.BAD_REQUEST, "CO003", "진단 기간이 종료되었습니다"),
    ALREADY_SUBMITTED(HttpStatus.CONFLICT, "CO004", "이미 제출한 진단입니다"),

    // 상담 (COUNSELING)
    COUNSELING_NOT_FOUND(HttpStatus.NOT_FOUND, "CS001", "상담 내역을 찾을 수 없습니다"),
    CANNOT_CANCEL_COUNSELING(HttpStatus.BAD_REQUEST, "CS002", "상담을 취소할 수 없습니다"),
    COUNSELING_ALREADY_APPROVED(HttpStatus.CONFLICT, "CS003", "이미 승인된 상담입니다"),
    INVALID_COUNSELING_DATE(HttpStatus.BAD_REQUEST, "CS004", "유효하지 않은 상담 날짜입니다"),

    // 마일리지 (MILEAGE)
    MILEAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "마일리지 내역을 찾을 수 없습니다"),
    INSUFFICIENT_MILEAGE(HttpStatus.BAD_REQUEST, "M002", "마일리지가 부족합니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
