// ImageHandler.java  (패키지 안 쓰면 package 선언 없음)
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageHandler {
    private static final Path BASE = Paths.get("www", "images");

    // ✅ HttpServer에서 부르는 정확한 시그니처
    public static boolean handle(String reqPath, OutputStream output) throws IOException {
        reqPath = stripQuery(reqPath); //stripQuery =>뒤에붙는 쿼리 제거
        if (reqPath == null || reqPath.isEmpty()) reqPath = "/";

        // /images/<파일명> 아니면 처리 안 함
        if (!reqPath.startsWith("/images/")) return false;
        
        // images/뒤에오는 파일명 추출
        String fileName = reqPath.substring("/images/".length());
        
        //파일명 안전성 검사: 영문/숫자/./_/-만 허용. (슬래시 같은 건 금지)
        //이상한 이름(경로 탈출 시도 등)이면 400 Bad Request로 짧은 HTML을 보내고 끝.
        if (!isSafeFileName(fileName)) {
            byte[] err = "<h1>400 Bad Request</h1>".getBytes(StandardCharsets.UTF_8);
            ResponseBuilder.writeBinaryResponse(400, "text/html; charset=UTF-8", err, output);
            return true;
        }
        
        //확장자 → MIME 타입을 결정(jpg/png/gif/ico/webp만 허용).
        String ct = contentTypeFor(fileName);
        //지원 안 하는 확장자면 415 Unsupported Media Type.
        if (ct == null) {
            byte[] err = "<h1>415 Unsupported Media Type</h1>".getBytes(StandardCharsets.UTF_8);
            ResponseBuilder.writeBinaryResponse(415, "text/html; charset=UTF-8", err, output);
            return true;
        }
        
        //실제 디스크 경로 만들기: www/images 밑에 fileName을 붙인 뒤 normalize()로 경로 정리.
        Path target = BASE.resolve(fileName).normalize();
        //보안 포인트: 결과 경로가 BASE(=www/images)로 시작하지 않으면 폴더 밖으로 빠져나가려는 공격 → 403 Forbidden.
        if (!target.startsWith(BASE)) {
            byte[] err = "<h1>403 Forbidden</h1>".getBytes(StandardCharsets.UTF_8);
            ResponseBuilder.writeBinaryResponse(403, "text/html; charset=UTF-8", err, output);
            return true;
        }
       
        //파일이 없거나 일반 파일이 아니면 404 Not Found.
        if (!Files.exists(target) || !Files.isRegularFile(target)) {
            byte[] err = ("<h1>404 Not Found</h1><p>" + fileName + "</p>").getBytes(StandardCharsets.UTF_8);
            ResponseBuilder.writeBinaryResponse(404, "text/html; charset=UTF-8", err, output);
            return true;
        }
        
        //실제 파일을 바이트 배열로 읽음
        byte[] data = Files.readAllBytes(target);
        //ResponseBuilder.writeBinaryResponse로 정상 응답(200) 전송.
        ResponseBuilder.writeBinaryResponse(200, ct, data, output);
        return true;
    }

    // ---------- 유틸 ----------
    //경로에서 ? 뒤의 쿼리 부분을 잘라냄.
    private static String stripQuery(String p) {
        if (p == null) return null;
        int i = p.indexOf('?');
        return (i >= 0) ? p.substring(0, i) : p;
    }
    
    //파일 확장자로 적절한 Content-Type(MIME 타입) 문자열을 돌려줌
    private static String contentTypeFor(String name) {
        String lower = name.toLowerCase();
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".png"))  return "image/png";
        if (lower.endsWith(".gif"))  return "image/gif";
        if (lower.endsWith(".ico"))  return "image/x-icon";
        if (lower.endsWith(".webp")) return "image/webp";
        //목록에 없으면 null → 위에서 415 처리.
        return null;
    }
    
    //파일명에 허용된 문자만 있는지 정규식으로 검사. 슬래시(/), 공백, 역슬래시(\) 등은 불허 → 디렉터리 탈출/이상한 경로 방지.
    private static boolean isSafeFileName(String fileName) {
        return fileName != null && fileName.matches("^[A-Za-z0-9._-]+$");
        // 필요시 '..' 차단 등의 강화도 가능
    }
}
