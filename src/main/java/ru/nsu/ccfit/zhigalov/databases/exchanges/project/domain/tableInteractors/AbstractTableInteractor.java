package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.Listener;

public abstract class AbstractTableInteractor<T> extends DatabaseInteractor {
    public AbstractTableInteractor(DatabaseController databaseController) {
        super(databaseController);
    }

    public void insert(T object, Listener listener, ErrorListener errorListener) {
        runQuery(getInsertQuery(object), listener, errorListener);
    }

    public void delete(T object, Listener listener, ErrorListener errorListener) {
        runQuery(getDeleteQuery(object), listener, errorListener);
    }

    public void update(T object, Listener listener, ErrorListener errorListener) {
        runQuery(getUpdateQuery(object), listener, errorListener);
    }

    protected abstract String getUpdateQuery(T object);

    protected abstract String getDeleteQuery(T object);

    protected abstract String getInsertQuery(T object);
}
