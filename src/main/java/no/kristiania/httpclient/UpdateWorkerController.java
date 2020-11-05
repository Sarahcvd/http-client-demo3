package no.kristiania.httpclient;

import no.kristiania.database.Worker;
import no.kristiania.database.WorkerDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class UpdateWorkerController implements HttpController{
    private WorkerDao workerDao;

    public UpdateWorkerController(WorkerDao workerDao) {
        this.workerDao = workerDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        HttpMessage response = handle(request);
        response.write(clientSocket);
        return response;
    }

    public HttpMessage handle(HttpMessage request) throws SQLException {
        QueryString requestedParameter = new QueryString(request.getBody());

        Integer workerId = Integer.valueOf(requestedParameter.getParameter("workerId"));
        Integer taskId = Integer.valueOf(requestedParameter.getParameter("taskId"));
        Worker worker = workerDao.retrieve(workerId);
        worker.setTaskId(taskId);

        workerDao.update(worker);

        HttpMessage redirect = new HttpMessage();
        redirect.setStartLine("HTTP/1.1 302 Redirect");
        redirect.getHeaders().put("Location", "http://localhost:8080/editWorker.html");
        return redirect;
    }
}
