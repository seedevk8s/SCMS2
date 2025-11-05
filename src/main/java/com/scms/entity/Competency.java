package com.scms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * 핵심역량
 * 요구사항 ID: COMP-001 ~ COMP-006
 * Level 1: 핵심역량 (상위)
 * Level 2: 하위역량 (하위)
 */
@Entity
@Table(name = "competencies", indexes = {
        @Index(name = "idx_level", columnList = "level"),
        @Index(name = "idx_parent_id", columnList = "parent_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Competency extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competency_id")
    private Long id;

    @NotBlank(message = "역량명은 필수입니다")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "레벨은 필수입니다")
    @Column(name = "level", nullable = false)
    private Integer level; // 1: 핵심역량, 2: 하위역량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Competency parent; // Level 2의 경우 상위 역량 참조

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "display_order")
    private Integer displayOrder;

}
