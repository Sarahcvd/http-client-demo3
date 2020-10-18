package no.kristiania.database;

import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WorkerDao {

    private final DataSource dataSource;

    public WorkerDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void main(String[] args) throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/kristianiaworker");
        dataSource.setUser("kristianiashop");
        dataSource.setPassword("harasilaw");
        Flyway.configure().dataSource(dataSource).load().migrate();

        WorkerDao workerDao = new WorkerDao(dataSource);

        System.out.println("Please enter worker name:");
        Scanner scanner = new Scanner(System.in);
        Worker worker = new Worker();
        worker.setFirstName(scanner.nextLine());

        workerDao.insert(worker);
        System.out.println(workerDao.list());
    }

    public void insert(Worker worker) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO worker (first_name) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS
                    )) {
                statement.setString(1, worker.getFirstName());
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    worker.setId(generatedKeys.getLong("id"));
                }
            }
        }
    }

    public Worker retrieve(Long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from worker WHERE id = ?")) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        Worker worker = new Worker();
                        worker.setId(rs.getLong("id"));
                        worker.setFirstName(rs.getString("first_name"));
                        return worker;
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    public List<String> list() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from worker")) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<String> workers = new ArrayList<>();
                    while (rs.next()) {
                        workers.add(rs.getString("first_name"));
                    }
                    return workers;
                }
            }
        }
    }

}
