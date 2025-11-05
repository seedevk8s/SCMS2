package com.scms.config;

import com.scms.entity.Role;
import com.scms.entity.User;
import com.scms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

/**
 * 테스트용 초기 데이터 생성
 * 개발 환경에서만 실행
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataLoader {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Profile("!prod") // 프로덕션 환경에서는 실행 안 함
    public CommandLineRunner loadTestData() {
        return args -> {
            // 이미 데이터가 있으면 생성하지 않음
            if (userRepository.count() > 0) {
                log.info("Test data already exists. Skipping data loading.");
                return;
            }

            log.info("Loading test data...");

            // 관리자 계정
            User admin = User.builder()
                    .studentId("admin")
                    .name("관리자")
                    .email("admin@scms.com")
                    .password(passwordEncoder.encode("Admin123!"))
                    .dateOfBirth(LocalDate.of(1990, 1, 1))
                    .address("서울시 강남구")
                    .role(Role.ADMIN)
                    .isActive(true)
                    .loginFailCount(0)
                    .accountLocked(false)
                    .build();
            userRepository.save(admin);
            log.info("Admin user created: {}", admin.getStudentId());

            // 학생 계정 1
            User student1 = User.builder()
                    .studentId("2024001")
                    .name("김학생")
                    .email("student1@scms.com")
                    .password(passwordEncoder.encode("Student123!"))
                    .dateOfBirth(LocalDate.of(2005, 3, 15))
                    .address("서울시 서초구")
                    .role(Role.STUDENT)
                    .isActive(true)
                    .loginFailCount(0)
                    .accountLocked(false)
                    .build();
            userRepository.save(student1);
            log.info("Student user created: {}", student1.getStudentId());

            // 학생 계정 2
            User student2 = User.builder()
                    .studentId("2024002")
                    .name("이학생")
                    .email("student2@scms.com")
                    .password(passwordEncoder.encode("Student123!"))
                    .dateOfBirth(LocalDate.of(2005, 5, 20))
                    .address("서울시 송파구")
                    .role(Role.STUDENT)
                    .isActive(true)
                    .loginFailCount(0)
                    .accountLocked(false)
                    .build();
            userRepository.save(student2);
            log.info("Student user created: {}", student2.getStudentId());

            // 상담사 계정
            User counselor = User.builder()
                    .studentId("counselor01")
                    .name("박상담")
                    .email("counselor@scms.com")
                    .password(passwordEncoder.encode("Counselor123!"))
                    .dateOfBirth(LocalDate.of(1985, 7, 10))
                    .address("서울시 강동구")
                    .role(Role.COUNSELOR)
                    .isActive(true)
                    .loginFailCount(0)
                    .accountLocked(false)
                    .build();
            userRepository.save(counselor);
            log.info("Counselor user created: {}", counselor.getStudentId());

            // 강사 계정
            User instructor = User.builder()
                    .studentId("instructor01")
                    .name("최강사")
                    .email("instructor@scms.com")
                    .password(passwordEncoder.encode("Instructor123!"))
                    .dateOfBirth(LocalDate.of(1980, 9, 25))
                    .address("서울시 마포구")
                    .role(Role.INSTRUCTOR)
                    .isActive(true)
                    .loginFailCount(0)
                    .accountLocked(false)
                    .build();
            userRepository.save(instructor);
            log.info("Instructor user created: {}", instructor.getStudentId());

            log.info("Test data loading completed. Total users: {}", userRepository.count());
            log.info("");
            log.info("========================================");
            log.info("테스트 계정 정보:");
            log.info("----------------------------------------");
            log.info("관리자 - ID: admin, PW: Admin123!");
            log.info("학생1 - ID: 2024001, PW: Student123!");
            log.info("학생2 - ID: 2024002, PW: Student123!");
            log.info("상담사 - ID: counselor01, PW: Counselor123!");
            log.info("강사 - ID: instructor01, PW: Instructor123!");
            log.info("========================================");
        };
    }

}
