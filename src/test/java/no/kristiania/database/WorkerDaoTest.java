package no.kristiania.database;

import no.kristiania.httpclient.HttpMessage;
import no.kristiania.httpclient.UpdateWorkerController;
import no.kristiania.httpclient.WorkerOptionsController;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkerDaoTest {
    private WorkerDao workerDao;
    private static Random random = new Random();
    private TaskDao taskDao;


    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testdatabase;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        workerDao = new WorkerDao(dataSource);
        taskDao = new TaskDao(dataSource);
    }

    @Test
    void shouldListInsertedWorkers() throws SQLException {
        Worker worker1 = exampleWorker();
        Worker worker2 = exampleWorker();
        workerDao.insert(worker1);
        workerDao.insert(worker2);
        assertThat(workerDao.list())
                .extracting(Worker::getFirstName)
                .contains(worker1.getFirstName(), worker2.getFirstName());
    }

    @Test
    void shouldRetrieveAllWorkerProperties() throws SQLException {
        workerDao.insert(exampleWorker());
        workerDao.insert(exampleWorker());
        Worker worker = exampleWorker();
        workerDao.insert(worker);
        assertThat(worker).hasNoNullFieldsOrPropertiesExcept("taskId");
        assertThat(workerDao.retrieve(worker.getId()))
                .usingRecursiveComparison()
                .isEqualTo(worker);
    }

    @Test
    void shouldReturnWorkersAsOptions() throws SQLException {
        WorkerOptionsController controller = new WorkerOptionsController(workerDao);
        Worker worker = WorkerDaoTest.exampleWorker();
        workerDao.insert(worker);

        assertThat(controller.getBody())
                .contains("<option value=" + worker.getId() + ">" + worker.getFirstName() + "</option>");
    }

    @Test
    void shouldUpdateExistingWorkerWithNewTask() throws IOException, SQLException {
        UpdateWorkerController controller = new UpdateWorkerController(workerDao);

        Worker worker = exampleWorker();
        workerDao.insert(worker);

        Task task = TaskDaoTest.exampleTask();
        new TaskDao(workerDao.dataSource).insert(task);

        String body = "workerId=" + worker.getId() + "&taskId=" + task.getId();

        HttpMessage response = controller.handle(new HttpMessage(body));
        assertThat(workerDao.retrieve(worker.getId()).getTaskId())
                .isEqualTo(task.getId());
        assertThat(response.getStartLine())
                .isEqualTo("HTTP/1.1 302 Redirect");
        assertThat(response.getHeaders().get("Location"))
                .isEqualTo("http://localhost:8080/editWorker.html");
    }

    public static Worker exampleWorker() {
        Worker worker = new Worker();
        worker.setFirstName(exampleFirstName());
        worker.setLastName(exampleLastName());
        worker.setEmailAddress(exampleEmailAddress());
        return worker;
    }

    /** Returns a random first name */
    private static String exampleFirstName() {
        String[] options = {"Johannes", "Christian", "Lucas", "Matheus", "Markus"};
        return options[random.nextInt(options.length)];
    }
    /** Returns a random last name */
    private static String exampleLastName() {
        String[] options = {"Johnsson", "Elfborg", "Colason", "Dobbelthode", "Trebein"};
        return options[random.nextInt(options.length)];
    }
    /** Returns a random email-address */
    private static String exampleEmailAddress() {
        String[] options = {"loller@lol.no", "jumper@jump.dk", "supreme@beta.uk", "simp@finlandia.se", "cheaptents@larsmonse.no"};
        return options[random.nextInt(options.length)];
    }
}