
public class CreateStatus {
	public int returnStatus(HttpRequest request, FileManager FM) {
		if(!request.getHttpVersion().equals("HTTP/1.1"))
			return 505;
		switch(request.getMethod()) {
		case "OPTIONS":
			return returnStatusOptions(request);
		case "GET":
			return returnStatusGet(request, FM);
		case "HEAD":
			return returnStatusGet(request, FM);
		case "POST":
			return returnStatusPost(request, FM);
		case "PUT":
			return returnStatusPut(request);
		case "DELETE":
			return returnStatusDelete(request);
		case "TRACE":
			return returnStatusTrace(request);
		case "CONNECT":
			return returnStatusConnect(request);
		default:
			return 400;
		}
	}
	public static int returnStatusOptions(HttpRequest request) {
		return 501;
	}
	//요청이 GET일 때 반환하는 상태 코드
	public static int returnStatusGet(HttpRequest request, FileManager FM) {
		if(request.getHeaders().containsKey("if-Modifided-Since"))
			return 304;
		else {
			String uri = request.getUri();
			// 루트 경로(/)면 index.html로 변경
			if(uri.equals("/")) {
				uri = "/index.html";
				request.setUri(uri);
			}
			if(FM.isFile(uri))
				return 200;
			else
				return 404;
		}
	}
	public static int returnStatusPost(HttpRequest request, FileManager FM) {
		if(!FM.isFolder(request.getUri()))
			return 404;
		return FM.makeFile(request.getBody());
	}
	public static int returnStatusPut(HttpRequest request) {
		return 501;
	}
	public static int returnStatusDelete(HttpRequest request) {
		return 501;
	}
	public static int returnStatusTrace(HttpRequest request) {
		return 501;
	}
	public static int returnStatusConnect(HttpRequest request) {
	
		return 501;
	}
}
