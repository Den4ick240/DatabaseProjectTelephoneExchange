package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.*;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ObjectListener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class GetRequestsWithNumberOfCables {
    private final ExchangeFactory exchangeFactory;
    private final GetEntityListUsecase<RequestWithNumberOfCables> getEntity;
    private Integer beneficiaryFilter;

    public GetRequestsWithNumberOfCables(ExchangeFactory exchangeFactory, GetEntityListUsecase<RequestWithNumberOfCables> getEntity) {
        this.exchangeFactory = exchangeFactory;
        this.getEntity = getEntity;
    }

    public void invoke(ObjectListener<Collection<RequestWithNumberOfCables>> listener, ErrorListener errorListener) {
        getEntity.invoke(this::getItem, getQuery(), listener, errorListener);
    }

    protected String getQuery() {
        return "select * from queue_view" + (beneficiaryFilter == null ? "" : " where is_beneficiary = " + beneficiaryFilter);
    }

    protected RequestWithNumberOfCables getItem(ResultSet resultSet) throws SQLException {
        Request request = new Request(
                resultSet.getInt("request_id"),
                new Address(
                        resultSet.getString("street"),
                        resultSet.getString("house")
                ),
                resultSet.getString("apartment"),
                exchangeFactory.getExchange(resultSet),
                new Person(
                        resultSet.getString("person_id"),
                        resultSet.getString("person_name"),
                        resultSet.getString("surname"),
                        resultSet.getString("gender"),
                        resultSet.getInt("age"),
                        resultSet.getInt("is_beneficiary") == 1
                ),
                resultSet.getDate("request_date")
        );
        return new RequestWithNumberOfCables(request, resultSet.getInt("number_of_cables"), resultSet.getInt("free_cables"));
    }

    public void setBeneficiaryFilter(Integer filter) {
        beneficiaryFilter = filter;
    }
}
