package no.kristiania.httpclient;

import no.kristiania.database.Worker;

import java.io.IOException;
import java.net.Socket;

public class WorkerTaskGetController implements HttpController {
    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException {
        String body = "<ul>";

        /*  Han sletter dette, men det skal sikkert med senere
        for (Worker worker : workerDao.list()) {
            body += "<li>" + worker.getFirstName() + " " + worker.getLastName() + " " + worker.getEmailAddress() + "</li>";
        }*/

        body += "</ul>";
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Content-Type: text/html\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                body;

        // Write the response back to the client
        clientSocket.getOutputStream().write(response.getBytes());
    }
}
