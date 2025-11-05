package com.scms.entity;

/**
 * 상담 상태
 */
public enum CounselingStatus {
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("거부"),
    CANCELLED("취소"),
    COMPLETED("완료");

    private final String description;

    CounselingStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
