package no.kristiania.database;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkerTaskDao {
    private DataSource dataSource;

    public WorkerTaskDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<WorkerTask> list() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from worker")) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<WorkerTask> workers = new ArrayList<>();
                    while (rs.next()) {
                        Worker worker = new Worker();
                        workers.add(mapRowToTask(rs));
                        rs.getString("first_name");
                        rs.getString("last_name");
                        rs.getString("email_address");
                        worker.setId(rs.getLong("id"));
                    }
                    return workers;
                }
            }
        }
    }

    private WorkerTask mapRowToTask(ResultSet rs) {
        return new WorkerTask();
    }

    public void insert(WorkerTask task) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO worker (first_name, last_name, email_address) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, task.getName());
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    task.setId(generatedKeys.getLong("id"));
                }
            }
        }
    }
}
