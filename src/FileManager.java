import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

	private static final String DOCUMENT_ROOT = "www";
	private String filePath;
	private String extraRequest;
	private String reDir;


	public boolean isFile(String uri) {
		System.out.println(uri);
		//?뒤의 요청은 추가 요청사항으로 처리
		String[] ulist =  uri.split("\\?",2);
		//파일 경로
		filePath = ulist[0];
		try {
			extraRequest = ulist[1];
		} catch(ArrayIndexOutOfBoundsException e) {
			//?가 없으면 비워두기
			extraRequest = "";
		}
		if (uri.contains("..")) {
			throw new IllegalArgumentException("파일 탐색 범위를 벗어난 요청입니다.");
		}
		File file = new File(DOCUMENT_ROOT, filePath);
		return file.exists() && file.isFile();
	}

	public boolean isFolder(String uri) {
		System.out.println(uri);
		//?뒤의 요청은 추가 요청사항으로 처리
		String[] ulist =  uri.split("\\?",2);
		//파일 경로
		filePath = ulist[0];
		try {
			extraRequest = ulist[1];
		} catch(ArrayIndexOutOfBoundsException e) {
			//?가 없으면 비워두기
			extraRequest = "";
		}
		File f = new File(DOCUMENT_ROOT, filePath);
		// 3) 진짜로 그런 경로가 서버에 있냐
		return f.exists();
	}

	public String returnFileName() {
		String []s = filePath.split("\\/");
		return s[s.length-1];
	}

	
	public byte[] returnImg(String uri) throws IOException, InterruptedException {
		Path base = Paths.get(DOCUMENT_ROOT + filePath);
		Path target = base.normalize();
		byte[] data = Files.readAllBytes(target);
		
		if (uri.contains("..")) {
			throw new IllegalArgumentException("파일 탐색 범위를 벗어난 요청입니다.");
		}
		
		return data;
	}
	
	public String returnFile(String uri) throws IOException, InterruptedException {
		if (uri.contains("..")) {
			throw new IllegalArgumentException("파일 탐색 범위를 벗어난 요청입니다.");
		}
		if (uri.equals("/VBF.txt")) {
			System.out.println("엄청 큰 파일 요청... 5초 걸림");
			Thread.sleep(5000);
			System.out.println("5초 지남 보낸다.");
		}
		FileReader fileReader = new FileReader(DOCUMENT_ROOT + filePath);
		if(fileReader == null)
			return null;
		StringBuilder html = new StringBuilder();
		String read = null;
		BufferedReader br = new BufferedReader(fileReader);

		while(true) {
			read = br.readLine();
			if(read == null)
				break;
			html.append(read).append("\n");
		}
		fileReader.close();
		br.close();
		return html.toString();
	}

	public int makeFile(String body) {
		//들어온 문자열을 각 키와 value로 구분
		Map<String, String> map = new HashMap<>();
		String[] pairs = body.split("&");
		for (String pair : pairs) {
			if (pair.isEmpty()) continue;

			// key=value 형태이지만 value가 없을 수도 있으니 limit=2
			String[] kv = pair.split("=", 2);
			String rawKey = kv[0];
			String rawVal = kv.length > 1 ? kv[1] : "";

			// application/x-www-form-urlencoded 는 + 를 공백으로 씀
			String key = urlDecode(rawKey);
			String val = urlDecode(rawVal);

			map.put(key, val);
		}
		System.out.println(map.toString());

		String fileName = "";
		if(extraRequest.startsWith("id=")) {
			String value = extraRequest.substring(3);
			fileName = value + ".txt";  // "1.txt"
		}

		if(map.containsKey("content")) {
			return addComments(map.get("content"),fileName);
		}
		if(map.containsKey("title")) {
			return addPost(map.get("title"), map.get("body"));
		}
		else
			return 400;
	}

	private static String urlDecode(String s) {
		// null 방어
		if (s == null) return "";
		// UTF-8로 디코딩
		return URLDecoder.decode(s, StandardCharsets.UTF_8);
	}

	private int addComments(String comment, String fileName) {
		// 댓글 달 대상 파일: www/Posted/1.txt 이런 식
		File f = new File(DOCUMENT_ROOT + "/Posted/" + fileName);

		if (!f.exists()) {
			System.out.println("댓글 대상 파일이 없습니다: " + f.getPath());
			return 404;
		}

		// 1) 원래 파일을 줄 단위로 다 읽어온다
		java.util.List<String> lines = new java.util.ArrayList<>();
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = r.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return 500;
		}

		// 2) 댓글이 시작되는 위치 찾기
		int emptyCount = 0;
		int commentStartIndex = -1;
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).trim().isEmpty()) {
				emptyCount++;
				if (emptyCount == 2) {
					// 이 다음 줄이 댓글이 시작되는 자리
					commentStartIndex = i + 1;
					break;
				}
			}
		}

		// 3) 아직 댓글 구역이 없으면 맨 끝에 빈 줄 하나 넣어서 만들어준다
		if (commentStartIndex == -1) {
			// 본문까지는 있는 상태고, 댓글 구역은 없는 상태
			// 그래서 "빈 줄" 하나 추가하고 그 뒤가 댓글이 되도록 한다
			lines.add("");               // ← 댓글 구역 시작
			commentStartIndex = lines.size(); // 지금 위치가 댓글 시작
		}

		// 4) 들어온 comment가 여러 줄이면 줄마다 한 댓글로 넣기
		String[] newComments = comment.split("\\r?\\n");
		for (String c : newComments) {
			c = c.trim();
			if (c.isEmpty()) continue;  // 빈 줄은 버림
			lines.add(c);
		}

		// 5) 전부 다시 파일에 쓴다
		try (java.io.BufferedWriter w =
					 new java.io.BufferedWriter(
							 new java.io.OutputStreamWriter(
									 new java.io.FileOutputStream(f, false),  // 덮어쓰기
									 java.nio.charset.StandardCharsets.UTF_8))) {

			for (int i = 0; i < lines.size(); i++) {
				w.write(lines.get(i));
				w.write("\n"); // 모든 줄에 다시 개행
			}
		} catch (IOException e) {
			e.printStackTrace();
			return 500;
		}
		reDir = "/Page/View.html?id=" + fileName.substring(0, fileName.length() - 4);
		return 303;
	}

	private int addPost(String title, String body) {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

		String ts = now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
		String fileName = ts + ".txt";

		File f = new File(DOCUMENT_ROOT + "/Posted/" + fileName);
		String iso = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

		try (BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(f), "UTF-8"))) {
			bw.write(title);
			bw.newLine();
			bw.write("date: " + iso);
			bw.newLine();
			bw.newLine();
			bw.write(body);
		}catch(IOException e){
			System.out.println("서버 에러");
			return 500;
		}

		System.out.println("3");
		reDir = "/Page/List.html";
		return appendToIndex(fileName);
	}

	private int appendToIndex(String fileName) {
		// 1) 폴더 객체
		File dir = new File(DOCUMENT_ROOT + "/Posted");
		// 폴더가 없으면 만들어두기
		dir.mkdirs();

		// 2) 인덱스 파일 객체 (_files.txt)
		File indexFile = new File(dir, "_files.txt");

		try {
			// 파일이 없으면 먼저 만들어둔다
			if (!indexFile.exists()) {
				indexFile.createNewFile();
			}

			// 3) 이어쓰기 모드로 연다 (append = true)
			try (java.io.BufferedWriter bw =
						 new java.io.BufferedWriter(
								 new java.io.OutputStreamWriter(
										 new java.io.FileOutputStream(indexFile, true),
										 java.nio.charset.StandardCharsets.UTF_8))) {

				bw.write(fileName);
				bw.write("\n");   // 한 줄로 추가
			}

		} catch (IOException e) {
			e.printStackTrace();
			return 500;
		}
		return 303;
	}

	public String getReDir() {
		return reDir;
	}
}
