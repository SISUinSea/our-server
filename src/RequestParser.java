import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

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
         BufferedReader reader = new BufferedReader(
             new InputStreamReader(input, StandardCharsets.UTF_8));

         String requestLine = reader.readLine();
         HttpRequest request = new HttpRequest();

         parseRequestLine(requestLine, request);
         parseHeaders(reader, request);

         return request;
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
        StringTokenizer st = new StringTokenizer(requestLine, " ");
        // parts.length == 3인지 검증
        if (st.countTokens() != 3) {
            throw new IllegalArgumentException("request line의 형식이 잘못되었어요!");
        }
        request.setMethod(st.nextToken());
        request.setUri(st.nextToken());
        request.setHttpVersion(st.nextToken());
        // method가 정의된 메소드인지 검증
        if (!isValidMethod(request.getMethod())) {
            throw new IllegalArgumentException(request.getMethod() +"는 정의되지 않은 method입니다.");
        }
        // httpVersion이 HTTP/1.1인지 확인
        if (!request.getHttpVersion().equals("HTTP/1.1")) {
            throw new IllegalArgumentException("지원하지 않는 HTTP 버전입니다. 지원 버전: HTTP/1.1");
        }
    }

    /** method name이 아래 중 하나와 같은지 확인합니다.
    *
     * @param method 요청받은 request method
     * @return boolean 아래의 method와 일치할 경우 true, 아닐 경우 false를 반환합니다.
    *                   "OPTIONS"                ; Section 9.2
                      | "GET"                    ; Section 9.3
                      | "HEAD"                   ; Section 9.4
                      | "POST"                   ; Section 9.5
                      | "PUT"                    ; Section 9.6
                      | "DELETE"                 ; Section 9.7
                      | "TRACE"                  ; Section 9.8
                      | "CONNECT"
    */
    private static boolean isValidMethod(String method) {
        Set<String> methods = new HashSet<>(Arrays.asList(
                "OPTIONS",
                "GET",
                "HEAD",
                "POST",
                "PUT",
                "DELETE",
                "TRACE",
                "CONNECT"
        ));
        return methods.contains(method);
    }

    // ============================================
    // 3번 팀원 담당 영역
    // ============================================

    public Map<String, String> parseHeaders(BufferedReader br) throws IOException {
        // “String 이름표”와 “String 내용물”이 있는 서랍장을 새로 만든다.
        Map<String, String> headers = new LinkedHashMap<>();

        String line; // 한 줄씩 읽을 공간 준비

        // 한 줄씩 읽기 시작
        int count = 0;  // ← 추가: 읽은 헤더 줄 개수

        // 한 줄씩 읽기 시작
        // br.readLine() 입력 스트림에서 한 줄 읽기
        while ((line = br.readLine()) != null) {
            //한 줄씩 읽다가 더 이상 없으면 끝
            if (line.isEmpty()) { // 빈 줄이면 헤더 끝
                break;
            }

            //즉, : (콜론) 기준으로 왼쪽이 이름, 오른쪽이 값
            //1️ 콜론 위치 찾기
            //2️ 앞뒤 공백 제거(trim)
            //3️ Map에 저장
            //Host: localhost:8080
            // (A) 추가: 헤더 줄 개수 제한 (예: 100줄)
            if (++count > 100) {
                throw new IOException("Too many header lines (limit 100)");
            }

            // (B) 추가: 콜론 없는 잘못된 줄은 무시
            int idx = line.indexOf(':');
            if (idx == -1) {
                continue;
            }  // 1️ 콜론(:) 위치 찾기
            //line.indexOf(':') 문자열에서 콜론(:)이 어디 있는지 위치(번호)를 찾음

            // 콜론이 있다면 (정상적인 헤더)

            String name = line.substring(0, idx).trim();        // 2️ 이름 부분
            // substring(0, idx) 그 위치 전까지의 문자열 (이름)

            String value = line.substring(idx + 1).trim();      // 3️ 값 부분
            // substring(idx + 1) 그 위치 다음부터 끝까지의 문자열 (값)
            // trim() 앞뒤 공백 제거

            String key = name.toLowerCase(Locale.ROOT);
            // HTTP 헤더는 대소문자 구분 안 함. (Host == host == HOST)
            // → 키를 소문자로 바꿔서 저장하면 해결됨.

            // 4️ Map에 추가
            // 이미 같은 이름의 헤더가 있으면 이어붙임
            if (headers.containsKey(key)) {
                headers.put(key, headers.get(key) + ", " + value);
            } else {
                headers.put(key, value);
            }
            // headers.put(name, value) Map에 이름(key)과 값(value)을 저장
            // 이렇게 저장된 헤더 안에는 아래와 같이 저장됨
            //"Host" → "localhost:8080"
            //"User-Agent" → "Mozilla/5.0"
        }

        return headers;
    }

    //  팀 뼈대(parseHeaders(BufferedReader, HttpRequest))와 바로 호환되도록 어댑터 제공
// - 팀원이 정적 메서드로 호출해도 작동하도록 static으로 제공
// - 네 기존 로직(Map 반환)을 그대로 재사용해서 HttpRequest에 채워 넣음
    public static void parseHeaders(BufferedReader br, HttpRequest req) throws IOException {
        RequestParser p = new RequestParser();        // 네 기존 인스턴스 메서드 재사용
        Map<String, String> m = p.parseHeaders(br);

        if (req.getHeaders() != null) {
            req.getHeaders().putAll(m);
        } else {
            req.setHeaders(new LinkedHashMap<>(m));
        }
    }

}
