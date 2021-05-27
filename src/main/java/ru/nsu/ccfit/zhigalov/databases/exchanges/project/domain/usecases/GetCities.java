package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.Consts;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.City;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ObjectListener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class GetCities {
    private final GetEntityListUsecase<City> getEntity;

    public GetCities(GetEntityListUsecase<City> getEntity) {
        this.getEntity = getEntity;
    }

    public void invoke(ObjectListener<Collection<City>> listener, ErrorListener errorListener) {
        getEntity.invoke(this::getItem, getQuery(), listener, errorListener);
    }

    protected String getQuery() {
        return "select * from " + Consts.getSchemaName() + "cities";
    }

    protected City getItem(ResultSet resultSet) throws SQLException {
        return new City(resultSet.getString("name"));
    }
}
