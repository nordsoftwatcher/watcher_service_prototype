package ru.nord.backend.infrastructure.diagnostics;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;

public class DbDiagnostic implements Diagnostic
{
    private final DataSource dataSource;
    private final String query;

    public DbDiagnostic(DataSource dataSource, String query) {
        if(dataSource == null) {
            throw new IllegalArgumentException("dataSource must be non-null");
        }
        this.dataSource = dataSource;
        this.query = query;
    }

    @Override
    public DiagnosticStatus call(DiagnosticContext ctx) throws Exception {
        String info;
        try (Connection connection = dataSource.getConnection()) {
            final DatabaseMetaData metaData = connection.getMetaData();
            final String connectionUrl = metaData.getURL();
            final String userName = metaData.getUserName();
            info = String.format("url: %s; user: %s; query: %s", connectionUrl, userName, this.query);
        }
        catch (Exception ex) {
            throw new DiagnosticException("Во время подключения к БД произошла ошибка", ex);
        }

        try (final Connection conn = dataSource.getConnection(); final Statement statement = conn.createStatement()) {
            statement.execute(query);
        }
        catch (Exception ex) {
            throw new DiagnosticException("Ошибка выполнения запроса к БД ["+info+"]", ex);
        }
        return new DiagnosticStatus(true, "Тестовый запрос к БД успешно выполнен ["+info+"]");
    }
}
