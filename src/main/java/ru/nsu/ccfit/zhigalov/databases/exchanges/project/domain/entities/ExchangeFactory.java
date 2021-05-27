package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeFactory {
    public Exchange getExchange(int exchangeId, String name, String departmentName, String institutionName) {
        if (name != null) return new CityExchange(exchangeId, name);
        if (departmentName != null) return new DepartmentalExchange(exchangeId, departmentName);
        if (institutionName != null) return new InstitutionalExchange(exchangeId, institutionName);
        return new Exchange(exchangeId);
    }

    public Exchange getExchange(ResultSet resultSet) throws SQLException {
        return getExchange(resultSet.getInt("exchange_id"), resultSet.getString("name"),
                resultSet.getString("department_name"), resultSet.getString("institution_name"));
    }
}
