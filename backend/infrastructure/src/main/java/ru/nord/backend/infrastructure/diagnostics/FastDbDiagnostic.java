package ru.nord.backend.infrastructure.diagnostics;

import javax.sql.DataSource;
import java.sql.*;

public class FastDbDiagnostic implements Diagnostic
{
    private final String dbName;
    private final DataSource dataSource;
    private final String query;
    private final int timeout;

    public FastDbDiagnostic(String dbName, DataSource dataSource, String query, int queryTimeout) {
        this.dbName = dbName;
        this.dataSource = dataSource;
        this.query = query;
        this.timeout = queryTimeout / 1000; // millis to seconds
    }

    @Override
    public DiagnosticStatus call(DiagnosticContext context) throws Exception
    {
        try (final Connection conn = dataSource.getConnection(); final Statement statement = conn.createStatement()) {
            statement.setQueryTimeout(timeout);
            statement.execute(query);
        } catch (Exception ex) {
            throw new DiagnosticException(String.format("Ошибка выполнения запроса к БД '%s'", dbName), ex);
        }
        return new DiagnosticStatus(true, String.format("Тестовый запрос '%s' к БД '%s' успешно выполнен", query, dbName));
    }
}
