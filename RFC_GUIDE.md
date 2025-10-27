# RFC 2616 읽기 가이드

HTTP/1.1 프로토콜 명세(RFC 2616)에서 우리 프로젝트에 필요한 부분만 정리했습니다.

## RFC 2616이란?

**RFC (Request for Comments)**: 인터넷 표준 문서
**RFC 2616**: HTTP/1.1 프로토콜 명세 (1999년 발표)

**전체 링크**: https://www.rfc-editor.org/rfc/rfc2616

**분량**: 총 176페이지 (하지만 우리는 일부만 읽으면 됨!)

---

## 전체가 함께 읽을 부분 (Day 1)

### Section 1: Introduction (5분)
**내용**: HTTP가 무엇인지, 왜 필요한지

**핵심 요약**:
- HTTP는 웹에서 정보를 주고받는 프로토콜
- 클라이언트(브라우저)가 요청, 서버가 응답
- 텍스트 기반 프로토콜

**읽지 않아도 됨**: 우리가 이미 아는 내용

---

### Section 4: HTTP Message (15분) ⭐ 중요!
**내용**: HTTP 메시지의 전체 구조

**핵심 개념**:

#### 4.1 Message Types
```
HTTP-message = Request | Response
```
- 두 종류: 요청과 응답

#### 4.2 Message Headers
```
message-header = field-name ":" [ field-value ]
```
- 헤더 형식: `이름: 값`
- 예: `Host: localhost:8080`

#### 4.3 Message Body
- 실제 데이터 (HTML, JSON 등)
- 헤더와 본문 사이에 빈 줄 필수

#### HTTP 메시지 구조
```
요청 또는 상태 라인
헤더1: 값1
헤더2: 값2
(빈 줄)
본문
```

**반드시 이해해야 할 것**:
- ✅ 헤더와 본문 사이 빈 줄
- ✅ CRLF (`\r\n`)로 줄 구분
- ✅ 헤더 형식: `이름: 값`

---

### 간단한 예제 (함께 보기, 15분)

#### HTTP 요청 예제
```
GET /index.html HTTP/1.1\r\n
Host: localhost:8080\r\n
User-Agent: Mozilla/5.0\r\n
Accept: text/html\r\n
\r\n
```

**분석**:
- Line 1: 요청 라인 (Method URI Version)
- Line 2-4: 헤더들
- Line 5: 빈 줄 (헤더 끝)
- 본문 없음 (GET 요청)

#### HTTP 응답 예제
```
HTTP/1.1 200 OK\r\n
Content-Type: text/html\r\n
Content-Length: 20\r\n
\r\n
<h1>Hello!</h1>
```

**분석**:
- Line 1: 상태 라인 (Version Code Message)
- Line 2-3: 헤더들
- Line 4: 빈 줄
- Line 5: 본문 (HTML)

---

## 팀원별 필독 섹션

### 2번 팀원: 요청 라인 파싱

#### Section 5: Request (30분)

##### 5.1 Request-Line ⭐⭐⭐
```
Request-Line = Method SP Request-URI SP HTTP-Version CRLF
```

**예시**:
```
GET /index.html HTTP/1.1
```

**구성 요소**:
1. **Method**: 동작 (GET, POST, HEAD 등)
2. **SP**: 공백 (Space)
3. **Request-URI**: 경로 (`/index.html`)
4. **HTTP-Version**: 버전 (`HTTP/1.1`)
5. **CRLF**: 줄바꿈 (`\r\n`)

**주요 메서드** (5.1.1):
- `GET`: 리소스 가져오기
- `POST`: 데이터 전송
- `HEAD`: 헤더만 가져오기

**우리 프로젝트**:
- GET만 구현해도 충분
- POST는 추가 기능

##### 5.3 Request-URI
```
Request-URI = "*" | absoluteURI | abs_path | authority
```

**우리는 abs_path만 처리**:
- `/index.html`
- `/static/style.css`
- `/admin`

**처리 방법**:
```java
String uri = parts[1];  // "/index.html"
```

---

### 3번 팀원: 요청 헤더 파싱

#### Section 4.2: Message Headers (20분)

**헤더 형식**:
```
message-header = field-name ":" [ field-value ]
```

**예시**:
```
Host: localhost:8080
User-Agent: Mozilla/5.0
Content-Length: 123
```

**주의사항**:
1. 콜론(`:`) 뒤 공백 선택적
2. 값에 콜론이 있을 수 있음
   ```
   Date: Mon, 27 Oct 2025 12:00:00 GMT
   ```
3. 대소문자 무관 (보통 첫 글자 대문자)

**처리 방법**:
```java
String[] parts = line.split(":", 2);  // 2로 제한!
String name = parts[0].trim();
String value = parts[1].trim();
```

#### Section 14: Header Field Definitions (참고용)

**자주 사용하는 헤더**:
- `Host`: 서버 주소 (필수!)
- `User-Agent`: 클라이언트 정보
- `Accept`: 받을 수 있는 타입
- `Content-Type`: 본문 타입
- `Content-Length`: 본문 길이

**우리 프로젝트**:
- 모든 헤더를 Map에 저장
- 특정 헤더만 사용

---

### 1번 팀원: 응답 라인 생성

#### Section 6: Response (30분)

##### 6.1 Status-Line ⭐⭐⭐
```
Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
```

**예시**:
```
HTTP/1.1 200 OK
```

**구성 요소**:
1. **HTTP-Version**: 항상 `HTTP/1.1`
2. **Status-Code**: 3자리 숫자
3. **Reason-Phrase**: 상태 설명
4. **CRLF**: `\r\n`

##### 6.1.1 Status Code and Reason Phrase ⭐⭐⭐

**상태 코드 분류**:
- **2xx**: 성공
  - `200 OK`: 성공
  - `201 Created`: 생성됨

- **4xx**: 클라이언트 오류
  - `400 Bad Request`: 잘못된 요청
  - `404 Not Found`: 찾을 수 없음

- **5xx**: 서버 오류
  - `500 Internal Server Error`: 서버 오류

**우리 프로젝트**:
```java
switch (statusCode) {
    case 200: return "OK";
    case 404: return "Not Found";
    case 500: return "Internal Server Error";
}
```

---

### 4번 팀원: 응답 헤더 생성

#### Section 7.1: Entity Header Fields (20분)

**Entity Header**: 본문에 대한 정보

**필수 헤더**:

##### 1. Content-Type (14.17)
```
Content-Type: text/html; charset=UTF-8
```
- 본문의 MIME 타입
- `text/html`: HTML 문서
- `text/plain`: 일반 텍스트
- `application/json`: JSON

##### 2. Content-Length (14.13) ⭐ 중요!
```
Content-Length: 123
```
- 본문의 **바이트** 길이
- 문자 개수가 아님!

**계산 방법**:
```java
String body = "...";
int length = body.getBytes(StandardCharsets.UTF_8).length;
```

##### 3. Connection (14.10)
```
Connection: close
```
- `close`: 응답 후 연결 종료
- `keep-alive`: 연결 유지 (추가 기능)

**우리 프로젝트**:
```java
StringBuilder headers = new StringBuilder();
headers.append("Content-Type: text/html; charset=UTF-8\r\n");
headers.append("Content-Length: " + length + "\r\n");
headers.append("Connection: close\r\n");
headers.append("\r\n");  // 빈 줄!
```

---

## RFC 읽는 팁

### 1. 완벽히 이해하려 하지 마세요
- RFC는 매우 상세하고 복잡
- 우리에게 필요한 부분만 발췌
- 모르는 부분은 건너뛰기

### 2. 예제로 이해하세요
- 추상적인 설명보다 예제가 이해하기 쉬움
- 직접 문자열로 만들어보기

### 3. 구현하면서 다시 읽으세요
- 처음엔 이해 안 될 수 있음
- 코드 작성하다 막히면 다시 참고

### 4. 팀원들과 함께 읽으세요
- Day 1에 함께 읽으면서 논의
- 이해한 내용 서로 설명

---

## 읽기 순서

### Day 1 (함께)
1. Section 1 (5분)
2. Section 4 (15분)
3. 예제 함께 보기 (15분)

### Day 2 (개별)
- **2번**: Section 5 (30분)
- **3번**: Section 4.2, 14 (20분)
- **1번**: Section 6 (30분)
- **4번**: Section 7.1, 14 (20분)

---

## 자주 하는 질문

### Q: RFC를 다 읽어야 하나요?
**A**: 아니요! 위에 표시한 섹션만 읽으면 됩니다.

### Q: 영어라서 어려워요
**A**: 예제를 보면서 이해하세요. 코드로 만들면 더 쉬워집니다.

### Q: 이해가 안 가요
**A**: 괜찮아요. 구현하면서 다시 읽으면 이해됩니다.

### Q: 더 공부하고 싶어요
**A**:
- RFC 전체 읽기
- HTTP/2, HTTP/3 알아보기
- 실제 서버 (Nginx, Apache) 구조 공부

---

## 참고 링크

- [RFC 2616 전체](https://www.rfc-editor.org/rfc/rfc2616)
- [RFC 2616 HTML 버전](https://www.w3.org/Protocols/rfc2616/rfc2616.html)
- [MDN HTTP 문서](https://developer.mozilla.org/ko/docs/Web/HTTP)
- [HTTP 상태 코드 목록](https://developer.mozilla.org/ko/docs/Web/HTTP/Status)

---

## 요약

| 팀원 | 읽을 섹션 | 시간 | 핵심 내용 |
|------|-----------|------|-----------|
| 전체 | 1, 4 | 20분 | HTTP 메시지 구조 |
| 2번 | 5, 5.1 | 30분 | Request-Line 형식 |
| 3번 | 4.2, 14 | 20분 | 헤더 형식 |
| 1번 | 6, 6.1 | 30분 | Status-Line 형식 |
| 4번 | 7.1, 14 | 20분 | Content-Type, Length |

**총 소요 시간**: 약 1-2시간 (천천히 읽으면서)
