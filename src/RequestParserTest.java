//import static org.assertj.core.api.Assertions.*;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//class RequestParserTest {
//
//
//    @Test()
//    @DisplayName("HTTP Request Line이 주어졌을 때, 메소드, 경로, 프로토콜을 올바르게 파싱하는지 검증")
//    void parseRequestLine_Normal() {
//        // given
//        String requestLine = "GET /index.html HTTP/1.1";
//        HttpRequest request = new HttpRequest();
//        RequestParser parser = new RequestParser();
//
//        // when
//        parser.parseRequestLine(requestLine, request);
//        // then
//        assertThat(request.getHeaders()).isEqualTo("GET");
//        assertThat(request.getUri()).isEqualTo("/index.html");
//        assertThat(request.getHttpVersion()).isEqualTo("HTTP/1.1");
//    }
//}