package no.kristiania.httpclient;

import no.kristiania.database.WorkerTask;
import no.kristiania.database.WorkerTaskDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class WorkerTaskPostController implements HttpController {
    private WorkerTaskDao workerTaskDao;

    public WorkerTaskPostController(WorkerTaskDao workerTaskDao) {
        this.workerTaskDao = workerTaskDao;
    }

    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        QueryString requestedParameter = new QueryString(request.getBody());

        WorkerTask task = new WorkerTask();
        task.setName(requestedParameter.getParameter("taskName"));
        workerTaskDao.insert(task);

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
