import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * HTTP 요청을 파싱하는 클래스
 *
 * 2번 팀원: 요청 라인 파싱 (parseRequestLine, readRequestLine)
 * 3번 팀원: 요청 헤더 파싱 (parseHeaders)
 */
public class RequestParser {

    /**
     * InputStream에서 HTTP 요청을 읽어 HttpRequest 객체로 변환합니다.
     *
     * 이 메서드는 Day 3 통합 시 완성됩니다.
     * 2번과 3번 팀원이 만든 메서드를 조합합니다.
     *
     * @param input 클라이언트 소켓의 InputStream
     * @return 파싱된 HttpRequest 객체
     * @throws IOException 네트워크 오류 시
     */
    public static HttpRequest parseRequest(InputStream input) throws IOException {
        // TODO: Day 3 통합 작업
        // 1. BufferedReader 생성
        // 2. 첫 줄 읽기 (요청 라인)
        // 3. HttpRequest 객체 생성
        // 4. parseRequestLine 호출하여 요청 라인 파싱
        // 5. parseHeaders 호출하여 헤더 파싱
        // 6. HttpRequest 객체 반환

        // 구현 힌트:
        // BufferedReader reader = new BufferedReader(
        //     new InputStreamReader(input, StandardCharsets.UTF_8));
        //
        // String requestLine = reader.readLine();
        // HttpRequest request = new HttpRequest();
        //
        // parseRequestLine(requestLine, request);
        // parseHeaders(reader, request);
        //
        // return request;

        throw new UnsupportedOperationException("아직 구현되지 않음");
    }

    // ============================================
    // 2번 팀원 담당 영역
    // ============================================

    /**
     * [2번 팀원 작업]
     * HTTP 요청 라인을 파싱합니다.
     *
     * RFC 2616 Section 5.1 참고:
     * Request-Line = Method SP Request-URI SP HTTP-Version CRLF
     *
     * 입력 예시: "GET /index.html HTTP/1.1"
     * 출력: request.method = "GET"
     *       request.uri = "/index.html"
     *       request.httpVersion = "HTTP/1.1"
     *
     * 구현 힌트:
     * 1. 문자열을 공백(" ")으로 split
     *    String[] parts = requestLine.split(" ");
     * 2. parts.length가 3인지 검증 (아니면 예외)
     *    if (parts.length != 3) {
     *        throw new IllegalArgumentException("잘못된 요청 라인 형식: " + requestLine);
     *    }
     * 3. parts[0] → method, parts[1] → uri, parts[2] → httpVersion
     * 4. request 객체에 setter로 저장
     *    request.setMethod(parts[0]);
     *    request.setUri(parts[1]);
     *    request.setHttpVersion(parts[2]);
     *
     * 예외 처리:
     * - IllegalArgumentException: 형식이 잘못된 경우
     *
     * 예상 작업 시간: 3-4시간
     *
     * @param requestLine 요청 라인 문자열
     * @param request 결과를 저장할 HttpRequest 객체
     * @throws IllegalArgumentException 형식이 잘못된 경우
     */
    public static void parseRequestLine(String requestLine, HttpRequest request) {
        // TODO: 2번 팀원 구현
        throw new UnsupportedOperationException("2번 팀원이 구현 필요");
    }

    // ============================================
    // 3번 팀원 담당 영역
    // ============================================

    /**
     * [3번 팀원 작업]
     * HTTP 요청 헤더들을 파싱합니다.
     *
     * RFC 2616 Section 4.2 참고:
     * message-header = field-name ":" [ field-value ]
     *
     * 입력 예시:
     * "Host: localhost:8080"
     * "User-Agent: Mozilla/5.0"
     * "Accept: text/html"
     * ""  ← 빈 줄이 헤더의 끝
     *
     * 출력: request.headers = {
     *   "Host": "localhost:8080",
     *   "User-Agent": "Mozilla/5.0",
     *   "Accept": "text/html"
     * }
     *
     * 구현 힌트:
     * 1. BufferedReader로 한 줄씩 읽기
     *    String line;
     *    while ((line = reader.readLine()) != null) {
     *
     * 2. 빈 줄("")이 나올 때까지 반복
     *    if (line.isEmpty()) {
     *        break;  // 헤더 끝
     *    }
     *
     * 3. 각 줄을 ":"로 split (주의: split(":", 2) 사용!)
     *    String[] parts = line.split(":", 2);
     *    if (parts.length == 2) {
     *
     * 4. 헤더 이름과 값을 trim()으로 공백 제거
     *    String name = parts[0].trim();
     *    String value = parts[1].trim();
     *
     * 5. Map에 저장
     *    request.getHeaders().put(name, value);
     *    }
     *    }
     *
     * 주의사항:
     * - split(":", 2)를 사용하세요 (값에 콜론이 있을 수 있음)
     *   예: "Date: Mon, 27 Oct 2025 12:00:00 GMT"
     * - trim()으로 앞뒤 공백 제거
     * - 빈 줄이 나오면 반복 종료
     *
     * 예외 처리:
     * - IOException: 네트워크 오류
     *
     * 예상 작업 시간: 3-4시간
     *
     * @param reader BufferedReader (요청 라인 다음부터 읽기)
     * @param request 결과를 저장할 HttpRequest 객체
     * @throws IOException 읽기 실패 시
     */
    public static void parseHeaders(BufferedReader reader, HttpRequest request)
            throws IOException {
        // TODO: 3번 팀원 구현
        throw new UnsupportedOperationException("3번 팀원이 구현 필요");
    }
}
