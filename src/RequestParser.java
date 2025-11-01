import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class RequestParser {

    public static HttpRequest parseRequest(InputStream input) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(input));

        // [지피티추가] 비어 있지 않은 첫 줄을 찾는 루프 (빈 줄/EOF 대비)
        String requestLine;
        while (true) {
            requestLine = br.readLine();
            if (requestLine == null) {
                // 클라이언트가 연결만 열고 요청을 안 보낸 경우 → null 반환
                return null; // [지피티추가: 예외 대신 조용히 종료하도록]
            }
            if (!requestLine.isBlank()) break; // 비어 있지 않으면 OK
            // 비어 있으면 다시 시도
        }

        HttpRequest req = parseRequestLine(requestLine);

        // 2) 헤더들
        Map<String, String> headers = new LinkedHashMap<>();
        while (true) {
            String line = br.readLine();
            if (line == null) break;            // 소켓 종료
            if (line.isEmpty()) break;          // 빈 줄 → 헤더 끝
            int colon = line.indexOf(':');
            if (colon <= 0) {
                // 잘못된 헤더 라인: 무시
                continue;
            }
            String name = line.substring(0, colon).trim();
            String value = line.substring(colon + 1).trim();
            // 중복 헤더는 콤마 병합
            if (headers.containsKey(name)) {
                headers.put(name, headers.get(name) + ", " + value);
            } else {
                headers.put(name, value);
            }
        }
        req.setHeaders(headers);

        // (Body는 이번 과제 범위 밖. 필요하면 Content-Length/Transfer-Encoding 기반으로 추가)
        return req;
    }

    static HttpRequest parseRequestLine(String line) throws IOException {
        if (line == null || line.isBlank()) {
            throw new IOException("Invalid request line");
        }
        // 일반적으로 "METHOD SP URI SP VERSION"
        StringTokenizer st = new StringTokenizer(line, " ");
        if (st.countTokens() < 3) {
            throw new IOException("Malformed request line: " + line);
        }
        String method  = st.nextToken();
        String uri     = st.nextToken();
        String version = st.nextToken();

        HttpRequest req = new HttpRequest();
        req.setMethod(method);
        req.setUri(uri);
        req.setHttpVersion(version);
        return req;
    }
}
