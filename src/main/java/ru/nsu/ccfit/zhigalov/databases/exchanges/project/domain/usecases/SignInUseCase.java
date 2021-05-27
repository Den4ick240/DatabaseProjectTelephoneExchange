package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseConnector;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.Listener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.DatabaseInteractor;

public class SignInUseCase {
    private static final String SCHEMA_QUERY = "alter session set current_schema = \"18206_ZHIGALOV\"";
    private final DatabaseConnector databaseConnector;
    private final DatabaseInteractor databaseInteractor;
    private boolean errorOccurred = false;

    public SignInUseCase(DatabaseConnector databaseConnector, DatabaseInteractor databaseInteractor) {
        this.databaseConnector = databaseConnector;
        this.databaseInteractor = databaseInteractor;
    }

    public void signIn(String login, String password, Listener listener, ErrorListener errorListener) {
        String pLogin = UserCreator.USER_LOGIN_PREFIX + login;
        databaseConnector.changeUser(pLogin, password, () -> {
        }, message -> {
            errorOccurred = true;
            errorListener.onError(message);
        });
        if (errorOccurred) return;
        databaseInteractor.runQuery(SCHEMA_QUERY, listener, errorListener);
    }
}
