package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.Listener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.DatabaseInteractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserCreator {
    static public final String USER_LOGIN_PREFIX = "zh186_";
    private final DatabaseInteractor databaseInteractor;
    static private final String CREATE_USER_QUERY = "create user %s identified by %s";
    static private final String GRANT_CONNECT_QUERY = "grant create session, alter session to %s";
    static private final String GRANT_ROLE_QUERY = "grant zh186_subscriber_role to %s";


    public UserCreator(DatabaseInteractor databaseInteractor) {
        this.databaseInteractor = databaseInteractor;
    }

    public void createUser(String login, String password, Listener listener, ErrorListener errorListener) {
        for (String query : getQueries(USER_LOGIN_PREFIX + login, password)) {
            databaseInteractor.runQuery(query, listener, errorListener);
        }
    }

    private Collection<String> getQueries(String login, String password) {
        List<String> list = new ArrayList<>();
        list.add(String.format(CREATE_USER_QUERY, login, password));
        list.add(String.format(GRANT_CONNECT_QUERY, login));
        list.add(String.format(GRANT_ROLE_QUERY, login));
        return list;
    }
}
