package com.scms.entity;

/**
 * 사용자 역할 정의
 * - STUDENT: 학생
 * - ADMIN: 관리자 (부서 관리자)
 * - COUNSELOR: 상담사
 * - INSTRUCTOR: 비교과 프로그램 운영자/강사
 */
public enum Role {
    STUDENT("학생"),
    ADMIN("관리자"),
    COUNSELOR("상담사"),
    INSTRUCTOR("강사");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
