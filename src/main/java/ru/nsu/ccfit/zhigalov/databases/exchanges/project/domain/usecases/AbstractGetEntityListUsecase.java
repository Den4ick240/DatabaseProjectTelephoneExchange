package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ObjectListener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractGetEntityListUsecase<T> {
    private final GetEntityListUsecase<T> getEntity;

    protected AbstractGetEntityListUsecase(GetEntityListUsecase<T> getEntity) {
        this.getEntity = getEntity;
    }

    public void invoke(ObjectListener<Collection<T>> listener, ErrorListener errorListener) {
        getEntity.invoke(this::getItem, getQuery(), listener, errorListener);
    }

    protected abstract String getQuery();
    protected abstract T getItem(ResultSet resultSet) throws SQLException;
}
