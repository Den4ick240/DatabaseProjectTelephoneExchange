package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GetGenders extends AbstractGetEntityListUsecase<String> {
    public GetGenders(DatabaseController databaseController) {
        super(new GetEntityListUsecase<>(databaseController));
    }

    @Override
    protected String getQuery() {
        return "select gender from genders";
    }

    @Override
    protected String getItem(ResultSet resultSet) throws SQLException {
        return resultSet.getString("gender");
    }
}
