package com.scms.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 비교과 프로그램 신청
 * 요구사항 ID: PRG-007 ~ PRG-010
 */
@Entity
@Table(name = "program_enrollments", indexes = {
        @Index(name = "idx_user_program", columnList = "user_id, program_id"),
        @Index(name = "idx_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramEnrollment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.PENDING;

    @Column(name = "cancel_reason", length = 500)
    private String cancelReason;

    @Column(name = "reject_reason", length = 500)
    private String rejectReason;

    @Column(name = "participation_confirmed")
    @Builder.Default
    private Boolean participationConfirmed = false;

    @Column(name = "satisfaction_survey_submitted")
    @Builder.Default
    private Boolean satisfactionSurveySubmitted = false;

    @Column(name = "result_report_submitted")
    @Builder.Default
    private Boolean resultReportSubmitted = false;

}
