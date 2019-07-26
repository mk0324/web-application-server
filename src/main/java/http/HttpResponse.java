package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private DataOutputStream dos;
    private Map<String, String> responseHeaders = new HashMap<>();

    public HttpResponse(OutputStream dos){
        this.dos = new DataOutputStream(dos);
    }

    public void addHeader(String key, String value){
        responseHeaders.put(key, value);
    }

    public void forward(String subUrl){
        try {
            byte[] body = Files.readAllBytes(new
                    File("./webapp" + subUrl).toPath());
            if (subUrl.endsWith(".css")) {
                responseHeaders.put("Content-Type", "text/css");
            } else if (subUrl.endsWith(".js")) {
                responseHeaders.put("Content-Type", "application/javascript");
            } else {
                responseHeaders.put("Content-Type", "text/html;charset=utf-8");
            }
            responseHeaders.put("Content-Length", body.length + "");
            response200Header();
            responseBody(body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void forwardBody(String body){
        byte[] contents = body.getBytes();
        responseHeaders.put("Content-Type", "text/html;charset=utf-8");
        responseHeaders.put("Content-Length", contents.length + "");
        response200Header();
        responseBody(contents);
    }

    public void sendRedirect(String redirectUrl){
        try{
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            processHeaders();
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }

    private void response200Header() {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            processHeaders();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void processHeaders() {
        try {
            Set<String> keys = responseHeaders.keySet();
            for (String key : keys) {
                dos.writeBytes(key + ": " + responseHeaders.get(key) + "\r\n");
            }
        }catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
