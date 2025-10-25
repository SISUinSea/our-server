import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * HTTP 응답을 생성하는 클래스
 *
 * 1번 팀원: 응답 라인 생성 (buildStatusLine, getStatusMessage, buildSimpleHtmlBody)
 * 4번 팀원: 응답 헤더 생성 (buildHeaders, writeResponse)
 */
public class ResponseBuilder {

    // ============================================
    // 1번 팀원 담당 영역
    // ============================================

    /**
     * [1번 팀원 작업]
     * 상태 코드에 해당하는 상태 메시지를 반환합니다.
     *
     * RFC 2616 Section 6.1.1 참고:
     * Status-Code = 3DIGIT
     * Reason-Phrase = *<TEXT>
     *
     * 주요 상태 코드:
     * - 200: OK
     * - 201: Created
     * - 400: Bad Request
     * - 404: Not Found
     * - 500: Internal Server Error
     *
     * 구현 힌트:
     * switch (statusCode) {
     *     case 200:
     *         return "OK";
     *     case 404:
     *         return "Not Found";
     *     case 500:
     *         return "Internal Server Error";
     *     default:
     *         return "Unknown";
     * }
     *
     * 예상 작업 시간: 1시간
     *
     * @param statusCode HTTP 상태 코드
     * @return 상태 메시지
     */
    public static String getStatusMessage(int statusCode) {
        // TODO: 1번 팀원 구현
        throw new UnsupportedOperationException("1번 팀원이 구현 필요");
    }

    /**
     * [1번 팀원 작업]
     * HTTP 응답 상태 라인을 생성합니다.
     *
     * RFC 2616 Section 6.1 참고:
     * Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
     *
     * 입력: response.statusCode = 200
     * 출력: "HTTP/1.1 200 OK\r\n"
     *
     * 구현 힌트:
     * 1. getStatusMessage()를 호출하여 상태 메시지 얻기
     *    String message = getStatusMessage(response.getStatusCode());
     *
     * 2. String.format()으로 상태 라인 생성
     *    return String.format("HTTP/1.1 %d %s\r\n",
     *                         response.getStatusCode(), message);
     *
     * 주의사항:
     * - 반드시 \r\n(CRLF)로 끝나야 함
     * - HTTP 버전은 항상 "HTTP/1.1"
     *
     * 예상 작업 시간: 1시간
     *
     * @param response HttpResponse 객체
     * @return 상태 라인 문자열
     */
    public static String buildStatusLine(HttpResponse response) {
        // TODO: 1번 팀원 구현
        throw new UnsupportedOperationException("1번 팀원이 구현 필요");
    }

    /**
     * [1번 팀원 작업]
     * 간단한 HTML 본문을 생성합니다.
     *
     * 입력: message = "Hello, World!"
     * 출력: "<html><head><title>Simple HTTP Server</title></head>
     *        <body><h1>Hello, World!</h1></body></html>"
     *
     * 구현 힌트:
     * StringBuilder html = new StringBuilder();
     * html.append("<html>");
     * html.append("<head><title>Simple HTTP Server</title></head>");
     * html.append("<body>");
     * html.append("<h1>").append(message).append("</h1>");
     * html.append("</body>");
     * html.append("</html>");
     * return html.toString();
     *
     * 또는 String.format() 사용:
     * return String.format(
     *     "<html><head><title>Simple HTTP Server</title></head>" +
     *     "<body><h1>%s</h1></body></html>",
     *     message
     * );
     *
     * 예상 작업 시간: 1시간
     *
     * @param message 표시할 메시지
     * @return HTML 문자열
     */
    public static String buildSimpleHtmlBody(String message) {
        // TODO: 1번 팀원 구현
        throw new UnsupportedOperationException("1번 팀원이 구현 필요");
    }

    // ============================================
    // 4번 팀원 담당 영역
    // ============================================

    /**
     * [4번 팀원 작업]
     * HTTP 응답 헤더들을 생성합니다.
     *
     * RFC 2616 Section 7.1, 14 참고
     *
     * 필수 헤더:
     * - Content-Type: 응답 본문의 MIME 타입
     * - Content-Length: 응답 본문의 바이트 길이
     * - Connection: close (연결 종료)
     *
     * 출력 형식:
     * "Content-Type: text/html; charset=UTF-8\r\n"
     * "Content-Length: 123\r\n"
     * "Connection: close\r\n"
     * "\r\n"  ← 헤더 끝을 나타내는 빈 줄
     *
     * 구현 힌트:
     * 1. StringBuilder 사용
     *    StringBuilder headers = new StringBuilder();
     *
     * 2. Content-Type 추가 (기본값: "text/html; charset=UTF-8")
     *    headers.append("Content-Type: text/html; charset=UTF-8\r\n");
     *
     * 3. Content-Length 계산 (중요: 바이트 길이!)
     *    String body = response.getBody();
     *    int length = body.getBytes(StandardCharsets.UTF_8).length;
     *    headers.append("Content-Length: ").append(length).append("\r\n");
     *
     * 4. Connection: close 추가
     *    headers.append("Connection: close\r\n");
     *
     * 5. 마지막에 \r\n 추가 (헤더 끝)
     *    headers.append("\r\n");
     *
     * 6. 문자열 반환
     *    return headers.toString();
     *
     * 주의사항:
     * - Content-Length는 문자 개수가 아닌 바이트 개수!
     *   body.length()가 아니라 body.getBytes(StandardCharsets.UTF_8).length 사용
     * - 마지막 빈 줄(\r\n) 필수 (헤더와 본문 구분)
     *
     * 예상 작업 시간: 2-3시간
     *
     * @param response HttpResponse 객체
     * @return 헤더 문자열
     */
    public static String buildHeaders(HttpResponse response) {
        // TODO: 4번 팀원 구현
        throw new UnsupportedOperationException("4번 팀원이 구현 필요");
    }

    /**
     * [4번 팀원 작업]
     * 완전한 HTTP 응답을 OutputStream에 씁니다.
     *
     * 응답 형식:
     * [상태 라인]
     * [헤더들]
     * [빈 줄]
     * [본문]
     *
     * 구현 힌트:
     * 1. buildStatusLine() 호출
     *    String statusLine = buildStatusLine(response);
     *
     * 2. buildHeaders() 호출
     *    String headers = buildHeaders(response);
     *
     * 3. response.getBody() 가져오기
     *    String body = response.getBody();
     *
     * 4. 위 3개를 순서대로 OutputStream에 쓰기
     *    output.write(statusLine.getBytes(StandardCharsets.UTF_8));
     *    output.write(headers.getBytes(StandardCharsets.UTF_8));
     *    output.write(body.getBytes(StandardCharsets.UTF_8));
     *
     * 5. output.flush() 호출 (중요!)
     *    output.flush();
     *
     * 주의사항:
     * - 모두 UTF-8로 인코딩
     * - 순서: 상태 라인 → 헤더 → 본문
     * - flush() 필수 (버퍼에 있는 데이터를 실제로 전송)
     *
     * 예상 작업 시간: 1-2시간
     *
     * @param response HttpResponse 객체
     * @param output 클라이언트 소켓의 OutputStream
     * @throws IOException 쓰기 실패 시
     */
    public static void writeResponse(HttpResponse response, OutputStream output)
            throws IOException {
        // TODO: 4번 팀원 구현
        throw new UnsupportedOperationException("4번 팀원이 구현 필요");
    }
}
