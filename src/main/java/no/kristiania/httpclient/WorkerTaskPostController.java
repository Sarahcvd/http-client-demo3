package no.kristiania.httpclient;

import java.io.IOException;
import java.net.Socket;

public class WorkerTaskPostController implements HttpController {
    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException {
        QueryString requestedParameter = new QueryString(request.getBody());

        String body = "Okay";
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                body;
        // Write the response back to the client
        clientSocket.getOutputStream().write(response.getBytes());
    }
}
