package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Address {
    private final String street, house;

    public Address(ResultSet resultSet) throws SQLException {
        street = resultSet.getString("street");
        house = resultSet.getString("house");
    }

    public String getStreet() {
        return street;
    }

    public String getHouse() {
        return house;
    }

    public Address(String street, String house) {
        this.street = street;
        this.house = house;
    }

    @Override
    public String toString() {
        return street + ", " + house;
    }
}
