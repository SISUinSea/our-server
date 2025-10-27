# Git 협업 가이드

이 문서는 4명의 팀원이 Git을 사용하여 효율적으로 협업하는 방법을 설명합니다.

## 브랜치 전략

### 브랜치 구조
```
main (2번 팀원이 관리하는 메인 브랜치)
├── feature/request-line        (2번 팀원)
├── feature/request-headers     (3번 팀원)
├── feature/response-line       (1번 팀원)
└── feature/response-headers    (4번 팀원)
```

### 브랜치 설명
- **main**: 통합 브랜치 (2번 팀원이 관리)
  - 템플릿 코드 포함
  - Pull Request 승인 후에만 merge
  - 직접 push 금지 (2번 팀원 제외)

- **feature/request-line**: 2번 팀원 작업 브랜치
  - 요청 라인 파싱 구현

- **feature/request-headers**: 3번 팀원 작업 브랜치
  - 요청 헤더 파싱 구현

- **feature/response-line**: 1번 팀원 작업 브랜치
  - 응답 라인 생성 구현

- **feature/response-headers**: 4번 팀원 작업 브랜치
  - 응답 헤더 생성 구현

## 작업 시작하기

### 1. 저장소 복제 (최초 1회만)
```bash
git clone https://github.com/[팀명]/ourserver.git
cd ourserver
```

### 2. 자기 작업 브랜치 생성
```bash
# main 브랜치에서 시작
git checkout main

# 자기 브랜치 생성 (팀원별로 다름)
# 1번 팀원:
git checkout -b feature/response-line

# 2번 팀원:
git checkout -b feature/request-line

# 3번 팀원:
git checkout -b feature/request-headers

# 4번 팀원:
git checkout -b feature/response-headers
```

### 3. 작업하기
```bash
# 파일 수정...
# 예: ResponseBuilder.java 수정

# 변경된 파일 확인
git status

# 변경사항 스테이징
git add src/ResponseBuilder.java

# 또는 모든 변경사항
git add .
```

### 4. 커밋하기
```bash
git commit -m "feat: 응답 라인 생성 기능 구현"
```

### 5. GitHub에 푸시
```bash
# 자기 브랜치를 origin에 푸시
git push origin feature/response-line
```

## Pull Request 생성

### GitHub 웹사이트에서:
1. 저장소 페이지 이동
2. "Pull requests" 탭 클릭
3. "New pull request" 버튼
4. **base: main** ← **compare: feature/[브랜치명]** 선택
5. 제목과 설명 작성
6. "Create pull request" 클릭
7. Reviewer로 **2번 팀원** 지정

### Pull Request 템플릿
```markdown
## 작업 내용
- [x] 응답 라인 생성 메서드 구현
- [x] 상태 코드 → 메시지 변환 구현
- [ ] 테스트 코드 작성 (진행 중)

## 테스트 방법
1. ResponseBuilderTest.java 실행
2. 예상 결과: "HTTP/1.1 200 OK" 출력

## 참고사항
- RFC 2616 Section 6.1 참고하여 구현
- 200, 404, 500 상태 코드 지원

## 질문/논의사항
- Content-Length 계산은 4번 팀원과 협의 필요
```

## 커밋 메시지 규칙

### 형식
```
타입: 간단한 설명 (50자 이내)

상세 설명 (선택사항)
```

### 타입 종류
- **feat**: 새로운 기능 추가
  ```
  feat: HTTP 요청 라인 파싱 기능 구현
  ```

- **fix**: 버그 수정
  ```
  fix: 헤더 파싱 시 공백 처리 오류 수정
  ```

- **docs**: 문서 수정
  ```
  docs: README에 실행 방법 추가
  ```

- **test**: 테스트 코드 추가/수정
  ```
  test: 요청 파싱 테스트 케이스 추가
  ```

- **refactor**: 코드 리팩토링
  ```
  refactor: parseRequestLine 메서드 가독성 개선
  ```

- **style**: 코드 포맷팅, 세미콜론 등
  ```
  style: 들여쓰기 수정
  ```

### 좋은 커밋 메시지 예시
```bash
✅ git commit -m "feat: HTTP 요청 헤더 파싱 구현"
✅ git commit -m "fix: Content-Length 계산 오류 수정"
✅ git commit -m "test: 요청 라인 파싱 테스트 추가"
```

### 나쁜 커밋 메시지 예시
```bash
❌ git commit -m "수정"
❌ git commit -m "asdf"
❌ git commit -m "완료"
```

## 최신 코드 받기

### main 브랜치 업데이트
```bash
# main으로 이동
git checkout main

# 최신 코드 받기
git pull origin main
```

### 자기 브랜치에 main 변경사항 반영
```bash
# 자기 브랜치로 이동
git checkout feature/response-line

# main의 변경사항을 현재 브랜치에 병합
git merge main

# 충돌이 없으면 자동으로 완료
# 충돌이 있으면 아래 "충돌 해결" 참고
```

## 충돌 해결

### 충돌이 발생하는 경우
- 두 사람이 같은 파일의 같은 부분을 수정했을 때
- 예: 2번과 3번이 모두 RequestParser.java를 수정

### 충돌 해결 방법

1. **충돌 파일 확인**
```bash
git status

# 출력 예시:
# both modified:   src/RequestParser.java
```

2. **파일 열기**
```java
public class RequestParser {
<<<<<<< HEAD
    // 내가 작성한 코드
    public static void parseRequestLine(...) {
        // 내 구현
    }
=======
    // main 브랜치의 코드
    public static void parseRequestLine(...) {
        // 다른 사람의 구현
    }
>>>>>>> main
}
```

3. **수동으로 수정**
- `<<<<<<<`, `=======`, `>>>>>>>` 표시 제거
- 두 코드를 적절히 합치거나 하나 선택
- 저장

4. **충돌 해결 완료**
```bash
git add src/RequestParser.java
git commit -m "merge: main 브랜치 변경사항 반영"
git push origin feature/request-line
```

### 충돌 예방
- 작업 전 항상 `git pull origin main`
- 같은 파일 동시 수정 피하기
- 자주 커밋하고 푸시하기

## 작업 플로우 요약
```
1. main에서 최신 코드 받기
   git checkout main
   git pull origin main

2. 자기 브랜치로 이동
   git checkout feature/[브랜치명]

3. main 변경사항 반영
   git merge main

4. 작업 (코드 수정)

5. 커밋
   git add .
   git commit -m "feat: 기능 설명"

6. 푸시
   git push origin feature/[브랜치명]

7. Pull Request 생성 (GitHub 웹)

8. 코드 리뷰 대기

9. Merge 완료 후 브랜치 삭제
```

## 주의사항

### ⚠️ 절대 금지
- ❌ main 브랜치에 직접 push (2번 팀원 제외)
- ❌ 다른 사람의 브랜치에 직접 push
- ❌ `git push -f` (force push) 사용

### ✅ 권장사항
- ✅ 작업 전 항상 최신 코드 받기
- ✅ 의미 있는 단위로 자주 커밋
- ✅ Pull Request에 상세한 설명 작성
- ✅ 막히면 단톡방에 질문
- ✅ Day 3 통합 회의 때 함께 충돌 해결

## Day별 Git 작업

### Day 1 (전체 함께)
- 2번 팀원이 템플릿 작성 → main에 push
- 나머지 팀원: clone + 브랜치 생성

### Day 2 (개별 작업)
- 각자 브랜치에서 작업
- 완료되면 커밋 + 푸시
- Pull Request 생성 (아직 merge 안 함)

### Day 3 (통합)
- 2번 팀원이 Pull Request 리뷰
- 문제 없으면 main에 merge
- 전체가 최신 main 받기
- handleClient 메서드 함께 완성

### Day 4 (마무리)
- 버그 수정 → 커밋 → 푸시
- 최종 코드 main에 통합

## 긴급 연락처
- 팀장 (2번): [전화번호]
- 단톡방: [링크]
- 통합 회의: Day 3, [시간], [장소]

## 도움말

### Git 명령어 모음
```bash
# 현재 브랜치 확인
git branch

# 변경사항 확인
git status

# 변경사항 상세 보기
git diff

# 커밋 히스토리
git log

# 브랜치 삭제 (merge 후)
git branch -d feature/response-line
```

### 자주 하는 실수

1. **main에 직접 작업**
   ```bash
   # 현재 브랜치 확인
   git branch
   # main이면 즉시 자기 브랜치로 이동!
   git checkout feature/[브랜치명]
   ```

2. **커밋 전에 푸시**
   ```bash
   # 에러: 커밋이 없어서 푸시 실패
   # 해결: 먼저 커밋하기
   git commit -m "메시지"
   git push
   ```

3. **충돌 무시하고 푸시**
   ```bash
   # 충돌이 있으면 푸시 안 됨
   # 해결: 충돌 먼저 해결하기
   ```

## 추가 학습 자료
- [Git 공식 문서](https://git-scm.com/doc)
- [GitHub 가이드](https://guides.github.com/)
- 팀원들과 함께 연습하기!
