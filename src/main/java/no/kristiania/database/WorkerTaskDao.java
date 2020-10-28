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
            try (PreparedStatement statement = connection.prepareStatement("select * from worker_tasks")) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<WorkerTask> workers = new ArrayList<>();
                    while (rs.next()) {
                        workers.add(mapRowToTask(rs));
                    }
                    return workers;
                }
            }
        }
    }
    public void insert(WorkerTask task) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO worker_tasks (name) VALUES (?)",
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

    public WorkerTask retrieve(Long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from worker_tasks WHERE id = ?")) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return mapRowToTask(rs);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    private WorkerTask mapRowToTask(ResultSet rs) throws SQLException {
        WorkerTask task = new WorkerTask();
        task.setId(rs.getLong("id"));
        task.setName(rs.getString("name"));
        return task;
    }


}
