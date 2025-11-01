import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.io.*;

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

    private static void handleClient(Socket clientSocket) throws IOException {
        try (InputStream input = clientSocket.getInputStream();
             OutputStream output = clientSocket.getOutputStream()) {

            HttpRequest request = RequestParser.parseRequest(input);
            System.out.println("요청 받음: " + request);

            // ========================================================
            // 🚨 NulL 체크 로직 추가 (오류 해결 지점)
            // 요청을 읽는데 실패하여 request가 null이면, 조용히 연결을 닫는다.
            // ========================================================
            if (request == null) {
                System.out.println("주의: 클라이언트가 요청을 보내지 않고 연결을 닫았습니다. (request is null)");
                return; // 함수 종료 후 finally 블록으로 이동하여 소켓 닫음
            }

            HttpResponse response = new HttpResponse();
            // 이제 request는 null이 아니므로 안전하게 호출 가능
            if (response.getRequestMethod() == null && request.getMethod() != null) {
                response.setRequestMethod(request.getMethod());
            }

            String path = request.getPath();
            int statusCode;

            // =========================
            // 인덱스 페이지
            // =========================
            if (path == null || "/".equals(path)) {
                response.setHeader("Content-Type", "text/html; charset=utf-8");
                String indexHtml =
                        "<!doctype html><html><head><meta charset='utf-8'><title>파일 목록</title></head><body>" +
                                "<h1>파일 목록</h1>" +
                                "<ul>" +
                                "<li><a href=\"/files/hello.txt\">hello.txt</a></li>" +
                                "<li><a href=\"/files/info.txt\">info.txt</a></li>" +
                                "</ul>" +
                                "<p style='color:#666'>* 첫 요청은 다운로드, 파일이 수정되지 않았다면 다음엔 304 페이지가 표시됩니다.</p>" +
                                "</body></html>";
                response.setBody(indexHtml);
                statusCode = 200;

                // =========================
                // 파일 다운로드 + 304 처리
                // =========================
            } else if (path.startsWith("/files/")) {
                String safePath = path.startsWith("/") ? path.substring(1) : path;
                File file = new File("wwwroot", safePath);

                if (!file.exists() || !file.isFile()) {
                    statusCode = 404;
                    response.setHeader("Content-Type", "text/html; charset=utf-8");
                    response.setBody(buildErrorPage(404, "Not Found"));
                } else {
                    // 캐시 관련 헤더 부착
                    CacheUtil.applyCachingHeaders(file, response);

                    // 304 조건 충족?
                    if (CacheUtil.shouldReturn304(request, file)) {
                        // ✅ 여기서 실제 304 대신 HTML 페이지를 보여주자
                        statusCode = 200; // HTML 페이지니까 200으로 보냄
                        response.setHeader("Content-Type", "text/html; charset=utf-8");
                        response.setBody(
                                "<!doctype html><html><head><meta charset='utf-8'><title>304 Not Modified</title></head>" +
                                        "<body style='font-family:system-ui,sans-serif; background:#0b0b0b; color:#00ff00; text-align:center; padding:60px'>" +
                                        "<h1>304 Not Modified</h1>" +
                                        "<p>이 파일은 변경되지 않았습니다.<br>서버의 캐시 데이터를 사용하세요.</p>" +
                                        "<p><a href='/'>홈으로 돌아가기</a></p>" +
                                        "</body></html>"
                        );
                    } else {
                        // 파일 변경됨 → 다운로드 진행
                        statusCode = 200;
                        response.setHeader("Content-Type", ContentType.of(path));

                        String filename = new java.io.File(path).getName();
                        // 다운로드 강제
                        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

                        if (path.toLowerCase().endsWith(".txt")) {
                            try {
                                response.setBody(FileText.readUtf8(file));
                            } catch (Exception e) {
                                statusCode = 500;
                                response.setHeader("Content-Type", "text/html; charset=utf-8");
                                response.setBody(buildErrorPage(500, "Internal Server Error"));
                            }
                        } else {
                            response.setHeader("Content-Type", "text/html; charset=utf-8");
                            response.setBody("<html><body><h1>지금은 txt만 테스트 중입니다.</h1></body></html>");
                        }
                    }
                }

                // =========================
                // 나머지 → 404
                // =========================
            } else {
                statusCode = 404;
                response.setHeader("Content-Type", "text/html; charset=utf-8");
                response.setBody(buildErrorPage(404, "Not Found"));
            }

            response.setStatusCode(statusCode);
            response.setStatusMessage(ResponseBuilder.getStatusMessage(statusCode));
            ResponseBuilder.writeResponse(response, output);
            System.out.println("응답 전송 완료 (" + statusCode + ")\n");

        } finally {
            clientSocket.close();
        }
    }

    private static String buildErrorPage(int statusCode, String statusMessage) {
        return String.format(
                "<html><head><title>%d %s</title></head>" +
                        "<body><h1>%d %s</h1></body></html>",
                statusCode, statusMessage, statusCode, statusMessage
        );
    }
}
