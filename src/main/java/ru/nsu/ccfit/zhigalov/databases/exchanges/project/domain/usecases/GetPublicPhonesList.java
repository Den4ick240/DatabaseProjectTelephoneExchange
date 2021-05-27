package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.*;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ObjectListener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class GetPublicPhonesList {
    private final ExchangeFactory exchangeFactory;
    private String streetFilter = null;
    private Exchange exchangeFilter = null;
    private final GetEntityListUsecase<PublicPhone> getEntity;

    public GetPublicPhonesList(ExchangeFactory exchangeFactory, GetEntityListUsecase<PublicPhone> getEntity) {
        this.exchangeFactory = exchangeFactory;
        this.getEntity = getEntity;
    }

    public void invoke(ObjectListener<Collection<PublicPhone>> listener, ErrorListener errorListener) {
        getEntity.invoke(this::getItem, getQuery(), listener, errorListener);
    }

    public void setStreetFilter(String streetFilter) {
        this.streetFilter = streetFilter;
    }

    public void setExchangeFilter(Exchange exchangeFilter) {
        this.exchangeFilter = exchangeFilter;
    }

    protected String getQuery() {
        return "select * from public_phones_view " +
                (streetFilter == null && exchangeFilter == null ? "" : "where ") +
                (streetFilter == null ? "" : "street = '" + streetFilter + "' ") +
                (streetFilter != null && exchangeFilter != null ? "and " : "") +
                (exchangeFilter == null ? "" : "exchange_id = " + exchangeFilter.getId());
    }

    protected PublicPhone getItem(ResultSet resultSet) throws SQLException {
        Address address = new Address(resultSet.getString("street"),
                resultSet.getString("house"));
        PhoneNumber phone_number = new PhoneNumber(resultSet.getString("phone_number"),
                PhoneType.PUBLIC, exchangeFactory.getExchange(resultSet));
        return new PublicPhone(address, phone_number, resultSet.getInt("phone_id"));
    }
}
