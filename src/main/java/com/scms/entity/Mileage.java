package com.scms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 마일리지
 * 요구사항 ID: PRG-014, PRG-027 ~ PRG-029, PRG-034
 */
@Entity
@Table(name = "mileages", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_program_id", columnList = "program_id"),
        @Index(name = "idx_earned_date", columnList = "earned_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mileage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mileage_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private Program program; // 비교과 프로그램과 연결 (null이면 외부 활동)

    @NotNull(message = "마일리지 점수는 필수입니다")
    @Column(name = "points", nullable = false)
    private Integer points;

    @NotNull(message = "획득일은 필수입니다")
    @Column(name = "earned_date", nullable = false)
    private LocalDateTime earnedDate;

    @Column(name = "description", length = 500)
    private String description; // 마일리지 획득 사유

    @Column(name = "evidence_url", length = 500)
    private String evidenceUrl; // 증빙 자료 URL

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private MileageStatus status = MileageStatus.APPROVED;

}
