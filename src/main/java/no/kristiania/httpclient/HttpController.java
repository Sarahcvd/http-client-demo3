package no.kristiania.httpclient;

import java.io.IOException;
import java.net.Socket;

public interface HttpController {
    void handle(HttpMessage request, Socket clientSocket) throws IOException;
}
