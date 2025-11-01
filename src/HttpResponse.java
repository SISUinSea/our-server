import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 응답 데이터를 저장하는 클래스
 *
 * Day 1에 전체 팀원이 함께 작성할 파일입니다.
 * 필드와 getter/setter만 정의합니다.
 */
public class HttpResponse {

    // Day 1 전체 작업 - 필드 정의 (완성)
    private int statusCode;                    // 예: 200, 404, 500
    private String statusMessage;              // 예: "OK", "Not Found"
    private Map<String, String> headers;       // 예: {"Content-Type": "text/html"}
    private String body;                       // 응답 본문 (HTML 등)

    // ✅ 추가: 요청 메서드 저장용 (HEAD/304 처리에 필요)
    private String requestMethod;

    public HttpResponse() {
        this.headers = new HashMap<>();
    }

    // Day 1 전체 작업 - getter/setter 생성 (완성)

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    // ✅ 추가: 개별 헤더 조작용 메서드
    public void setHeader(String name, String value) {
        this.headers.put(name, value);
    }

    public String getHeader(String name) {
        return this.headers.get(name);
    }

    // ✅ 추가: 요청 메서드 getter/setter
    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String method) {
        this.requestMethod = method;
    }

    /**
     * 디버깅용 toString 메서드
     */
    @Override
    public String toString() {
        return String.format("HttpResponse{statusCode=%d, message='%s', headers=%s, bodyLength=%d}",
                statusCode, statusMessage, headers, body != null ? body.length() : 0);
    }
}
