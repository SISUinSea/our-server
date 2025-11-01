import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        try (InputStream input = clientSocket.getInputStream();
             OutputStream output = clientSocket.getOutputStream()) {

            // 요청 파싱
            HttpRequest request = RequestParser.parseRequest(input);
            System.out.println("요청 받음: " + request);
         // 홈('/') 요청이면 서버가 직접 HTML 생성해서 반환 (숏서킷)
            String reqPath = safeExtractPath(request); // 이미 추가해둔 보조함수

         // ==== 홈("/") 또는 "/index.html" → www/index.html 파일을 읽어 내려보내기 ====
            if ("/".equals(reqPath) || "/index.html".equals(reqPath)) {
                java.nio.file.Path indexPath = java.nio.file.Paths.get("www", "index.html");
                if (java.nio.file.Files.exists(indexPath) && java.nio.file.Files.isRegularFile(indexPath)) {
                    String html = java.nio.file.Files.readString(indexPath, java.nio.charset.StandardCharsets.UTF_8);

                    HttpResponse home = new HttpResponse();
                    home.setStatusCode(200);
                    home.setBody(html); // ResponseBuilder.buildHeaders()가 text/html; charset=UTF-8로 내려줌
                    ResponseBuilder.writeResponse(home, output);
                    System.out.println("응답 전송 완료 (www/index.html)\n");
                    return; // 숏서킷
                } else {
                    // index.html이 없으면 404
                    byte[] err = "<h1>404 Not Found</h1><p>index.html 없음</p>".getBytes(java.nio.charset.StandardCharsets.UTF_8);
                    ResponseBuilder.writeBinaryResponse(404, "text/html; charset=UTF-8", err, output);
                    System.out.println("응답 전송 완료 (404 index.html not found)\n");
                    return;
                }
            }



         // ======= [이미지 라우팅: /images , /images/<파일명>] =======
            reqPath = safeExtractPath(request); // getPath()가 없어도 안전 추출

            // 1) /images 또는 /images/ → 인덱스(목록) 페이지 (HTML)
            if ("/images".equals(reqPath) || "/images/".equals(reqPath)) {
                HttpResponse idx = new HttpResponse();
                idx.setStatusCode(200);
                idx.setBody("<!doctype html><meta charset='utf-8'>"
                	    + "<h1>Images</h1>"
                	    + "<ul>"
                	    + "<li><a href='/images/cat.jpeg'>cat.jpeg</a></li>"
                	    + "<li><a href='/images/dog.jpeg'>dog.jpeg</a></li>"
                	    + "<li><a href='/images/logo.jpeg'>logo.jpeg</a></li>"
                	    + "</ul><p><a href='/'>← home</a></p>");
                ResponseBuilder.writeResponse(idx, output);
                System.out.println("응답 전송 완료 (images index)\n");
                return; // 이미지 응답 후, 기존 흐름으로 내려가지 않도록 즉시 종료
            }

            // 2) /images/<파일명> → 실제 이미지 바이너리 전송
            if (reqPath != null && reqPath.startsWith("/images/")) {
                String fileName = reqPath.substring("/images/".length());

                // 파일명/확장자 검증
                if (!isSafeFileName(fileName)) {
                    byte[] err = "<h1>400 Bad Request</h1>".getBytes(StandardCharsets.UTF_8);
                    ResponseBuilder.writeBinaryResponse(400, "text/html; charset=UTF-8", err, output);
                    System.out.println("응답 전송 완료 (400)\n");
                    return;
                }
                String ct = contentTypeFor(fileName);
                if (ct == null) {
                    byte[] err = "<h1>415 Unsupported Media Type</h1>".getBytes(StandardCharsets.UTF_8);
                    ResponseBuilder.writeBinaryResponse(415, "text/html; charset=UTF-8", err, output);
                    System.out.println("응답 전송 완료 (415)\n");
                    return;
                }

                // 디스크 경로: 프로젝트루트/static/images/<파일명>
                Path base   = Paths.get("www", "images");
                Path target = base.resolve(fileName).normalize();

                // 디렉터리 탈출 방지
                if (!target.startsWith(base)) {
                    byte[] err = "<h1>403 Forbidden</h1>".getBytes(StandardCharsets.UTF_8);
                    ResponseBuilder.writeBinaryResponse(403, "text/html; charset=UTF-8", err, output);
                    System.out.println("응답 전송 완료 (403)\n");
                    return;
                }

                // 파일 읽기
                if (!Files.exists(target) || !Files.isRegularFile(target)) {
                    byte[] err = ("<h1>404 Not Found</h1><p>" + fileName + "</p>").getBytes(StandardCharsets.UTF_8);
                    ResponseBuilder.writeBinaryResponse(404, "text/html; charset=UTF-8", err, output);
                    System.out.println("응답 전송 완료 (404)\n");
                    return;
                }

                byte[] data = Files.readAllBytes(target);
                ResponseBuilder.writeBinaryResponse(200, ct, data, output);
                System.out.println("응답 전송 완료 (image: " + fileName + ")\n");
                return;
            }
            // ======= [/이미지 라우팅] =======

            // 응답 생성
            HttpResponse response = new HttpResponse();

            // 요청에 따라 응답 상태 코드 생성
            int statusCode = CreateStatus.returnStatus(request);
            response.setStatusCode(statusCode);
            response.setStatusMessage(ResponseBuilder.getStatusMessage(statusCode));

            // 파일 찾아서 body에 저장 (200 OK일 때만)
            if (statusCode == 200) {
                response.setBody(FileManager.returnFile());
            } else {
                // 에러 페이지 생성
                response.setBody(buildErrorPage(statusCode, ResponseBuilder.getStatusMessage(statusCode)));
            }

            // 응답 전송
            ResponseBuilder.writeResponse(response, output);
            System.out.println("응답 전송 완료\n");

        } finally {
            clientSocket.close();
        }
    }

    /**
     * 에러 상태 코드에 대한 간단한 HTML 페이지를 생성합니다.
     */
    private static String buildErrorPage(int statusCode, String statusMessage) {
        return String.format(
            "<html><head><title>%d %s</title></head>" +
            "<body><h1>%d %s</h1></body></html>",
            statusCode, statusMessage, statusCode, statusMessage
        );
    }
 // --- [유틸 1] 확장자 → Content-Type (화이트리스트) ---
    private static String contentTypeFor(String name) {
        String lower = name.toLowerCase();
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".png"))  return "image/png";
        if (lower.endsWith(".gif"))  return "image/gif";
        if (lower.endsWith(".ico"))  return "image/x-icon";
        if (lower.endsWith(".webp")) return "image/webp";
        return null; // 허용 안 함
    }

    // --- [유틸 2] 파일명 안전성 체크 (영문/숫자/._- 만 허용) ---
    private static boolean isSafeFileName(String fileName) {
        return fileName != null && fileName.matches("^[A-Za-z0-9._-]+$");
    }

    // --- [유틸 3] HttpRequest에서 경로 안전 추출 (getPath() 없어도 동작) ---
    private static String safeExtractPath(HttpRequest req) {
        // 1) 흔한 게터 이름들을 반사로 시도
        String[] methods = {"getPath", "getRequestTarget", "getUri", "getTarget"};
        for (String m : methods) {
            try {
                java.lang.reflect.Method md = req.getClass().getMethod(m);
                Object v = md.invoke(req);
                if (v instanceof String s && !s.isEmpty()) {
                    int q = s.indexOf('?');
                    return (q >= 0) ? s.substring(0, q) : s;
                }
            } catch (Exception ignore) {}
        }
        // 2) 마지막 폴백: toString()에서 "GET /... HTTP/1.1" 형태 대충 파싱
        String s = String.valueOf(req);
        try {
            int sp1 = s.indexOf(' ');
            int sp2 = s.indexOf(' ', sp1 + 1);
            if (sp1 > 0 && sp2 > sp1) {
                String target = s.substring(sp1 + 1, sp2);
                int q = target.indexOf('?');
                return (q >= 0) ? target.substring(0, q) : target;
            }
        } catch (Exception ignore) {}
        return "/";
    }

}
