package com.scms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 상담 예약
 * 요구사항 ID: CNSL-001 ~ CNSL-024
 */
@Entity
@Table(name = "counselings", indexes = {
        @Index(name = "idx_student_id", columnList = "student_id"),
        @Index(name = "idx_counselor_id", columnList = "counselor_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_desired_date", columnList = "desired_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Counseling extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counseling_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id")
    private User counselor; // 상담사 배정 후 설정

    @NotBlank(message = "상담 유형은 필수입니다")
    @Column(name = "counseling_type", nullable = false, length = 20)
    private String counselingType; // 진로, 학업, 심리

    @NotNull(message = "희망 날짜는 필수입니다")
    @Column(name = "desired_date", nullable = false)
    private LocalDateTime desiredDate;

    @Column(name = "desired_time", length = 20)
    private String desiredTime; // 예: "10:00-11:00"

    @NotBlank(message = "상담 내용은 필수입니다")
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private CounselingStatus status = CounselingStatus.PENDING;

    @Column(name = "confirmed_date")
    private LocalDateTime confirmedDate;

    @Column(name = "confirmed_time", length = 20)
    private String confirmedTime;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo; // 관리자/상담사 메모

    @Column(name = "reject_reason", length = 500)
    private String rejectReason;

    @Column(name = "cancel_reason", length = 500)
    private String cancelReason;

    @Column(name = "satisfaction_survey_submitted")
    @Builder.Default
    private Boolean satisfactionSurveySubmitted = false;

}
