package no.kristiania.httpclient;

import no.kristiania.database.WorkerDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class UpdateWorkerController implements HttpController{
    public UpdateWorkerController(WorkerDao workerDao) {

    }

    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {

    }
}
