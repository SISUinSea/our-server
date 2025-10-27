import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.io.*;

/**
 * Simple HTTP Server
 *
 * Day 1에 전체 팀원이 함께 작성할 파일입니다.
 * 기본 구조만 제공하고, handleClient 메서드는 통합 시 완성합니다.
 */
public class HttpServer {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        // Day 1 전체 작업 (완성)
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // 로컬 IP 주소 가져오기
            String localIP = InetAddress.getLocalHost().getHostAddress();

            // 서버 시작 메시지 출력
            System.out.println("========================================");
            System.out.println("Simple HTTP Server");
            System.out.println("========================================");
            System.out.println("접속 가능 주소: http://" + localIP + ":" + PORT);
            System.out.println("로컬 접속: http://localhost:" + PORT);
            System.out.println("========================================");
            System.out.println("서버가 시작되었습니다. 연결을 기다립니다...\n");

            // 무한 루프로 클라이언트 연결 대기
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("클라이언트 연결됨: " + clientSocket.getInetAddress());
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("서버 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 클라이언트 요청을 처리합니다.
     *
     * Day 3 통합 시 완성할 메서드입니다.
     * 각 팀원이 만든 파싱/생성 메서드를 여기서 조합합니다.
     *
     * @param clientSocket 클라이언트 소켓
     * @throws IOException 네트워크 오류 시
     */
    private static void handleClient(Socket clientSocket) throws IOException {
        // TODO: Day 3 통합 작업
        // 1. InputStream, OutputStream 얻기
        // 2. RequestParser로 요청 파싱 → HttpRequest 객체
        // 3. 요청 내용 로그 출력
        // 4. ResponseBuilder로 응답 생성 → HttpResponse 객체
        // 5. 응답 전송
        // 6. 소켓 닫기

        // 구현 힌트:
        // try (InputStream input = clientSocket.getInputStream();
        //      OutputStream output = clientSocket.getOutputStream()) {
        //
        //     // 요청 파싱
        //     HttpRequest request = RequestParser.parseRequest(input);
        //     System.out.println("요청 받음: " + request);
        //
        //     // 응답 생성
        //     HttpResponse response = new HttpResponse();
        //     response.setStatusCode(200);
        //     response.setStatusMessage("OK");
        //
        //     String htmlBody = ResponseBuilder.buildSimpleHtmlBody("Hello, World!");
        //     response.setBody(htmlBody);
        //
        //     // 응답 전송
        //     ResponseBuilder.writeResponse(response, output);
        //     System.out.println("응답 전송 완료\n");
        //
        // } finally {
        //     clientSocket.close();
        // }

        System.out.println("TODO: 요청 처리 로직 구현 필요");
    }
}
