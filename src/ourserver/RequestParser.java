package ourserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Locale;
//HTTP 헤더는 대소문자 구분 안 함. (Host == host == HOST)
//지금은 키를 그대로 쓰니까, 섞여 들어오면 다른 키처럼 저장될 수 있음. → 키를 소문자로 바꿔서 저장하면 해결됨.

public class RequestParser {

    public Map<String, String> parseHeaders(BufferedReader br) throws IOException {
        // “String 이름표”와 “String 내용물”이 있는 서랍장을 새로 만든다.
        Map<String, String> headers = new LinkedHashMap<>();

        String line; // 한 줄씩 읽을 공간 준비

        // 한 줄씩 읽기 시작
        int count = 0;  // ← 추가: 읽은 헤더 줄 개수

        // 한 줄씩 읽기 시작
        // br.readLine() 입력 스트림에서 한 줄 읽기
        while ((line = br.readLine()) != null) {
            //한 줄씩 읽다가 더 이상 없으면 끝
            if (line.isEmpty()) { // 빈 줄이면 헤더 끝
                break;
            }

            //즉, : (콜론) 기준으로 왼쪽이 이름, 오른쪽이 값
            //1️ 콜론 위치 찾기
            //2️ 앞뒤 공백 제거(trim)
            //3️ Map에 저장
            //Host: localhost:8080
            // (A) 추가: 헤더 줄 개수 제한 (예: 100줄)
            if (++count > 100) {
                throw new IOException("Too many header lines (limit 100)");
            }

            // (B) 추가: 콜론 없는 잘못된 줄은 무시
            int idx = line.indexOf(':');
            if (idx == -1) {
                continue;
            }  // 1️ 콜론(:) 위치 찾기
            //line.indexOf(':') 문자열에서 콜론(:)이 어디 있는지 위치(번호)를 찾음

              // 콜론이 있다면 (정상적인 헤더)

                String name = line.substring(0, idx).trim();        // 2️ 이름 부분
                // substring(0, idx) 그 위치 전까지의 문자열 (이름)

                String value = line.substring(idx + 1).trim();      // 3️ 값 부분
                // substring(idx + 1) 그 위치 다음부터 끝까지의 문자열 (값)
                // trim() 앞뒤 공백 제거

                String key = name.toLowerCase(Locale.ROOT);
                // HTTP 헤더는 대소문자 구분 안 함. (Host == host == HOST)
                // → 키를 소문자로 바꿔서 저장하면 해결됨.


                // 4️ Map에 추가
                // 이미 같은 이름의 헤더가 있으면 이어붙임
                if (headers.containsKey(key)) {
                    headers.put(key, headers.get(key) + ", " + value);
                } else {
                    headers.put(key, value);
                }
                // headers.put(name, value) Map에 이름(key)과 값(value)을 저장
                // 이렇게 저장된 헤더 안에는 아래와 같이 저장됨
                //"Host" → "localhost:8080"
                //"User-Agent" → "Mozilla/5.0"




        }

        return headers;
    }

}
