import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * ResponseBuilder 테스트 클래스
 *
 * 각 팀원이 자기가 구현한 메서드를 테스트합니다.
 */
public class ResponseBuilderTest {

    public static void main(String[] args) {
        System.out.println("=== ResponseBuilder 테스트 시작 ===\n");

        test1번팀원_응답라인생성();
        test4번팀원_응답헤더생성();

        System.out.println("\n=== 테스트 완료 ===");
    }

    /**
     * [1번 팀원 작성]
     * 응답 라인 생성 테스트
     */
    private static void test1번팀원_응답라인생성() {
        System.out.println("[1번 팀원] 응답 라인 생성 테스트");

        // TODO: 1번 팀원 작성
        // 1. HttpResponse 객체 생성
            HttpResponse response = new HttpResponse();
        //
        // 2. response.setStatusCode(200) 설정
            response.setStatusCode(200);
        //
        // 3. buildStatusLine(response) 호출
           try {
                String statusLine = ResponseBuilder.buildStatusLine(response);
        //
        // 4. 결과가 "HTTP/1.1 200 OK\r\n"인지 확인
                String expected = "HTTP/1.1 200 OK\r\n";
                if (expected.equals(statusLine)) {
                    System.out.println("✅ 200 상태 라인 생성 성공: " + statusLine.replace("\r\n", "\\r\\n"));
                } else {
                    System.out.println("❌ 200 상태 라인 생성 실패");
                    System.out.println("   예상: " + expected.replace("\r\n", "\\r\\n"));
                    System.out.println("   실제: " + statusLine.replace("\r\n", "\\r\\n"));
                }
        //
        // 5. 404 상태 코드 테스트
                response.setStatusCode(404);
                statusLine = ResponseBuilder.buildStatusLine(response);
                expected = "HTTP/1.1 404 Not Found\r\n";
                if (expected.equals(statusLine)) {
                    System.out.println("✅ 404 상태 라인 생성 성공: " + statusLine.replace("\r\n", "\\r\\n"));
                } else {
                    System.out.println("❌ 404 상태 라인 생성 실패");
                }
        //
        // 6. 500 상태 코드 테스트
                response.setStatusCode(500);
                statusLine = ResponseBuilder.buildStatusLine(response);
                expected = "HTTP/1.1 500 Internal Server Error\r\n";
                if (expected.equals(statusLine)) {
                    System.out.println("✅ 500 상태 라인 생성 성공: " + statusLine.replace("\r\n", "\\r\\n"));
                } else {
                    System.out.println("❌ 500 상태 라인 생성 실패");
                }
        //
        // 7. HTML 본문 생성 테스트
        //        String html = ResponseBuilder.buildSimpleHtmlBody("Hello, World!");
        //        if (html.contains("<h1>Hello, World!</h1>")) {
        //            System.out.println("✅ HTML 본문 생성 성공");
        //        } else {
        //            System.out.println("❌ HTML 본문 생성 실패");
        //        }
        //
            } catch (Exception e) {
                System.out.println("❌ 예외 발생: " + e.getMessage());
                e.printStackTrace();
            }
           
        System.out.println("TODO: 1번 팀원이 테스트 작성 필요\n");
    }

    /**
     * [4번 팀원 작성]
     * 응답 헤더 생성 테스트
     */
    private static void test4번팀원_응답헤더생성() {
        System.out.println("[4번 팀원] 응답 헤더 생성 테스트");

        HttpResponse response = new HttpResponse(); 
        String testBody = "가나다abc"; // UTF-8 = 12 bytes
        response.setBody(testBody);
        response.setStatusCode(200);

        try {
            // 헤더 생성
            String headers = ResponseBuilder.buildHeaders(response);

            // Content-Type
            if (headers.contains("Content-Type: text/html") &&
                headers.toLowerCase().contains("charset=utf-8")) {
                System.out.println("✅ Content-Type OK");
            } else {
                System.out.println("❌ Content-Type 누락/오류");
            }

            int expectedLength = testBody.getBytes(StandardCharsets.UTF_8).length; // 12
            if (headers.contains("Content-Length: " + expectedLength)) {
                System.out.println("✅ Content-Length OK: " + expectedLength);
            } else {
                System.out.println("❌ Content-Length 오류 (예상 " + expectedLength + ")");
            }

            // Connection: close
            if (headers.contains("Connection: close")) {
                System.out.println("✅ Connection OK");
            } else {
                System.out.println("❌ Connection 누락");
            }

            // 헤더 끝 CRLF CRLF
            if (headers.endsWith("\r\n\r\n")) {
                System.out.println("✅ 헤더 종료(\\r\\n\\r\\n) OK");
            } else {
                System.out.println("❌ 헤더 종료 형식 오류");
            }

            // 전체 응답 쓰기 (상태라인+헤더+본문)
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ResponseBuilder.writeResponse(response, out);
            String full = out.toString(StandardCharsets.ISO_8859_1);

            // 상태 라인
            if (full.startsWith("HTTP/1.1 200")) System.out.println("✅ 상태 라인 OK");
            else System.out.println("❌ 상태 라인 오류");

            // 본문 경계/내용/길이
            int idx = full.indexOf("\r\n\r\n");
            if (idx <= 0) {
                System.out.println("❌ 헤더/본문 경계 누락");
            } else {
                String bodyPart = full.substring(idx + 4);
                byte[] bodyBytes = bodyPart.getBytes(StandardCharsets.ISO_8859_1);
                if (bodyBytes.length == expectedLength) System.out.println("✅ 본문 길이 OK");
                else System.out.println("❌ 본문 길이 오류");
            }

        } catch (Exception e) {
            System.out.println("❌ 예외: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
    }


}
