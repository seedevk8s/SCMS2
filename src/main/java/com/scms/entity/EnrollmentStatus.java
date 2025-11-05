package com.scms.entity;

/**
 * 프로그램 신청 상태
 */
public enum EnrollmentStatus {
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("거부"),
    CANCELLED("취소"),
    COMPLETED("완료"),
    WAITING("대기열"); // 정원 초과 시

    private final String description;

    EnrollmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
