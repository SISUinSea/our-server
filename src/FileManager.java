import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {
	static FileReader fin = null;
	static BufferedReader br = null;
	private static final String DOCUMENT_ROOT = "www";

	public static boolean isFile(String uri) {
		try {
			fin = new FileReader(DOCUMENT_ROOT + uri);
		} catch (FileNotFoundException e) {
			// 파일을 찾지 못함
			return false;
		}
		return true;
	}
	public static String returnFile() throws IOException {
	
		if(fin == null)
			return null;
		StringBuilder html = new StringBuilder();
		String read = null;
		br = new BufferedReader(fin);
		while(true) {
			read = br.readLine();
			if(read == null)
				break;
			html.append(read);
		}
		return html.toString();
	}
}
