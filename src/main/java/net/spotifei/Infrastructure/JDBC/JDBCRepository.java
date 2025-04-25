package net.spotifei.Infrastructure.JDBC;

import java.sql.*;

public class JDBCRepository {
    private static String url;
    private static String user;
    private static String password;
    private Connection _connection;

    private Connection getConnection() throws SQLException {
        if(_connection == null || _connection.isClosed()){
            return DriverManager.getConnection(
                    url, user, password
            );
        }
        return _connection;
    }

    private ResultSet executeQuery(String queryName){

    }

    private PreparedStatement getQueryNamed(String queryName){

    }
}
