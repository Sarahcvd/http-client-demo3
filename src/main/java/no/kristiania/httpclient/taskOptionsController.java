package no.kristiania.httpclient;

import no.kristiania.database.Task;
import no.kristiania.database.TaskDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class taskOptionsController implements HttpController{
    private TaskDao taskDao;

    public taskOptionsController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        HttpMessage response = new HttpMessage(getBody());
        response.write(clientSocket);

        HttpMessage redirect = new HttpMessage();
        return redirect;
    }

    public String getBody() throws SQLException {
        String body = "";
        for(Task task : taskDao.list()){
            body += "<option value=" + task.getId() +">" + task.getName() + task.getColorCode() +"</option>";
        }

        return body;

    }
}
