package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.Consts;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ObjectListener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class GetHouses {
    private final GetEntityListUsecase<String> getEntity;

    public GetHouses(GetEntityListUsecase<String> getEntity) {
        this.getEntity = getEntity;
    }

    public void invoke(String street, ObjectListener<Collection<String>> listener, ErrorListener errorListener) {
        getEntity.invoke(this::getItem, getQuery(street), listener, errorListener);
    }

    protected String getQuery(String street) {
        return "select house from " + Consts.getSchemaName() + "addresses where street = '" + street + "'";
    }

    protected String getItem(ResultSet resultSet) throws SQLException {
        return resultSet.getString("house");
    }
}
