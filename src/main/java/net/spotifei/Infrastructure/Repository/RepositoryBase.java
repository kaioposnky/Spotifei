package net.spotifei.Infrastructure.Repository;

import io.github.cdimascio.dotenv.Dotenv;
import net.spotifei.Infrastructure.JDBC.JDBCRepository;

import java.sql.SQLException;

public abstract class RepositoryBase {

    private static JDBCRepository _jdbcRepository;

    /**
     * Inicializa a Database com as informações do arquivo do .env
     */
    private void startDatabase() throws SQLException {
        Dotenv dotenv = Dotenv.configure().filename("env").load();
        String databaseUrl = dotenv.get("DATABASE_URL");
        String databaseUser = dotenv.get("DATABASE_USER");
        String databasePassword = dotenv.get("DATABASE_PASSWORD");
        _jdbcRepository = new JDBCRepository(
                databaseUrl, databaseUser, databasePassword
        );
    }

    protected JDBCRepository get_jdbcRepository() throws SQLException {
        if (_jdbcRepository == null){
            startDatabase();
        }
        return _jdbcRepository;
    }

}
