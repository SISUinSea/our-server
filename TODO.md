# 팀원별 할 일 체크리스트

## Day 1: 전체 함께 (2-3시간)

### 전체 팀원
- [ ] Git 저장소 clone
- [ ] 개발 환경 설정 (JDK, IDE)
- [ ] HttpRequest.java getter/setter 작성
- [ ] HttpResponse.java getter/setter 작성
- [ ] HttpServer.java 기본 틀 작성
- [ ] RFC 2616 Section 1, 4 함께 읽기
- [ ] 자기 브랜치 생성
- [ ] 다음 회의 일정 잡기 (Day 3)

---

## Day 2: 개별 작업

### 1번 팀원 (예상 3-4시간)
**파일**: `src/ResponseBuilder.java`, `test/ResponseBuilderTest.java`

- [ ] **ResponseBuilder.java 작업**
  - [ ] `getStatusMessage()` 구현
    - [ ] 200, 404, 500 상태 코드 처리
    - [ ] switch 문 사용
  - [ ] `buildStatusLine()` 구현
    - [ ] "HTTP/1.1 [코드] [메시지]\r\n" 형식
  - [ ] `buildSimpleHtmlBody()` 구현
    - [ ] 간단한 HTML 생성

- [ ] **ResponseBuilderTest.java 작성**
  - [ ] 응답 라인 생성 테스트
  - [ ] 여러 상태 코드 테스트 (200, 404, 500)
  - [ ] HTML 본문 생성 테스트

- [ ] **Git 작업**
  - [ ] 커밋 및 푸시
  - [ ] Pull Request 생성

**읽을 자료:**
- RFC 2616 Section 6.1 (Status-Line)
- RFC 2616 Section 6.1.1 (Status Code)

---

### 2번 팀원 (예상 4-5시간)
**파일**: `src/RequestParser.java`, `test/RequestParserTest.java`

- [ ] **RequestParser.java 작업**
  - [ ] `parseRequestLine()` 구현
    - [ ] 문자열 split
    - [ ] HttpRequest에 저장
    - [ ] 예외 처리

- [ ] **RequestParserTest.java 작성**
  - [ ] 요청 라인 파싱 테스트
  - [ ] 잘못된 형식 테스트

- [ ] **전체 아키텍처 문서화**
  - [ ] 클래스 다이어그램 작성 (선택)
  - [ ] 통합 시 주의사항 정리

- [ ] **Git 작업**
  - [ ] 커밋 및 푸시
  - [ ] Pull Request 생성

**읽을 자료:**
- RFC 2616 Section 5.1 (Request-Line)
- BufferedReader API 문서

---

### 3번 팀원 (예상 3-4시간)
**파일**: `src/RequestParser.java`, `test/RequestParserTest.java`

- [ ] **RequestParser.java 작업**
  - [ ] `parseHeaders()` 구현
    - [ ] 반복문으로 헤더 읽기
    - [ ] ":" 기준으로 split (주의: `split(":", 2)`)
    - [ ] trim()으로 공백 제거
    - [ ] Map에 저장
    - [ ] 빈 줄까지 읽기

- [ ] **RequestParserTest.java 작성**
  - [ ] 헤더 파싱 테스트
  - [ ] 여러 헤더 테스트

- [ ] **Git 작업**
  - [ ] 커밋 및 푸시
  - [ ] Pull Request 생성

**읽을 자료:**
- RFC 2616 Section 4.2 (Message Headers)
- Map 사용법

**주의사항:**
- `split(":", 2)` 사용 (값에 콜론이 있을 수 있음)
- trim()으로 앞뒤 공백 제거 필수

---

### 4번 팀원 (예상 3-4시간)
**파일**: `src/ResponseBuilder.java`, `test/ResponseBuilderTest.java`

- [ ] **ResponseBuilder.java 작업**
  - [ ] `buildHeaders()` 구현
    - [ ] Content-Type 헤더
    - [ ] Content-Length 계산 (바이트!)
    - [ ] Connection 헤더
    - [ ] 빈 줄 추가
  - [ ] `writeResponse()` 구현
    - [ ] 상태 라인 쓰기
    - [ ] 헤더 쓰기
    - [ ] 본문 쓰기
    - [ ] flush()

- [ ] **ResponseBuilderTest.java 작성**
  - [ ] 헤더 생성 테스트
  - [ ] Content-Length 계산 테스트
  - [ ] 전체 응답 쓰기 테스트

- [ ] **Git 작업**
  - [ ] 커밋 및 푸시
  - [ ] Pull Request 생성

**읽을 자료:**
- RFC 2616 Section 7.1 (Entity Header)
- RFC 2616 Section 14.13 (Content-Length)
- OutputStream 사용법

**주의사항:**
- Content-Length는 문자 개수가 아닌 바이트 개수!
- `body.getBytes(StandardCharsets.UTF_8).length` 사용

---

## Day 3: 통합 (3-4시간)

### 전체 팀원
- [ ] **최신 코드 동기화**
  - [ ] 최신 main 코드 받기
  - [ ] Pull Request 리뷰 (2번 주도)
  - [ ] 충돌 있으면 함께 해결
  - [ ] main에 merge

- [ ] **HttpServer.java 완성**
  - [ ] `main()` 메서드 구현
    - [ ] ServerSocket 생성
    - [ ] 서버 시작 메시지 출력
    - [ ] 무한 루프로 클라이언트 대기
    - [ ] 예외 처리
  - [ ] `handleClient()` 메서드 구현
    - [ ] InputStream, OutputStream 얻기
    - [ ] RequestParser로 요청 파싱
    - [ ] 요청 내용 로그 출력
    - [ ] ResponseBuilder로 응답 생성
    - [ ] 응답 전송
    - [ ] 소켓 닫기

- [ ] **통합 테스트**
  - [ ] 서버 실행
  - [ ] 브라우저 접속
  - [ ] 로그 확인
  - [ ] 버그 수정

- [ ] **코드 정리**
  - [ ] 주석 보완
  - [ ] 불필요한 TODO 제거
  - [ ] 코드 포맷팅

---

## Day 4: 마무리 (2-3시간)

### 전체 팀원
- [ ] **최종 테스트**
  - [ ] 여러 브라우저에서 테스트
    - [ ] Chrome
    - [ ] Firefox
    - [ ] Safari
  - [ ] 동시 접속 테스트
  - [ ] 에러 케이스 테스트
    - [ ] 잘못된 요청 처리
    - [ ] 네트워크 오류 처리

- [ ] **문서 정리**
  - [ ] README 업데이트
  - [ ] 주석 보완
  - [ ] TODO 제거
  - [ ] 실행 방법 최종 확인

- [ ] **시연 준비**
  - [ ] 시연 시나리오 작성
  - [ ] 리허설
  - [ ] WiFi 네트워크 테스트

- [ ] **제출 파일 준비**
  - [ ] .class 파일 삭제
  - [ ] .git 디렉토리 제외
  - [ ] 압축
  - [ ] 팀원 이름 확인

---

## Day 5-7: 추가 기능 (선택)

### 시간이 남으면 추가할 기능

#### 정적 파일 서빙 (2번, 3번)
- [ ] FileHandler 클래스 작성
- [ ] Content-Type 자동 판단
- [ ] 파일 읽기 및 전송
- [ ] 404 에러 처리

#### Admin 모니터링 페이지 (1번, 4번)
- [ ] ServerStats 클래스
- [ ] 실시간 통계 수집
- [ ] HTML 페이지 생성
- [ ] CSS 스타일링

#### 멀티스레드 처리 (2번)
- [ ] Thread Pool 구현
- [ ] 동시 연결 처리
- [ ] 성능 테스트

#### 404 페이지 (3번 또는 4번)
- [ ] ErrorPageBuilder 클래스
- [ ] 커스텀 404 페이지 디자인

---

## 긴급 체크리스트

### 막혔을 때
1. [ ] TODO 주석 다시 읽기
2. [ ] RFC 문서 다시 읽기
3. [ ] 테스트 코드로 확인
4. [ ] 단톡방에 질문
5. [ ] 2번 팀원에게 연락

### 제출 전 최종 확인
- [ ] 컴파일 오류 없음
- [ ] 서버 실행됨
- [ ] 브라우저 접속 가능
- [ ] 로그 출력 정상
- [ ] .class 파일 삭제
- [ ] README 최신 상태
- [ ] 팀원 이름 확인

---

## 주간 일정 요약

| 날짜 | 작업 | 참여 | 주요 내용 |
|------|------|------|-----------|
| Day 1 | 프로젝트 설정, 기본 틀 | 전체 | DTO 클래스, RFC 읽기 |
| Day 2 | 개별 작업 | 개별 | 각자 담당 기능 구현 |
| Day 3 | 통합 | 전체 | 코드 통합, 테스트 |
| Day 4 | 마무리 | 전체 | 최종 테스트, 문서화 |
| Day 5-7 | 추가 기능 | 선택 | 파일 서빙, 모니터링 |

---

## 팀원별 작업 요약

| 팀원 | 핵심 작업 | 예상 시간 | 난이도 |
|------|-----------|-----------|--------|
| 1번 | 응답 라인 생성, HTML 본문 | 3-4시간 | ⭐⭐ |
| 2번 | 요청 라인 파싱, 아키텍처 | 4-5시간 | ⭐⭐⭐ |
| 3번 | 요청 헤더 파싱 | 3-4시간 | ⭐⭐ |
| 4번 | 응답 헤더 생성, 응답 전송 | 3-4시간 | ⭐⭐⭐ |

---

## 연락처
- 팀장 (2번): [전화번호]
- 단톡방: [링크]
- 회의: Day 3, [시간], [장소]
