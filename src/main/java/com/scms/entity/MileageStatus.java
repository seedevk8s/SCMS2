package com.scms.entity;

/**
 * 마일리지 상태
 */
public enum MileageStatus {
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("거부"),
    CANCELLED("취소");

    private final String description;

    MileageStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
