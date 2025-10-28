import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {
	static FileReader fin = null;
	static BufferedReader br = null;
	public static boolean isFile(String uri) {
		try {
			fin = new FileReader(".." + uri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
