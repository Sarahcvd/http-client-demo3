package no.kristiania.database;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkerTaskDao extends AbstractDao <WorkerTask>{

    public WorkerTaskDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected WorkerTask mapRow(ResultSet rs) throws SQLException {
        return null;
    }
}
