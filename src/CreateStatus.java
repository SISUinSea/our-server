
public class CreateStatus {
	public static int returnStatus(HttpRequest request) {
		if(request.getHttpVersion() != "HTTP/1.1")
			return 505;
		switch(request.getMethod()) {
		case "OPTIONS":
			return returnStatusOptions(request);
		case "GET":
			return returnStatusGet(request);
		case "HEAD":
			return returnStatusHead(request);
		case "POST":
			return returnStatusPost(request);
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
		
		return 500;
	}
	//요청이 GET일 때 반환하는 상태 코드
	public static int returnStatusGet(HttpRequest request) {
		if(request.getHeaders().containsKey("if-Modifided-Scince"))
			return 304;
		else if(FileFinder.isFile(request.getUri()))
			return 200;
		else
			return 404;
	}
	public static int returnStatusHead(HttpRequest request) {
		
		return 500;
	}
	public static int returnStatusPost(HttpRequest request) {
		
		return 500;
	}
	public static int returnStatusPut(HttpRequest request) {
		
		return 500;
	}
	public static int returnStatusDelete(HttpRequest request) {
		
		return 500;
	}
	public static int returnStatusTrace(HttpRequest request) {
	
		return 500;
	}
	public static int returnStatusConnect(HttpRequest request) {
	
		return 500;
	}
}
