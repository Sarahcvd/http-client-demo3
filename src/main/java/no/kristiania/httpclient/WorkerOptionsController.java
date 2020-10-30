package no.kristiania.httpclient;

import no.kristiania.database.Worker;
import no.kristiania.database.WorkerDao;
import no.kristiania.database.WorkerTaskDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class WorkerOptionsController implements HttpController{
    private WorkerDao workerDao;

    public WorkerOptionsController(WorkerDao workerDao) {
        this.workerDao = workerDao;
    }

    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        String body = getBody();
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                body;
        // Write the response back to the client
        clientSocket.getOutputStream().write(response.getBytes());
    }

    public String getBody() throws SQLException {
        return workerDao.list()
                .stream().map(w -> "<option value=" + w.getId() +">" + w.getFirstName() + "</option>")
                .collect(Collectors.joining());
    }
}
