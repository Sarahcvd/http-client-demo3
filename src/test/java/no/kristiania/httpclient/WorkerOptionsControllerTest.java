package no.kristiania.httpclient;

import no.kristiania.database.Worker;
import no.kristiania.database.WorkerDao;
import no.kristiania.database.WorkerDaoTest;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class WorkerOptionsControllerTest {

    private WorkerDao workerDao;

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testdatabase;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        workerDao = new WorkerDao(dataSource);
    }

    @Test
    void shouldReturnWorkersAsOptions() throws SQLException {
        WorkerOptionsController controller = new WorkerOptionsController(workerDao);
        Worker worker = WorkerDaoTest.exampleWorker();
        workerDao.insert(worker);

        assertThat(controller.getBody())
                .contains("<option value=" + worker.getId() + ">" + worker.getFirstName() + "</option>");
    }
}