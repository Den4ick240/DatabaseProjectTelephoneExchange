package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.Consts;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ObjectListener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class GetStreets {
    private final GetEntityListUsecase<String> getEntity;
    public GetStreets(GetEntityListUsecase<String> getEntity) {
        this.getEntity = getEntity;
    }

    public void invoke(ObjectListener<Collection<String>> listener, ErrorListener errorListener) {
        getEntity.invoke(this::getItem, getQuery(), listener, errorListener);
    }

    protected String getQuery() {
        return "Select distinct street from " + Consts.getSchemaName() + "addresses";
    }

    protected String getItem(ResultSet resultSet) throws SQLException {
        return resultSet.getString("street");
    }
}
