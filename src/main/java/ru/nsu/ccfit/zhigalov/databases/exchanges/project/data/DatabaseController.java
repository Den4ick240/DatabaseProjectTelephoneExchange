package ru.nsu.ccfit.zhigalov.databases.exchanges.project.data;

import java.sql.*;

public class DatabaseController {
    private Connection connection;
    private Statement statement;
    private String currentHost;
    private static final String FAILURE_MESSAGE = "Failed to make connection";
    public int connect(String host, String login, String password) throws SQLException {
            if (connection != null) {
                close();
            }
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@" + host + ":1521:XE", login, password);

            currentHost = host;
            if (connection != null) {
                System.out.println("Connected to the database!");
                statement = connection.createStatement();
                return 0;
            } else {
                System.out.println(FAILURE_MESSAGE);
                return -1;
            }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet executeQuery(String query) throws SQLException {
        return statement.executeQuery(query);
    }

    public int executeUpdateQuery(String query) throws SQLException {
        return statement.executeUpdate(query);
    }

    public Connection getConnection() {
        return connection;
    }

    public String getCurrentHost() {
        return currentHost;
    }
}

