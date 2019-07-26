package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.RequestHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();
    private RequestLine requestLine;

    // 생성자에서 처리?-?
    public HttpRequest(InputStream in){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            if (line == null) {
                return;
            }

            // 첫번째 라인 - Request Line
            // GET /user/create?userId=javajigi&password=password&name=JaeSung HTTP/1.1
            // POST /user/create HTTP/1.1
            // processRequestLine()에 대한 메소드를 새로운 클래스를 추가하여 해결
            requestLine = new RequestLine(line);

            line = br.readLine();
            //boolean logined = false;
            while (!line.equals("")) {
                log.debug("header : {}", line);
                String[] headerTokens = line.split(": ");
                /*if (headerTokens.length == 2) {
                    headers.put(headerTokens[0], headerTokens[1]);
                }*/
                headers.put(headerTokens[0].trim(), headerTokens[1].trim());
                /*if (line.contains("Cookie")) {
                    logined = isLogin(line);
                }*/
                line = br.readLine();
            }

            if (getMethod().isPost()) {
               String body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
               params = HttpRequestUtils.parseQueryString(body);
            } else {
                params = requestLine.getParams();
            }
        }  catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getHeader(String headerKey) {
        return headers.get(headerKey);
    }

    public String getParameter(String paramKey) {
        return params.get(paramKey);
    }
}
