# Simple HTTP Server

4명의 팀원이 협업하여 개발하는 HTTP/1.1 서버입니다.

## 프로젝트 개요

### 목표
- Socket API를 직접 사용하여 HTTP 서버 구현
- RFC 2616 (HTTP/1.1) 프로토콜 직접 파싱 및 생성
- 외부 라이브러리 없이 Java 표준 라이브러리만 사용

### 제약사항
- ❌ Apache HttpClient, Spring, Tomcat 등 사용 금지
- ✅ `java.net.*`, `java.io.*` 표준 라이브러리만 사용
- ✅ Socket, ServerSocket 직접 구현
- ✅ HTTP 메시지 직접 파싱/생성

## 팀원 및 역할

| 팀원 | 담당 파일 | 작업 내용 |
|------|-----------|-----------|
| 1번 | ResponseBuilder.java | 응답 라인 생성, HTML 본문 생성 |
| 2번 | RequestParser.java | 요청 라인 파싱, 전체 아키텍처 설계 (팀장) |
| 3번 | RequestParser.java | 요청 헤더 파싱 |
| 4번 | ResponseBuilder.java | 응답 헤더 생성, 응답 전송 |

## 프로젝트 구조
```
ourserver/
├── src/
│   ├── HttpServer.java          # 메인 서버
│   ├── HttpRequest.java         # 요청 DTO
│   ├── HttpResponse.java        # 응답 DTO
│   ├── RequestParser.java       # 요청 파싱 (2번, 3번)
│   └── ResponseBuilder.java     # 응답 생성 (1번, 4번)
├── test/
│   ├── RequestParserTest.java   # 요청 파싱 테스트
│   └── ResponseBuilderTest.java # 응답 생성 테스트
└── 문서/
    ├── README.md                # 이 파일
    ├── COLLABORATION.md         # Git 협업 가이드
    ├── TODO.md                  # 할 일 목록
    └── RFC_GUIDE.md             # RFC 읽기 가이드
```

## 컴파일 및 실행

### 컴파일
```bash
# src 디렉토리로 이동
cd src

# 모든 Java 파일 컴파일
javac *.java

# 또는 개별 컴파일
javac HttpServer.java HttpRequest.java HttpResponse.java RequestParser.java ResponseBuilder.java
```

### 실행
```bash
# src 디렉토리에서
java HttpServer
```

실행하면 다음과 같이 출력됩니다:
```
========================================
Simple HTTP Server
========================================
접속 가능 주소: http://192.168.0.15:8080
로컬 접속: http://localhost:8080
========================================
서버가 시작되었습니다.
```

### 테스트
```bash
# 브라우저에서 접속
http://localhost:8080

# 또는 다른 컴퓨터에서 (같은 WiFi 필요)
http://[서버IP]:8080
```

### 단위 테스트 실행
```bash
# test 디렉토리로 이동 (src에서 상위로)
cd ../test

# src 클래스들을 클래스패스에 포함하여 컴파일
javac -cp ../src *.java

# 테스트 실행 (src를 클래스패스에 포함)
java -cp ../src:. RequestParserTest
java -cp ../src:. ResponseBuilderTest
```

## 개발 일정

### Day 1 (전체 함께) - 기본 틀 작성
- [ ] 프로젝트 구조 이해
- [ ] HttpRequest, HttpResponse 클래스 getter/setter 완성
- [ ] 소켓 서버 기본 틀 작성
- [ ] RFC 2616 Section 1, 4 함께 읽기
- [ ] Git 브랜치 생성

### Day 2 (개별 작업) - 각자 담당 기능 구현
- [ ] 1번: 응답 라인 생성, HTML 본문 생성
- [ ] 2번: 요청 라인 파싱
- [ ] 3번: 요청 헤더 파싱
- [ ] 4번: 응답 헤더 생성, 응답 전송

### Day 3 (통합) - 코드 통합 및 테스트
- [ ] 각자 작성한 코드 통합
- [ ] handleClient 메서드 완성
- [ ] 전체 테스트
- [ ] 버그 수정

### Day 4 (마무리) - 최종 점검 및 제출
- [ ] 최종 테스트
- [ ] 문서화
- [ ] 시연 준비

### Day 5-7 (추가 기능, 선택사항)
- [ ] 정적 파일 서빙
- [ ] Admin 모니터링 페이지
- [ ] 멀티스레드 처리

## 시연 방법

### 방법 1: 같은 WiFi 네트워크 (추천)
1. 모든 노트북을 같은 WiFi에 연결
2. 한 노트북에서 서버 실행
3. 다른 노트북에서 서버 IP로 접속
4. 여러 브라우저에서 동시 접속 시연

### 방법 2: 로컬 테스트
1. 서버 실행
2. 같은 컴퓨터에서 브라우저 여러 개 열기
3. `localhost:8080` 접속

## 참고 자료

### RFC 2616 (HTTP/1.1)
- [RFC 2616 전체 문서](https://www.rfc-editor.org/rfc/rfc2616)
- Section 4: HTTP Message (전체 필독)
- Section 5: Request (2번, 3번 필독)
- Section 6: Response (1번, 4번 필독)

### Java 문서
- [ServerSocket API](https://docs.oracle.com/javase/8/docs/api/java/net/ServerSocket.html)
- [Socket API](https://docs.oracle.com/javase/8/docs/api/java/net/Socket.html)
- [BufferedReader API](https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html)

## 문제 해결

### 포트가 이미 사용 중
```
java.net.BindException: Address already in use
```
→ 다른 프로그램이 8080 포트를 사용 중입니다. 서버 코드에서 포트 번호를 변경하세요.

### 방화벽 문제
Windows 방화벽이 연결을 차단할 수 있습니다.
→ Windows 방화벽 설정에서 Java 허용

### 다른 컴퓨터에서 접속 안 됨
→ 같은 WiFi에 연결되어 있는지 확인
→ 서버 IP 주소가 올바른지 확인

## 팀 연락처
- 팀장 (2번): [연락처 입력]
- 단톡방: [링크 입력]
- 통합 회의: Day 3, [시간], [장소]
