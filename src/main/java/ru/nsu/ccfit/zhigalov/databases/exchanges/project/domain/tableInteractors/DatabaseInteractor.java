package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.Listener;

import java.sql.SQLException;

public class DatabaseInteractor {
    private final DatabaseController databaseController;

    public DatabaseInteractor(DatabaseController databaseController) {
        this.databaseController = databaseController;
    }

    public void runQuery(String query, Listener listener, ErrorListener errorListener) {
        try {
            databaseController.executeUpdateQuery(query);
            listener.onSuccess();
        } catch (SQLException e) {
            e.printStackTrace();
            errorListener.onError(e.getMessage());
        }
    }
}
