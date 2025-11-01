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
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            String localIP = InetAddress.getLocalHost().getHostAddress();

            System.out.println("========================================");
            System.out.println("Simple HTTP Server");
            System.out.println("========================================");
            System.out.println("접속 가능 주소: http://" + localIP + ":" + PORT);
            System.out.println("로컬 접속: http://localhost:" + PORT);
            System.out.println("========================================");
            System.out.println("서버가 시작되었습니다. 연결을 기다립니다...\n");

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

    /** 클라이언트 요청을 처리 */
    private static void handleClient(Socket clientSocket) throws IOException {
        try (InputStream input = clientSocket.getInputStream();
             OutputStream output = clientSocket.getOutputStream()) {

            // 1) 요청 파싱
            HttpRequest request = RequestParser.parseRequest(input);
            System.out.println("요청 받음: " + request);

            // 2) 경로 추출 (심플 버전)
            String reqPath = request.getUri();
            if (reqPath == null || reqPath.isEmpty()) reqPath = "/";
            int q = reqPath.indexOf('?');
            if (q >= 0) reqPath = reqPath.substring(0, q);

            // 3) 이미지 라우팅은 전담 핸들러에 위임
            if (ImageHandler.handle(reqPath, output)) {
                System.out.println("응답 전송 완료 (ImageHandler): " + reqPath);
                return;
            }

            HttpResponse response = new HttpResponse();
            
            int statusCode = CreateStatus.returnStatus(request);
            response.setStatusCode(statusCode);
            response.setStatusMessage(ResponseBuilder.getStatusMessage(statusCode));

            if (statusCode == 200) {
                response.setBody(FileManager.returnFile());
            } else {
                response.setBody(buildErrorPage(statusCode, ResponseBuilder.getStatusMessage(statusCode)));
            }

            ResponseBuilder.writeResponse(response, output);
            System.out.println("응답 전송 완료\n");

        } finally {
            clientSocket.close();
        }
    }

    /** 에러 페이지 */
    private static String buildErrorPage(int statusCode, String statusMessage) {
        return String.format(
            "<html><head><title>%d %s</title></head>" +
            "<body><h1>%d %s</h1></body></html>",
            statusCode, statusMessage, statusCode, statusMessage
        );
    }

}
