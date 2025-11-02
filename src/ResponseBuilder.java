import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * HTTP 응답을 생성하는 클래스
 *
 * 1번 팀원: 응답 라인 생성 (buildStatusLine, getStatusMessage, buildSimpleHtmlBody)
 * 4번 팀원: 응답 헤더 생성 (buildHeaders, writeResponse)
 */
public class ResponseBuilder {
    /**
     * [1번 팀원 작업]
     * 상태 코드에 해당하는 상태 메시지를 반환합니다.
     *
     * RFC 2616 Section 6.1.1 참고:
     * Status-Code = 3DIGIT
     * Reason-Phrase = *<TEXT>
     *
     * 주요 상태 코드:
     * - 200: OK
     * - 201: Created
     * - 400: Bad Request
     * - 404: Not Found
     * - 500: Internal Server Error
     *
     * 구현 힌트:
     * switch (statusCode) {
     *     case 200:
     *         return "OK";
     *     case 404:
     *         return "Not Found";
     *     case 500:
     *         return "Internal Server Error";
     *     default:
     *         return "Unknown";
     * }
     *
     * 예상 작업 시간: 1시간
     *
     * @param statusCode HTTP 상태 코드
     * @return 상태 메시지
     */
    public static String getStatusMessage(int statusCode) {
        // TODO: 1번 팀원 구현
    	switch (statusCode) {
    		case 100://should
    			return "Continue";
    		case 101://should
    			return "Switching Protocols";
    		case 200://must
                return "OK";
    		case 201://should
    			return "Created";//주요 상태 코드
    		case 203://should
    			return "Non-Authoritative Information";
    		case 204:
    			return "No Content";
    		case 205:
    			return "Reset Content";
    		case 206:
    			return "Partial Content";
    		case 300:
    			return "Multiple Choices";
    		case 301:
    			return "Moved Permanently";
    		case 302:
    			return "Found";
    		case 303:
    			return "See Other";
    		case 304:
    			return "Not Modified";//주요 상태 코드
    		case 307:
    			return "Temporary Redirect";
    		case 400:
                return "Bad Request";//주요 상태 코드
    		case 401://should
                return "Unauthorized";
    		case 403:
                return "Forbidden";
    		case 404://must
                return "Not Found";
    		case 405:
                return "Method Not Allowed";
    		case 406:
                return "Not Acceptable";
    		case 407:
                return "Proxy Authentication Required";
    		case 408:
                return "Request Timeout";
    		case 409:
                return "Conflict";
    		case 410:
                return "Gone";
    		case 411:
                return "Length Required";
    		case 412:
                return "Precondition Failed";
    		case 413:
                return "Request Entity Too Large";
    		case 414:
                return "Request-URI Too Long";
    		case 415:
                return "Unsupported Media Type";
    		case 416:
                return "Requested Range Not Satisfiable";
            case 417:
                return "Expectation Failed";
            case 500:
                return "Internal Server Error";//주요 상태 코드
            case 501:
                return "Not Implemented";
            case 502:
                return "Bad Gateway";
            case 503:
                return "Service Unavailable";
            case 504:
                return "Gateway Timeout";
            case 505://done
                return "HTTP Version Not Supported";
            default:
                return "Unknown";
    	}
        //throw new UnsupportedOperationException("1번 팀원이 구현 필요");
    }

    /**
     * [1번 팀원 작업]
     * HTTP 응답 상태 라인을 생성합니다.
     *
     * RFC 2616 Section 6.1 참고:
     * Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
     *
     * 입력: response.statusCode = 200
     * 출력: "HTTP/1.1 200 OK\r\n"
     *
     * 구현 힌트:
     * 1. getStatusMessage()를 호출하여 상태 메시지 얻기
     *    String message = getStatusMessage(response.getStatusCode());
     *
     * 2. String.format()으로 상태 라인 생성
     *    return String.format("HTTP/1.1 %d %s\r\n",
     *                         response.getStatusCode(), message);
     *
     * 주의사항:
     * - 반드시 \r\n(CRLF)로 끝나야 함
     * - HTTP 버전은 항상 "HTTP/1.1"
     *
     * 예상 작업 시간: 1시간
     *
     * @param response HttpResponse 객체
     * @return 상태 라인 문자열
     */
    public static String buildStatusLine(HttpResponse response) {
        // TODO: 1번 팀원 구현
    	return String.format("HTTP/1.1 %d %s\r\n", response.getStatusCode(), getStatusMessage(response.getStatusCode()));
        //throw new UnsupportedOperationException("1번 팀원이 구현 필요");
    }
    
    //파일 명에 따라서 헤더 매핑
    public static String guessContentType(String n) {
        if (n.endsWith(".css"))   return "text/css; charset=utf-8";
        if (n.endsWith(".js"))    return "application/javascript; charset=utf-8";
        if (n.endsWith(".html"))  return "text/html; charset=utf-8";
        if (n.endsWith(".json"))  return "application/json; charset=utf-8";
        if (n.endsWith(".svg"))   return "image/svg+xml";
        if (n.endsWith(".png"))   return "image/png";
        if (n.endsWith(".jpg") || n.endsWith(".jpeg")) return "image/jpeg";
        if (n.endsWith(".ico"))   return "image/x-icon";
        if (n.endsWith(".woff2")) return "font/woff2";
        if (n.endsWith(".txt"))   return "text/plain; charset=utf-8";
        if (n.endsWith(".gif"))  return "image/gif";
        if (n.endsWith(".ico"))  return "image/x-icon";
        if (n.endsWith(".webp")) return "image/webp";
        return "application/octet-stream";
    }


    // ============================================
    // 4번 팀원 담당 영역
    // ============================================

    /**
     * [4번 팀원 작업]
     * HTTP 응답 헤더들을 생성합니다.
     *
     * RFC 2616 Section 7.1, 14 참고
     *
     * 필수 헤더:
     * - Content-Type: 응답 본문의 MIME 타입
     * - Content-Length: 응답 본문의 바이트 길이
     * - Connection: close (연결 종료)
     *
     * 출력 형식:
     * "Content-Type: text/html; charset=UTF-8\r\n"
     * "Content-Length: 123\r\n"
     * "Connection: close\r\n"
     * "\r\n"  ← 헤더 끝을 나타내는 빈 줄
     *
     * 구현 힌트:
     * 1. StringBuilder 사용
     *    StringBuilder headers = new StringBuilder();
     *
     * 2. Content-Type 추가 (기본값: "text/html; charset=UTF-8")
     *    headers.append("Content-Type: text/html; charset=UTF-8\r\n");
     *
     * 3. Content-Length 계산 (중요: 바이트 길이!)
     *    String body = response.getBody();
     *    int length = body.getBytes(StandardCharsets.UTF_8).length;
     *    headers.append("Content-Length: ").append(length).append("\r\n");
     *
     * 4. Connection: close 추가
     *    headers.append("Connection: close\r\n");
     *
     * 5. 마지막에 \r\n 추가 (헤더 끝)
     *    headers.append("\r\n");
     *
     * 6. 문자열 반환
     *    return headers.toString();
     *
     * 주의사항:
     * - Content-Length는 문자 개수가 아닌 바이트 개수!
     *   body.length()가 아니라 body.getBytes(StandardCharsets.UTF_8).length 사용
     * - 마지막 빈 줄(\r\n) 필수 (헤더와 본문 구분)
     *
     * 예상 작업 시간: 2-3시간
     *
     * @param response HttpResponse 객체
     * @return 헤더 문자열
     */
    public static String buildHeaders(HttpResponse response, String dir) {
    	//response에서 body 가져오기. 
        String body = response.getBody();
        if (body == null) body = "";
        
        //파일 크기 구하기. 요청한 파일이 이미지면 이미지 길이 구하기. 
        int length;
        if(response.isImg()) {
        	//이미지를 요청했지만 실제로 파일이 없는 경우 에러 페이지를 전송.
        	try {
        		length = response.getImg().length;
        	}
        	catch(NullPointerException e) {
        		length = body.getBytes(StandardCharsets.UTF_8).length;
        	}
        }
        else {
        	length = body.getBytes(StandardCharsets.UTF_8).length; 
        }
        
        //파일 명으로 content-type 생성. 파일이 없으면 에러 페이지 전송
        String fileName = response.getFileName();
        String contentType;
        if (fileName != null) {
            contentType = guessContentType(fileName);
        }
        else {
            // 에러 페이지나 동적 페이지일 때
            contentType = "text/html; charset=utf-8";
        }
        
        StringBuilder headers = new StringBuilder(128);
        
        //303일 경우 경로 설정됨.
        if (dir!=null) {
            headers.append("Location: ").append(dir).append("\r\n");
        }
        if(response.getLastModified() != null)
        	headers.append("Last-Modified: ").append(response.getLastModified()).append("\r\n");
        headers.append("Content-Type: ").append(contentType).append("\r\n");
        headers.append("Content-Length: ").append(length).append("\r\n");
        headers.append("Connection: close\r\n");
        headers.append("Date: ")
               .append(java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC)
                       .format(java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME))
               .append("\r\n");
        headers.append("Server: SimpleHTTP/1.0\r\n");
        headers.append("\r\n"); 
        return headers.toString();
    }



    /**
     * [4번 팀원 작업]
     * 완전한 HTTP 응답을 OutputStream에 씁니다.
     *
     * 응답 형식:
     * [상태 라인]
     * [헤더들]
     * [빈 줄]
     * [본문]
     *
     * 구현 힌트:
     * 1. buildStatusLine() 호출
     *    String statusLine = buildStatusLine(response);
     *
     * 2. buildHeaders() 호출
     *    String headers = buildHeaders(response);
     *
     * 3. response.getBody() 가져오기
     *    String body = response.getBody();
     *
     * 4. 위 3개를 순서대로 OutputStream에 쓰기
     *    output.write(statusLine.getBytes(StandardCharsets.UTF_8));
     *    output.write(headers.getBytes(StandardCharsets.UTF_8));
     *    output.write(body.getBytes(StandardCharsets.UTF_8));
     *
     * 5. output.flush() 호출 (중요!)
     *    output.flush();
     *
     * 주의사항:
     * - 모두 UTF-8로 인코딩
     * - 순서: 상태 라인 → 헤더 → 본문
     * - flush() 필수 (버퍼에 있는 데이터를 실제로 전송)
     *
     * 예상 작업 시간: 1-2시간
     *
     * @param response HttpResponse 객체
     * @param output 클라이언트 소켓의 OutputStream
     * @throws IOException 쓰기 실패 시
     */
    public static void writeResponse(HttpResponse response, OutputStream output, String dir)
            throws IOException {
        // 상태 라인 (1번 파트 미구현이어도 동작하게 fallback 포함)
        String statusLine = buildStatusLine(response);
        System.out.print("응답 라인 작성 완료. 응답 라인: " + statusLine);

        String headers = buildHeaders(response, dir);
        System.out.print("헤더 작성 완료. 헤더:\n" + headers);
        
        byte[] body;
        String bodyStr = response.getBody();
        if(response.isImg()) {
        	byte[] b = response.getImg();
        	if(b == null)
        		body = bodyStr.getBytes(StandardCharsets.UTF_8);
        	else
        		body = b;
        }
        else {
            if (bodyStr == null) 
            	bodyStr = "";
            body = bodyStr.getBytes(StandardCharsets.UTF_8);
        }
        
        System.out.println("바디 작성 완료.");
        
        output.write(statusLine.getBytes(StandardCharsets.UTF_8));
        output.write(headers.getBytes(StandardCharsets.UTF_8));
        //head 요청이면 body 안보내기
        if (!response.isHead()) {
            output.write(body);
        }
        output.flush();
    }

    public static void writeBinaryResponse(int statusCode,
                                           String contentType,
                                           byte[] data,
                                           OutputStream output) throws IOException {
        // 상태 라인: 1번 파트가 구현돼 있으면 사용, 실패 시 폴백
        HttpResponse tmp = new HttpResponse();
        tmp.setStatusCode(statusCode);
        String statusLine = buildStatusLine(tmp);


        // 헤더 (바이너리는 data.length 그대로)
        StringBuilder headers = new StringBuilder(128);
        headers.append("Content-Type: ").append(contentType).append("\r\n");
        headers.append("Content-Length: ").append(data == null ? 0 : data.length).append("\r\n");
        headers.append("Connection: close\r\n");
        headers.append("Cache-Control: max-age=3600\r\n"); // 선택(1시간 캐시)
        headers.append("Date: ")
                .append(java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC)
                        .format(java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME))
                .append("\r\n");
        headers.append("Server: SimpleHTTP/1.0\r\n");
        headers.append("\r\n");

        // 전송 (헤더는 ASCII, 바디는 바이너리)
        output.write(statusLine.getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        output.write(headers.toString().getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        if (data != null && data.length > 0) {
            output.write(data);
        }
        output.flush();
    }

}
