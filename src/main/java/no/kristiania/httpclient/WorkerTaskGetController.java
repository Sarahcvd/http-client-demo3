package no.kristiania.httpclient;

import no.kristiania.database.Worker;
import no.kristiania.database.WorkerTask;
import no.kristiania.database.WorkerTaskDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class WorkerTaskGetController implements HttpController {
    private WorkerTaskDao workerTaskDao;

    public WorkerTaskGetController(WorkerTaskDao workerTaskDao) {
        this.workerTaskDao = workerTaskDao;
    }

    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        String body = "<ul>";
        for(WorkerTask task : workerTaskDao.list()) {
            body += "<li>" + task.getName() + "</li>";
        }

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
