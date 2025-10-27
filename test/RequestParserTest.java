import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * RequestParser 테스트 클래스
 *
 * 각 팀원이 자기가 구현한 메서드를 테스트합니다.
 */
public class RequestParserTest {

    public static void main(String[] args) {
        System.out.println("=== RequestParser 테스트 시작 ===\n");

        test2번팀원_요청라인파싱();
        test3번팀원_요청헤더파싱();

        System.out.println("\n=== 테스트 완료 ===");
    }

    /**
     * [2번 팀원 작성]
     * 요청 라인 파싱 테스트
     */
    private static void test2번팀원_요청라인파싱() {
        System.out.println("[2번 팀원] 요청 라인 파싱 테스트");

        // TODO: 2번 팀원 작성
        // 1. HttpRequest 객체 생성
        //    HttpRequest request = new HttpRequest();
        //
        // 2. parseRequestLine("GET /index.html HTTP/1.1", request) 호출
        //    try {
        //        RequestParser.parseRequestLine("GET /index.html HTTP/1.1", request);
        //
        // 3. request.getMethod()가 "GET"인지 확인
        //        if ("GET".equals(request.getMethod())) {
        //            System.out.println("✅ Method 파싱 성공: " + request.getMethod());
        //        } else {
        //            System.out.println("❌ Method 파싱 실패: " + request.getMethod());
        //        }
        //
        // 4. request.getUri()가 "/index.html"인지 확인
        //        if ("/index.html".equals(request.getUri())) {
        //            System.out.println("✅ URI 파싱 성공: " + request.getUri());
        //        } else {
        //            System.out.println("❌ URI 파싱 실패: " + request.getUri());
        //        }
        //
        // 5. request.getHttpVersion()이 "HTTP/1.1"인지 확인
        //        if ("HTTP/1.1".equals(request.getHttpVersion())) {
        //            System.out.println("✅ HTTP Version 파싱 성공: " + request.getHttpVersion());
        //        } else {
        //            System.out.println("❌ HTTP Version 파싱 실패: " + request.getHttpVersion());
        //        }
        //
        //    } catch (Exception e) {
        //        System.out.println("❌ 예외 발생: " + e.getMessage());
        //        e.printStackTrace();
        //    }
        //
        // 6. 잘못된 형식 테스트
        //    try {
        //        HttpRequest badRequest = new HttpRequest();
        //        RequestParser.parseRequestLine("INVALID", badRequest);
        //        System.out.println("❌ 예외가 발생해야 하는데 발생하지 않음");
        //    } catch (IllegalArgumentException e) {
        //        System.out.println("✅ 잘못된 형식 예외 처리 성공");
        //    }

        System.out.println("TODO: 2번 팀원이 테스트 작성 필요\n");
    }

    /**
     * [3번 팀원 작성]
     * 요청 헤더 파싱 테스트
     */
    private static void test3번팀원_요청헤더파싱() {
        System.out.println("[3번 팀원] 요청 헤더 파싱 테스트");

        // TODO: 3번 팀원 작성
        // 1. 테스트용 헤더 문자열 생성
        //    String testHeaders =
        //        "Host: localhost:8080\r\n" +
        //        "User-Agent: TestClient\r\n" +
        //        "Accept: text/html\r\n" +
        //        "\r\n";  // 빈 줄 (헤더 끝)
        //
        // 2. ByteArrayInputStream으로 변환
        //    ByteArrayInputStream inputStream =
        //        new ByteArrayInputStream(testHeaders.getBytes(StandardCharsets.UTF_8));
        //
        // 3. BufferedReader 생성
        //    BufferedReader reader = new BufferedReader(
        //        new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        //
        // 4. HttpRequest 객체 생성
        //    HttpRequest request = new HttpRequest();
        //
        // 5. parseHeaders() 호출
        //    try {
        //        RequestParser.parseHeaders(reader, request);
        //
        // 6. headers에 "Host" 키가 있는지 확인
        //        if (request.getHeaders().containsKey("Host")) {
        //            System.out.println("✅ Host 헤더 있음: " + request.getHeaders().get("Host"));
        //        } else {
        //            System.out.println("❌ Host 헤더 없음");
        //        }
        //
        // 7. headers에 "User-Agent" 키가 있는지 확인
        //        if (request.getHeaders().containsKey("User-Agent")) {
        //            System.out.println("✅ User-Agent 헤더 있음: " + request.getHeaders().get("User-Agent"));
        //        } else {
        //            System.out.println("❌ User-Agent 헤더 없음");
        //        }
        //
        // 8. 값이 올바른지 확인
        //        if ("localhost:8080".equals(request.getHeaders().get("Host"))) {
        //            System.out.println("✅ Host 값 정확함");
        //        } else {
        //            System.out.println("❌ Host 값 부정확: " + request.getHeaders().get("Host"));
        //        }
        //
        //    } catch (Exception e) {
        //        System.out.println("❌ 예외 발생: " + e.getMessage());
        //        e.printStackTrace();
        //    }

        System.out.println("TODO: 3번 팀원이 테스트 작성 필요\n");
    }
}
