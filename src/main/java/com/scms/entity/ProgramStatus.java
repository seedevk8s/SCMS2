package com.scms.entity;

/**
 * 비교과 프로그램 상태
 */
public enum ProgramStatus {
    UPCOMING("예정"),
    RECRUITING("모집중"),
    IN_PROGRESS("진행중"),
    COMPLETED("종료"),
    CANCELLED("취소");

    private final String description;

    ProgramStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
