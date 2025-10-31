import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 요청 데이터를 저장하는 클래스
 *
 * Day 1에 전체 팀원이 함께 작성할 파일입니다.
 * 필드와 getter/setter만 정의합니다.
 */
public class HttpRequest {

    // Day 1 전체 작업 - 필드 정의 (완성)
    private String method;         // 예: "GET", "POST"
    private String uri;            // 예: "/index.html"
    private String httpVersion;    // 예: "HTTP/1.1"
    private Map<String, String> headers;  // 예: {"Host": "localhost:8080"}
    private String body;

    public HttpRequest() {
        this.headers = new HashMap<>();
    }

    // Day 1 전체 작업 - getter/setter 생성 (완성)

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * 디버깅용 toString 메서드
     */
    @Override
    public String toString() {
        return String.format("HttpRequest{method='%s', uri='%s', version='%s', headers=%s}",
            method, uri, httpVersion, headers);
    }

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
