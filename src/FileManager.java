import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {

	private static final String DOCUMENT_ROOT = "www";


	public static boolean isFile(String uri) {
		if (uri.contains("..")) {
			throw new IllegalArgumentException("파일 탐색 범위를 벗어난 요청입니다.");
		}
		File file = new File(DOCUMENT_ROOT+uri);
		return file.exists() && file.isFile();
	}

	public static String returnFile(String uri) throws IOException, InterruptedException {
		if (uri.contains("..")) {
			throw new IllegalArgumentException("파일 탐색 범위를 벗어난 요청입니다.");
		}
		if (uri.equals("/VBF.txt")) {
			System.out.println("엄청 큰 파일 요청... 5초 걸림");
			Thread.sleep(5000);
			System.out.println("5초 지남 보낸다.");
		}
		FileReader fileReader = new FileReader(DOCUMENT_ROOT + uri);
		if(fileReader == null)
			return null;
		StringBuilder html = new StringBuilder();
		String read = null;
		BufferedReader br = new BufferedReader(fileReader);

		while(true) {
			read = br.readLine();
			if(read == null)
				break;
			html.append(read);
		}
		fileReader.close();
		br.close();
		return html.toString();
	}
}
