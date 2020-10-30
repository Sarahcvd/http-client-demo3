package no.kristiania.database;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkerDao extends AbstractDao<Worker> {

    public WorkerDao(DataSource dataSource) {
        super(dataSource);
    }

    public List<Worker> list() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM worker")) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<Worker> workers = new ArrayList<>();
                    while (rs.next()) {
                        workers.add(mapRow(rs));
                    }
                    return workers;
                }
            }
        }
    }

    public void insert(Worker worker) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO worker (first_name, last_name, email_address) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS

            )) {
                statement.setString(1, worker.getFirstName());
                statement.setString(2, worker.getLastName());
                statement.setString(3, worker.getEmailAddress());
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    worker.setId(generatedKeys.getLong("id"));
                }
            }
        }
    }

    public Worker retrieve(Long id) throws SQLException {
        return retrieve(id, "SELECT * FROM worker WHERE id = ?");
    }

    @Override
    protected Worker mapRow(ResultSet rs) throws SQLException {
        Worker worker = new Worker();
        worker.setId(rs.getLong("id"));
        worker.setFirstName(rs.getString("first_name"));
        worker.setLastName(rs.getString("last_name"));
        worker.setEmailAddress(rs.getString("email_address"));
        return worker;
    }
}