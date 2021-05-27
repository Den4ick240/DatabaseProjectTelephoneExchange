package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.Consts;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.*;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ObjectListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters.ConcatenateFilters;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters.Filter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

public class GetSubscribers {
    private static final String QUERY_BASE = "select * from " + Consts.getSchemaName() + "subscribers_view";
    private final ExchangeFactory exchangeFactory;
    private final GetEntityListUsecase<Subscription> getEntity;
    private final ConcatenateFilters concatenateFilters;
    private String query;



    public GetSubscribers(ExchangeFactory exchangeFactory, GetEntityListUsecase<Subscription> getEntity, ConcatenateFilters concatenateFilters) {
        this.exchangeFactory = exchangeFactory;
        this.getEntity = getEntity;
        this.concatenateFilters = concatenateFilters;
    }

    public void setFilters(Collection<Filter> filters) {
        StringBuilder query = new StringBuilder(QUERY_BASE);
        if (!filters.isEmpty())
            query.append(" where ");
        for (Iterator<Filter> it = filters.iterator(); it.hasNext();) {
            query.append(it.next().getSqlWhereClause());
            if (it.hasNext())
                query.append(" and ");
        } //TODO:replace with concantenate filters
        this.query = query.toString();
    }

    public void invoke(ObjectListener<Collection<Subscription>> listener, ErrorListener errorListener) {
        getEntity.invoke(this::getItem, getQuery(), listener, errorListener);
    }

    protected String getQuery() {
        return this.query;
    }

    protected Subscription getItem(ResultSet resultSet) throws SQLException {
        return new Subscription(resultSet, exchangeFactory.getExchange(resultSet));
    }
}
