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
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        QueryString requestedParameter = new QueryString(request.getBody());

        Long workerId = Long.valueOf(requestedParameter.getParameter("workerId"));
        Long taskId = Long.valueOf(requestedParameter.getParameter("taskId"));
        Worker worker = workerDao.retrieve(workerId);
        worker.setTaskId(taskId);

        workerDao.update(worker);


    }
}
