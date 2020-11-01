package no.kristiania.httpclient;

import no.kristiania.database.Worker;
import no.kristiania.database.WorkerTask;
import no.kristiania.database.WorkerTaskDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class WorkerTaskOptionsController implements HttpController{
    private WorkerTaskDao taskDao;

    public WorkerTaskOptionsController(WorkerTaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        HttpMessage response = new HttpMessage(getBody());
        response.write(clientSocket);
    }

    public String getBody() throws SQLException {
        String body = "";
        for(WorkerTask task : taskDao.list()){
            body += "<option value=" + task.getId() +">" + task.getName() + "</option>";
        }

        return body;
        /*return workerDao.list()
                .stream().map(w -> "<option value=" + w.getId() +">" + w.getFirstName() + "</option>")
                .collect(Collectors.joining());*/
    }
}
