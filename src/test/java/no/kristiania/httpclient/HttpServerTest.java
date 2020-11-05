package no.kristiania.httpclient;

import no.kristiania.database.Worker;
import no.kristiania.database.WorkerDao;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Date;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpServerTest {

    private JdbcDataSource dataSource;
    private HttpServer server;

    @BeforeEach
    void setUp() throws IOException {
        dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testdatabase;DB_CLOSE_DELAY=-1");

        Flyway.configure().dataSource(dataSource).load().migrate();
        // Declaring port 0 means: operative system will give us a "random"(unused) port
        server = new HttpServer(0, dataSource);
    }

    @Test
    void shouldReturnSuccessfulStatusCode() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo");
        assertEquals(200, client.getStatusCode());
    }

    @Test
    void shouldReturnUnsuccessfulStatusCode() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo?status=404");
        assertEquals(404, client.getStatusCode());
    }

    @Test
    void shouldReturnContentLength() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo?body=HelloWorld");
        assertEquals("10", client.getResponseHeader("Content-Length"));
    }

    @Test
    void shouldReturnResponseBody() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo?body=HelloWorld");
        assertEquals("HelloWorld", client.getResponseBody());
    }

    @Test
    void shouldReturnFileFromDisk() throws IOException {
        File contentRoot = new File("target/test-classes");

        String fileContent = "Hello World " + new Date();
        Files.writeString(new File(contentRoot,"test.txt").toPath(), fileContent);

        HttpClient client = new HttpClient("localhost", server.getPort(), "/test.txt");
        assertEquals(fileContent, client.getResponseBody());
        assertEquals("text/plain", client.getResponseHeader("Content-Type"));
    }

    @Test
    void shouldReturnCorrectContentType() throws IOException {
        File contentRoot = new File("target/test-classes");

        Files.writeString(new File(contentRoot, "index.html").toPath(), "<h2>Hello World</h2>");

        HttpClient client = new HttpClient("localhost", server.getPort(), "/index.html");
        assertEquals("text/html", client.getResponseHeader("Content-Type"));
    }

    @Test
    void shouldReturn404IfFileNotFound() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/notFound.txt");
        assertEquals(404, client.getStatusCode());
    }



    @Test
    void shouldPostNewWorker() throws IOException, SQLException {
        String requestBody = "first_name=wali&email_address=wgbjork@gmail.com";
        HttpClient client = new HttpClient("localhost", server.getPort(), "/api/newWorker", "POST", requestBody);
        assertEquals(302, client.getStatusCode());
        assertThat(server.getWorkers())
                .filteredOn(worker -> worker.getFirstName().equals("wali"))
                .isNotEmpty()
                .satisfies(w -> assertThat(w.get(0).getEmailAddress()).isEqualTo("wgbjork@gmail.com"));
    }

    @Test
    void shouldReturnExistingMembers() throws IOException, SQLException {
        WorkerDao workerDao = new WorkerDao(dataSource);
        Worker worker = new Worker();
        worker.setFirstName("wali");
        worker.setLastName("gustav");
        worker.setEmailAddress("wgbjork@gmail.com");
        workerDao.insert(worker);
        HttpClient client = new HttpClient("localhost", server.getPort(), "/api/worker");
        assertThat(client.getResponseBody()).contains("<li>wali gustav wgbjork@gmail.com</li>");
    }

}