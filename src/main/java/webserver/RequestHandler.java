package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import static java.lang.Integer.parseInt;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            //int contentLenth = -1;
            if (line == null) {
                return;
            }

            String url = HttpRequestUtils.getUrl(line);
            log.debug("request line : {}", line);
            br.readLine();

            Map<String, String> headers = new HashMap<>();
            while (!line.equals("")) {
                log.debug("header : {}", line);
                String[] headerTokens = line.split(": ");
                if (headerTokens.length == 2) {
                    headers.put(headerTokens[0], headerTokens[1]);
                }
                /*if(line.startsWith("Content-Length")){
                    contentLenth = parseInt(line.split(" ")[1]);
                }*/
                line = br.readLine();
            }
            log.debug("Content-Length : {}", headers.get("Content-Length"));

            if (url.equals("/user/create")) {
                //int index = url.indexOf("?");
                //String paramStr = url.substring(index + 1);
                String paramStr = IOUtils.readData(br, parseInt(headers.get("Content-Length")));
                log.debug("Request Body : {}", paramStr);

                Map<String, String> params = HttpRequestUtils.parseQueryString(paramStr);
                User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                log.debug("User : {}", user);

                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos, "/index.html");
                //url = "/index.html";
            }
                DataOutputStream dos = new DataOutputStream(out);
                //byte[] body = "Hello World".getBytes();
                byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url) {
        try{
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("\r\n");
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
