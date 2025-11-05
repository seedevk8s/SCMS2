package com.scms.repository;

import com.scms.entity.LoginHistory;
import com.scms.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 로그인 이력 Repository
 * 요구사항 ID: AUTH-001 (로그인 이력 저장)
 */
@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

    /**
     * 사용자별 로그인 이력 조회 (페이징)
     */
    Page<LoginHistory> findByUserOrderByLoginAtDesc(User user, Pageable pageable);

    /**
     * 사용자의 최근 로그인 이력 조회
     */
    List<LoginHistory> findTop10ByUserOrderByLoginAtDesc(User user);

    /**
     * 특정 기간 동안의 로그인 이력 조회
     */
    List<LoginHistory> findByLoginAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 로그인 실패 이력 조회
     */
    List<LoginHistory> findByUserAndSuccessFalseOrderByLoginAtDesc(User user);

}
