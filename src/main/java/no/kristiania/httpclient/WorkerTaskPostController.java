package no.kristiania.httpclient;

import no.kristiania.database.Task;
import no.kristiania.database.TaskDao;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class WorkerTaskPostController implements HttpController {
    private TaskDao taskDao;

    public WorkerTaskPostController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        QueryString requestedParameter = new QueryString(request.getBody());


        String decodedTaskName = URLDecoder.decode(requestedParameter.getParameter("taskName"), StandardCharsets.UTF_8);
        String decodedTaskColor = URLDecoder.decode(requestedParameter.getParameter("colorCode"), StandardCharsets.UTF_8);

        Task task = new Task();
        task.setName(decodedTaskName);
        task.setColorCode(decodedTaskColor);
        taskDao.insert(task);

        String body = "Hang on, redirecting....";
        String response = "HTTP/1.1 302 REDIRECT\r\n" +
                "Location: http://localhost:8080/newTask.html\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                body;
        // Write the response back to the client
        clientSocket.getOutputStream().write(response.getBytes());
        return request;
    }

}
