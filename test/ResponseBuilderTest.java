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

        // TODO: 4번 팀원 작성
        // 1. HttpResponse 객체 생성
        //    HttpResponse response = new HttpResponse();
        //
        // 2. response.setBody("<html>Test</html>") 설정
        //    String testBody = "<html>Test</html>";
        //    response.setBody(testBody);
        //
        // 3. buildHeaders(response) 호출
        //    try {
        //        String headers = ResponseBuilder.buildHeaders(response);
        //
        // 4. 결과에 "Content-Type"이 포함되어 있는지 확인
        //        if (headers.contains("Content-Type: text/html")) {
        //            System.out.println("✅ Content-Type 헤더 있음");
        //        } else {
        //            System.out.println("❌ Content-Type 헤더 없음");
        //        }
        //
        // 5. 결과에 "Content-Length"가 올바른지 확인
        //        int expectedLength = testBody.getBytes(StandardCharsets.UTF_8).length;
        //        String expectedHeader = "Content-Length: " + expectedLength;
        //        if (headers.contains(expectedHeader)) {
        //            System.out.println("✅ Content-Length 정확함: " + expectedLength);
        //        } else {
        //            System.out.println("❌ Content-Length 부정확");
        //            System.out.println("   예상: " + expectedLength);
        //        }
        //
        // 6. 결과에 "Connection: close"가 있는지 확인
        //        if (headers.contains("Connection: close")) {
        //            System.out.println("✅ Connection 헤더 있음");
        //        } else {
        //            System.out.println("❌ Connection 헤더 없음");
        //        }
        //
        // 7. 결과가 "\r\n\r\n"로 끝나는지 확인 (헤더 끝 빈 줄)
        //        if (headers.endsWith("\r\n\r\n")) {
        //            System.out.println("✅ 헤더가 빈 줄로 올바르게 끝남");
        //        } else {
        //            System.out.println("❌ 헤더가 빈 줄로 끝나지 않음");
        //        }
        //
        // 8. 전체 응답 쓰기 테스트
        //        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //        response.setStatusCode(200);
        //        ResponseBuilder.writeResponse(response, outputStream);
        //
        //        String fullResponse = outputStream.toString("UTF-8");
        //        if (fullResponse.contains("HTTP/1.1 200") &&
        //            fullResponse.contains("Content-Type") &&
        //            fullResponse.contains(testBody)) {
        //            System.out.println("✅ 전체 응답 쓰기 성공");
        //        } else {
        //            System.out.println("❌ 전체 응답 쓰기 실패");
        //        }
        //
        //    } catch (Exception e) {
        //        System.out.println("❌ 예외 발생: " + e.getMessage());
        //        e.printStackTrace();
        //    }

        System.out.println("TODO: 4번 팀원이 테스트 작성 필요\n");
    }
}
