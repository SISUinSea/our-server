//(선택) 간단 파일 로더: FileText.java (txt만 읽어 문자열로)
//
//지금은 txt로 테스트하니까 문자열 로더로 충분. (바이너리는 나중에 확장)
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class FileText {
    private FileText() {}
    public static String readUtf8(File f) throws Exception {
        byte[] bytes = Files.readAllBytes(f.toPath());
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
