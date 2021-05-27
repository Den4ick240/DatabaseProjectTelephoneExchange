package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Address;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ObjectListener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class GetAddresses {
    private final GetEntityListUsecase<Address> getEntity;
    public GetAddresses(GetEntityListUsecase<Address> getEntity) {
        this.getEntity = getEntity;
    }

    public void invoke(ObjectListener<Collection<Address>> listener, ErrorListener errorListener) {
        getEntity.invoke(this::getItem, "Select * from addresses", listener, errorListener);
    }
    private Address getItem(ResultSet resultSet) throws SQLException {
        return new Address(
                resultSet.getString("street"),
                resultSet.getString("house"));
    }
}
