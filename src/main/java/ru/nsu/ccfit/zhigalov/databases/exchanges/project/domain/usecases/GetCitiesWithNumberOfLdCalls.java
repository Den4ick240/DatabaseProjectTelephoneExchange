package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.City;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.CityWithNumberOfLdCalls;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GetCitiesWithNumberOfLdCalls extends AbstractGetEntityListUsecase<CityWithNumberOfLdCalls> {

    public GetCitiesWithNumberOfLdCalls(GetEntityListUsecase<CityWithNumberOfLdCalls> getEntity) {
        super(getEntity);
    }

    @Override
    protected String getQuery() {
        return "select * from cities_with_number_of_calls";
    }

    @Override
    protected CityWithNumberOfLdCalls getItem(ResultSet resultSet) throws SQLException {
        return new CityWithNumberOfLdCalls(new City(resultSet.getString("name")),
                resultSet.getInt("number_of_calls"));
    }
}
