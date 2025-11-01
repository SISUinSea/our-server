import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 요청 데이터를 저장하는 클래스
 *
 * Day 1에 전체 팀원이 함께 작성할 파일입니다.
 * 필드와 getter/setter만 정의합니다.
 */
public class HttpRequest {

    private String method;         // 예: "GET", "POST"
    private String uri;            // 예: "/index.html"
    private String httpVersion;    // 예: "HTTP/1.1"
    private Map<String, String> headers;  // 예: {"Host": "localhost:8080"}

    public HttpRequest() {
        this.headers = new HashMap<>();
    }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }

    public String getHttpVersion() { return httpVersion; }
    public void setHttpVersion(String httpVersion) { this.httpVersion = httpVersion; }

    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }

    /** 호환용: handleClient에서 path라고 부르므로 alias 제공 */
    public String getPath() { return getUri(); }

    /** 대소문자 무시 단일 헤더 조회 (If-None-Match/If-Modified-Since 등 캐치) */
    public String getHeader(String name) {
        if (name == null) return null;
        String v = headers.get(name);
        if (v != null) return v;
        for (Map.Entry<String, String> e : headers.entrySet()) {
            if (e.getKey() != null && e.getKey().equalsIgnoreCase(name)) {
                return e.getValue();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("HttpRequest{method='%s', uri='%s', version='%s', headers=%s}",
                method, uri, httpVersion, headers);
    }
}
