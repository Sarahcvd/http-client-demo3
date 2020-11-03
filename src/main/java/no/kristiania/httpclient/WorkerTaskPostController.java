package no.kristiania.httpclient;

import no.kristiania.database.Task;
import no.kristiania.database.TaskDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class WorkerTaskPostController implements HttpController {
    private TaskDao taskDao;

    public WorkerTaskPostController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        QueryString requestedParameter = new QueryString(request.getBody());

        Task task = new Task();
        task.setName(requestedParameter.getParameter("taskName"));
        taskDao.insert(task);

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
