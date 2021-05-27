package ru.nsu.ccfit.zhigalov.databases.exchanges.project.data;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.Listener;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public class DatabaseConnector {
    private final ExecutorService executorService;
    private final DatabaseController databaseController;

    public DatabaseConnector(ExecutorService executorService,
                             DatabaseController databaseController) {
        this.executorService = executorService;
        this.databaseController = databaseController;
    }

    public void startConnectingToDatabase(String host,
                                          String login,
                                          String password,
                                          Listener connectionListener,
                                          ErrorListener errorListener) {
        executorService.execute(() -> {
            try {
                if (databaseController.connect(host, login, password) == 0) {
                    connectionListener.onSuccess();
                } else {
                    errorListener.onError("Failed to connect");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                errorListener.onError(e.getMessage());
            }
        });
    }

    public void changeUser(String login, String password, Listener listener, ErrorListener errorListener) {
        startConnectingToDatabase(databaseController.getCurrentHost(), login, password, listener, errorListener);
    }
}
