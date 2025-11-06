## 📋 Summary

이 PR은 대학교 브랜딩을 변경하고 로컬 MySQL 환경에서 즉시 실행 가능하도록 완전한 데이터베이스 설정을 추가합니다.

## 🎯 주요 변경 사항

### 1. 브랜딩 변경 ✅
- **우석대학교** → **푸름대학교 학생성장지원센터**로 전체 시스템 명칭 변경
- 영향 받은 파일:
  - 모든 HTML 템플릿 (login, index, header, footer, layout)
  - HomeController.java
  - README.md

### 2. 데이터베이스 완전 설정 ✅

#### 새로 추가된 테이블
- **users** 테이블: 학생/상담사/관리자 통합 인증 테이블
  - BCrypt 비밀번호 암호화
  - 계정 잠금 기능 (5회 실패 시)
  - Soft Delete 지원
- **login_history** 테이블: 로그인 이력 추적
- **counselors** 테이블: 상담사 전문 정보

#### DataLoader 자동 생성 (신규)
- `src/main/java/com/scms/app/config/DataLoader.java` 추가
- 애플리케이션 첫 실행 시 자동으로 테스트 계정 생성
- BCrypt로 안전한 비밀번호 암호화
- 콘솔 로그로 생성된 계정 정보 출력

### 3. Docker 지원 추가 ✅
- `docker-compose.yml` 추가
- MySQL 8.0 컨테이너 자동 설정
- 데이터베이스 자동 초기화

### 4. 문서화 강화 ✅
- `database/README.md`: 포괄적인 데이터베이스 설정 가이드
- 메인 `README.md`: 빠른 시작 가이드 및 테스트 계정 정보
- `.env.example`: 환경 변수 템플릿

## 🔑 테스트 계정

애플리케이션 첫 실행 시 자동 생성되는 계정:

### 학생 계정 (8개)
| 학번 | 비밀번호 | 이름 | 학과 |
|------|----------|------|------|
| 2024001 | 030101 | 김철수 | 컴퓨터공학과 |
| 2024002 | 040215 | 이영희 | 소프트웨어학과 |
| 2023001 | 020310 | 박민수 | 정보보안학과 |
| 2023002 | 020505 | 최지은 | 컴퓨터공학과 |
| 2022001 | 010620 | 정우진 | 인공지능학과 |
| 2022002 | 010815 | 강민지 | 데이터사이언스학과 |
| 2021001 | 000911 | 윤서준 | 컴퓨터공학과 |
| 2021002 | 001225 | 임하늘 | 소프트웨어학과 |

### 관리자 계정
| 학번 | 비밀번호 | 역할 |
|------|----------|------|
| 9999999 | admin123 | ADMIN |

> 💡 초기 비밀번호는 생년월일 6자리 (yyMMdd) 형식

## 🚀 로컬 환경에서 실행하기

### Docker 사용 (권장)
```bash
docker-compose up -d
mvn spring-boot:run
```

### 로컬 MySQL 사용
```bash
# 1. 데이터베이스 생성
mysql -u root -p
CREATE DATABASE scms2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
exit

# 2. 스키마 적용
mysql -u root -p scms2 < database/schema.sql

# 3. application.yml에서 MySQL 비밀번호 확인/수정

# 4. 애플리케이션 실행
mvn spring-boot:run

# 5. 브라우저 접속
http://localhost:8080
```

## ✅ 테스트 체크리스트

- [x] 대학교 명칭 변경 확인
- [x] users 테이블 생성 확인
- [x] DataLoader 자동 실행 확인
- [x] 테스트 계정으로 로그인 성공
- [x] BCrypt 비밀번호 암호화 확인
- [x] 로그인 히스토리 기록 확인
- [x] Docker Compose 실행 확인
- [x] 문서 정확성 확인

## 📁 변경된 파일

### 신규 파일
- `docker-compose.yml`
- `database/README.md`
- `.env.example`
- `src/main/java/com/scms/app/config/DataLoader.java`

### 수정된 파일
- `README.md`
- `database/schema.sql`
- `src/main/resources/templates/layout/layout.html`
- `src/main/resources/templates/login.html`
- `src/main/resources/templates/layout/header.html`
- `src/main/resources/templates/index.html`
- `src/main/resources/templates/layout/footer.html`
- `src/main/java/com/scms/app/controller/HomeController.java`

## 🔍 Breaking Changes

**없음** - 기존 기능은 모두 유지되며 데이터베이스 설정이 추가되었습니다.

## 📝 추가 노트

- 초기 비밀번호는 생년월일 6자리로 설정되어 있어 실제 프로덕션 환경에서는 변경 필요
- DataLoader는 users 테이블이 비어있을 때만 실행되므로 재실행 시 중복 생성 문제 없음
- 로그인 5회 실패 시 계정 자동 잠금 기능 활성화
- 모든 비밀번호는 BCrypt로 암호화되어 저장

## 📚 관련 문서

- [database/README.md](database/README.md) - 상세한 데이터베이스 설정 가이드
- [README.md](README.md) - 프로젝트 메인 가이드
