# SCMS API 가이드

## 개발 환경 설정

### 필수 요구사항
- Java 17+
- MySQL 8.0+
- Gradle 8.4+

### 데이터베이스 설정
```sql
CREATE DATABASE scms_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 환경 변수 설정
`src/main/resources/application.yml` 파일에서 다음 환경 변수를 설정하세요:

```yaml
DB_PASSWORD: MySQL 비밀번호
MAIL_USERNAME: Gmail 계정
MAIL_PASSWORD: Gmail 앱 비밀번호
JWT_SECRET: JWT 시크릿 키 (256bit)
```

### 애플리케이션 실행
```bash
# Gradle로 실행
./gradlew bootRun

# 또는 JAR 파일 생성 후 실행
./gradlew build
java -jar build/libs/scms-1.0.0.jar
```

서버 실행 후: http://localhost:8080

---

## 인증 API (Authentication)

### 1. 회원가입
**요구사항 ID**: AUTH-001

```http
POST /api/auth/signup
Content-Type: application/json

{
  "studentId": "2024999",
  "name": "홍길동",
  "email": "hong@example.com",
  "password": "Password123!",
  "passwordConfirm": "Password123!",
  "dateOfBirth": "2005-01-15",
  "address": "서울시 강남구"
}
```

**응답 (201 Created)**
```json
{
  "success": true,
  "message": "회원가입이 완료되었습니다",
  "data": null,
  "timestamp": "2025-11-05T10:30:00"
}
```

**비밀번호 규칙**: 최소 8자, 영문+숫자+특수문자 포함

---

### 2. 로그인
**요구사항 ID**: AUTH-001, AUTH-004

```http
POST /api/auth/login
Content-Type: application/json

{
  "studentId": "2024001",
  "password": "Student123!"
}
```

**응답 (200 OK)**
```json
{
  "success": true,
  "message": "로그인 성공",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "user": {
      "userId": 1,
      "studentId": "2024001",
      "name": "김학생",
      "email": "student1@scms.com",
      "role": "STUDENT"
    }
  },
  "timestamp": "2025-11-05T10:30:00"
}
```

**주요 기능**:
- Access Token 유효기간: 1시간
- Refresh Token 유효기간: 7일
- 5회 로그인 실패 시 계정 자동 잠금
- 로그인 이력 자동 저장 (IP, User-Agent)

---

### 3. 로그아웃
**요구사항 ID**: AUTH-002

```http
POST /api/auth/logout
Authorization: Bearer {accessToken}
```

**응답 (200 OK)**
```json
{
  "success": true,
  "message": "로그아웃 성공",
  "data": null,
  "timestamp": "2025-11-05T10:30:00"
}
```

**처리 내용**: 토큰을 블랙리스트에 추가하여 재사용 방지

---

### 4. 토큰 갱신
**요구사항 ID**: AUTH-007

```http
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**응답 (200 OK)**
```json
{
  "success": true,
  "message": "토큰 갱신 성공",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600
  },
  "timestamp": "2025-11-05T10:30:00"
}
```

---

### 5. 비밀번호 재설정
**요구사항 ID**: AUTH-003

```http
POST /api/auth/password-reset
Content-Type: application/json

{
  "name": "김학생",
  "studentId": "2024001",
  "dateOfBirth": "2005-03-15"
}
```

**응답 (200 OK)**
```json
{
  "success": true,
  "message": "임시 비밀번호가 이메일로 전송되었습니다",
  "data": null,
  "timestamp": "2025-11-05T10:30:00"
}
```

**처리 내용**:
- 사용자 정보 확인 (이름, 학번, 생년월일)
- 임시 비밀번호 생성
- 계정 잠금 해제
- 이메일 발송 (구현 예정)

---

### 6. 헬스 체크
```http
GET /api/auth/health
```

**응답 (200 OK)**
```json
{
  "success": true,
  "message": "Auth API is healthy",
  "data": "OK",
  "timestamp": "2025-11-05T10:30:00"
}
```

---

## 테스트 계정

애플리케이션 실행 시 자동으로 생성되는 테스트 계정:

| 역할 | 학번/ID | 비밀번호 | 이메일 | 권한 |
|------|---------|----------|--------|------|
| 관리자 | admin | Admin123! | admin@scms.com | ADMIN |
| 학생1 | 2024001 | Student123! | student1@scms.com | STUDENT |
| 학생2 | 2024002 | Student123! | student2@scms.com | STUDENT |
| 상담사 | counselor01 | Counselor123! | counselor@scms.com | COUNSELOR |
| 강사 | instructor01 | Instructor123! | instructor@scms.com | INSTRUCTOR |

---

## 에러 응답 형식

모든 에러는 통일된 형식으로 반환됩니다:

```json
{
  "code": "A002",
  "message": "이메일 또는 비밀번호가 올바르지 않습니다",
  "status": 401,
  "timestamp": "2025-11-05T10:30:00",
  "errors": null
}
```

### 주요 에러 코드

| 코드 | HTTP 상태 | 설명 |
|------|-----------|------|
| A001 | 401 | 인증이 필요합니다 |
| A002 | 401 | 이메일 또는 비밀번호가 올바르지 않습니다 |
| A003 | 403 | 계정이 잠겼습니다 |
| A004 | 403 | 비활성화된 계정입니다 |
| A005 | 401 | 유효하지 않은 토큰입니다 |
| A006 | 401 | 만료된 토큰입니다 |
| U001 | 404 | 사용자를 찾을 수 없습니다 |
| U002 | 409 | 이미 사용중인 이메일입니다 |
| U003 | 409 | 이미 사용중인 학번입니다 |
| U004 | 400 | 비밀번호가 올바르지 않습니다 |
| U005 | 400 | 비밀번호가 일치하지 않습니다 |

전체 에러 코드 목록: `src/main/java/com/scms/exception/ErrorCode.java`

---

## cURL 테스트 예제

### 회원가입
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "2024999",
    "name": "테스트",
    "email": "test@example.com",
    "password": "Test123!",
    "passwordConfirm": "Test123!",
    "dateOfBirth": "2005-01-01",
    "address": "서울시"
  }'
```

### 로그인
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "2024001",
    "password": "Student123!"
  }'
```

### 로그아웃
```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### 토큰 갱신
```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "YOUR_REFRESH_TOKEN"
  }'
```

---

## 보안 기능

### JWT 토큰 보안
- HMAC SHA256 서명
- Secret Key는 환경변수로 관리 (코드 하드코딩 금지)
- Access Token: 1시간 유효
- Refresh Token: 7일 유효
- 로그아웃 시 블랙리스트에 추가

### 비밀번호 보안
- BCrypt 암호화 (NFR-SEC-001 요구사항)
- 평문 저장 절대 금지
- 8자 이상, 영문+숫자+특수문자 필수

### 계정 보안
- 5회 로그인 실패 시 자동 잠금
- 로그인 이력 저장 (IP, User-Agent, 성공/실패)
- 계정 활성화/비활성화 기능

### URL 접근 제어
- `/api/auth/**`: 인증 불필요 (회원가입, 로그인 등)
- `/api/admin/**`: ADMIN 권한 필수
- `/api/counselor/**`: COUNSELOR 또는 ADMIN 권한
- `/api/instructor/**`: INSTRUCTOR 또는 ADMIN 권한
- `/api/**`: 그 외 모든 API는 인증 필수

---

## 다음 구현 예정

- [ ] 비교과 프로그램 API (PRG-001~037)
- [ ] 역량 진단 API (COMP-001~020)
- [ ] 상담 관리 API (CNSL-001~024)
- [ ] 마일리지 API (PRG-014, PRG-027~029)
- [ ] 마이페이지 API (MY-001~009)
- [ ] 관리자 대시보드 API (ADM-001~022)
