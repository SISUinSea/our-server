import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ResponseBuilder {

    // ===== 상태 메시지 / 상태라인 =====
    public static String getStatusMessage(int statusCode) {
        switch (statusCode) {
            case 100: return "Continue";
            case 101: return "Switching Protocols";
            case 200: return "OK";
            case 201: return "Created";
            case 203: return "Non-Authoritative Information";
            case 204: return "No Content";
            case 205: return "Reset Content";
            case 206: return "Partial Content";
            case 300: return "Multiple Choices";
            case 301: return "Moved Permanently";
            case 302: return "Found";
            case 303: return "See Other";
            case 304: return "Not Modified";
            case 307: return "Temporary Redirect";
            case 400: return "Bad Request";
            case 401: return "Unauthorized";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 405: return "Method Not Allowed";
            case 406: return "Not Acceptable";
            case 407: return "Proxy Authentication Required";
            case 408: return "Request Timeout";
            case 409: return "Conflict";
            case 410: return "Gone";
            case 411: return "Length Required";
            case 412: return "Precondition Failed";
            case 413: return "Request Entity Too Large";
            case 414: return "Request-URI Too Long";
            case 415: return "Unsupported Media Type";
            case 416: return "Requested Range Not Satisfiable";
            case 417: return "Expectation Failed";
            case 500: return "Internal Server Error";
            case 501: return "Not Implemented";
            case 502: return "Bad Gateway";
            case 503: return "Service Unavailable";
            case 504: return "Gateway Timeout";
            case 505: return "HTTP Version Not Supported";
            default:  return "Unknown";
        }
    }

    public static String buildStatusLine(HttpResponse response) {
        return String.format("HTTP/1.1 %d %s\r\n",
                response.getStatusCode(),
                getStatusMessage(response.getStatusCode()));
    }

    public static String buildSimpleHtmlBody(String message) {
        if (message == null) message = "";
        return "<!doctype html><html><head><meta charset=\"utf-8\"><title>Simple HTTP Server</title></head>"
                + "<body><h1>" + message + "</h1></body></html>";
    }

    // ===== 헤더/바디 출력 =====
    public static String buildHeaders(HttpResponse response) {
        int code = response.getStatusCode();
        String method = response.getRequestMethod();
        boolean noBody = code == 304 || (method != null && method.equalsIgnoreCase("HEAD"));

        String body = response.getBody();
        byte[] bodyBytes = (body == null) ? new byte[0] : body.getBytes(StandardCharsets.UTF_8);

        // 기본 헤더들(없으면 채워넣기)
        if (response.getHeader("Date") == null) {
            response.setHeader("Date",
                    java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC)
                            .format(java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME));
        }
        if (response.getHeader("Server") == null) {
            response.setHeader("Server", "SimpleHTTP/1.0");
        }
        if (response.getHeader("Content-Type") == null) {
            response.setHeader("Content-Type", "text/html; charset=UTF-8");
        }
        if (response.getHeader("Connection") == null) {
            response.setHeader("Connection", "close");
        }

        // 본문 전송 여부에 따라 Content-Length 설정
        if (noBody) {
            response.setHeader("Content-Length", "0");
        } else {
            response.setHeader("Content-Length", String.valueOf(bodyBytes.length));
        }

        // 응답 객체에 들어있는 모든 헤더 그대로 출력
        StringBuilder headers = new StringBuilder(256);
        for (Map.Entry<String, String> h : response.getHeaders().entrySet()) {
            headers.append(h.getKey()).append(": ").append(h.getValue()).append("\r\n");
        }
        headers.append("\r\n"); // 헤더 끝
        return headers.toString();
    }

    public static void writeResponse(HttpResponse response, OutputStream output)
            throws IOException {
        String statusLine = buildStatusLine(response);

        int code = response.getStatusCode();
        String method = response.getRequestMethod();
        boolean noBody = code == 304 || (method != null && method.equalsIgnoreCase("HEAD"));

        String headers = buildHeaders(response);

        String bodyStr = response.getBody();
        byte[] body = (bodyStr == null) ? new byte[0] : bodyStr.getBytes(StandardCharsets.UTF_8);

        output.write(statusLine.getBytes(StandardCharsets.UTF_8));
        output.write(headers.getBytes(StandardCharsets.UTF_8));
        if (!noBody && body.length > 0) {
            output.write(body);
        }
        output.flush();
    }
}
