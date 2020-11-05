package no.kristiania.httpclient;

import no.kristiania.database.Task;
import no.kristiania.database.TaskDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class WorkerTaskGetController implements HttpController {
    private TaskDao taskDao;

    public WorkerTaskGetController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        String body = "<ul>";
        for(Task task : taskDao.list()) {
            body += "<li colorCode="+ task.getColorCode() +">" + task.getName() + "</br>   Current status:   " + task.getColorCode() + "</li>";
        }

        body += "</ul>";
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Content-Type: text/html\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                body;


        // Write the response back to the client
        clientSocket.getOutputStream().write(response.getBytes());
        return request;
    }
}
