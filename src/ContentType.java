//새 파일 추가: ContentType.java (확장자 → Content-Type)
public final class ContentType {
    private ContentType() {}
    public static String of(String path) {
        String p = path == null ? "" : path.toLowerCase();
        if (p.endsWith(".html") || p.endsWith(".htm")) return "text/html; charset=utf-8";
        if (p.endsWith(".txt"))  return "text/plain; charset=utf-8";
        if (p.endsWith(".css"))  return "text/css; charset=utf-8";
        if (p.endsWith(".js"))   return "application/javascript; charset=utf-8";
        if (p.endsWith(".json")) return "application/json; charset=utf-8";
        if (p.endsWith(".pdf"))  return "application/pdf";
        if (p.endsWith(".hwp"))  return "application/x-hwp";
        if (p.endsWith(".docx")) return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        if (p.endsWith(".png"))  return "image/png";
        if (p.endsWith(".jpg") || p.endsWith(".jpeg")) return "image/jpeg";
        return "application/octet-stream";
    }
}
