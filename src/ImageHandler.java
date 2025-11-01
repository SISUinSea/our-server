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
        reqPath = stripQuery(reqPath);
        if (reqPath == null || reqPath.isEmpty()) reqPath = "/";

        // /images 또는 /images/ → 간단한 목록 페이지
        if ("/images".equals(reqPath) || "/images/".equals(reqPath)) {
            String html = "<!doctype html><meta charset='utf-8'>"
                    + "<h1>Images</h1>"
                    + "<ul>"
                    + "<li><a href='/images/cat.jpeg'>고양이</a></li>"
                    + "<li><a href='/images/dog.jpeg'>강아지</a></li>"
                    + "<li><a href='/images/logo.jpeg'>로고</a></li>"
                    + "</ul><p><a href='/'>← home</a></p>";

            HttpResponse idx = new HttpResponse();
            idx.setStatusCode(200);
            idx.setBody(html);
            ResponseBuilder.writeResponse(idx, output);
            return true;
        }

        // /images/<파일명> 아니면 처리 안 함
        if (!reqPath.startsWith("/images/")) return false;

        String fileName = reqPath.substring("/images/".length());

        if (!isSafeFileName(fileName)) {
            byte[] err = "<h1>400 Bad Request</h1>".getBytes(StandardCharsets.UTF_8);
            ResponseBuilder.writeBinaryResponse(400, "text/html; charset=UTF-8", err, output);
            return true;
        }

        String ct = contentTypeFor(fileName);
        if (ct == null) {
            byte[] err = "<h1>415 Unsupported Media Type</h1>".getBytes(StandardCharsets.UTF_8);
            ResponseBuilder.writeBinaryResponse(415, "text/html; charset=UTF-8", err, output);
            return true;
        }

        Path target = BASE.resolve(fileName).normalize();
        if (!target.startsWith(BASE)) {
            byte[] err = "<h1>403 Forbidden</h1>".getBytes(StandardCharsets.UTF_8);
            ResponseBuilder.writeBinaryResponse(403, "text/html; charset=UTF-8", err, output);
            return true;
        }

        if (!Files.exists(target) || !Files.isRegularFile(target)) {
            byte[] err = ("<h1>404 Not Found</h1><p>" + fileName + "</p>").getBytes(StandardCharsets.UTF_8);
            ResponseBuilder.writeBinaryResponse(404, "text/html; charset=UTF-8", err, output);
            return true;
        }

        byte[] data = Files.readAllBytes(target);
        ResponseBuilder.writeBinaryResponse(200, ct, data, output);
        return true;
    }

    // ---------- 유틸 ----------
    private static String stripQuery(String p) {
        if (p == null) return null;
        int i = p.indexOf('?');
        return (i >= 0) ? p.substring(0, i) : p;
    }

    private static String contentTypeFor(String name) {
        String lower = name.toLowerCase();
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".png"))  return "image/png";
        if (lower.endsWith(".gif"))  return "image/gif";
        if (lower.endsWith(".ico"))  return "image/x-icon";
        if (lower.endsWith(".webp")) return "image/webp";
        return null;
    }

    private static boolean isSafeFileName(String fileName) {
        return fileName != null && fileName.matches("^[A-Za-z0-9._-]+$");
        // 필요시 '..' 차단 등의 강화도 가능
    }
}
