package com.scms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 비교과 프로그램
 * 요구사항 ID: PRG-001 ~ PRG-037
 */
@Entity
@Table(name = "programs", indexes = {
        @Index(name = "idx_category", columnList = "category"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_start_date", columnList = "start_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Program extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private Long id;

    @NotBlank(message = "프로그램 제목은 필수입니다")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "카테고리는 필수입니다")
    @Column(name = "category", nullable = false, length = 50)
    private String category; // 학술, 봉사, 문화 등

    @NotNull(message = "정원은 필수입니다")
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "current_enrollment", nullable = false)
    @Builder.Default
    private Integer currentEnrollment = 0;

    @NotNull(message = "시작일은 필수입니다")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @NotNull(message = "종료일은 필수입니다")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "location", length = 200)
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private User instructor; // 강사/운영자

    @Column(name = "mileage_points")
    @Builder.Default
    private Integer mileagePoints = 0;

    @Column(name = "application_start", nullable = false)
    private LocalDateTime applicationStart;

    @Column(name = "application_end", nullable = false)
    private LocalDateTime applicationEnd;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ProgramStatus status = ProgramStatus.UPCOMING;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * 신청 가능 여부 확인
     */
    public boolean isApplicationAvailable() {
        LocalDateTime now = LocalDateTime.now();
        return isActive
                && status == ProgramStatus.RECRUITING
                && now.isAfter(applicationStart)
                && now.isBefore(applicationEnd)
                && currentEnrollment < capacity;
    }

    /**
     * 정원 초과 여부 확인
     */
    public boolean isFull() {
        return currentEnrollment >= capacity;
    }

    /**
     * 신청 인원 증가
     */
    public void increaseEnrollment() {
        if (currentEnrollment < capacity) {
            this.currentEnrollment++;
        }
    }

    /**
     * 신청 인원 감소
     */
    public void decreaseEnrollment() {
        if (currentEnrollment > 0) {
            this.currentEnrollment--;
        }
    }
}
