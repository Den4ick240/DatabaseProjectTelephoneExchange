package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.Consts;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ObjectListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Exchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.ExchangeFactory;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PhoneNumber;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PhoneType;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters.ConcatenateFilters;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters.ExchangeFilter;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters.Filter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class GetPhoneNumbers extends AbstractGetEntityListUsecase<PhoneNumber> {
    private static final String QUERY_BASE =
            "select phone_number, type, exchange_id, name, department_name, institution_name from " + Consts.getSchemaName()
                    + "phone_numbers_view";

    private String query;
    private final ExchangeFactory exchangeFactory;
    private Collection<Filter> filters = new ArrayList<>();
    private final ConcatenateFilters concatenateFilters;

    public GetPhoneNumbers(DatabaseController databaseController, ExchangeFactory exchangeFactory, ConcatenateFilters concatenateFilters) {
        super(new GetEntityListUsecase<>(databaseController));
        this.exchangeFactory = exchangeFactory;
        this.concatenateFilters = concatenateFilters;
    }

    @Override
    protected String getQuery() {
        return query;
    }

    @Override
    protected PhoneNumber getItem(ResultSet resultSet) throws SQLException {
        return new PhoneNumber(
                resultSet.getString("phone_number"),
                PhoneType.valueOf(resultSet.getString("type")),
                exchangeFactory.getExchange(resultSet));
    }

    public void invoke(ObjectListener<Collection<PhoneNumber>> listener, ErrorListener errorListener, Exchange exchange) {
        Collection<Filter> filters = new ArrayList<>(this.filters);
        filters.add(new ExchangeFilter(exchange));
        query = QUERY_BASE + concatenateFilters.concatenate(filters);
        super.invoke(listener, errorListener);
    }

    @Override
    public void invoke(ObjectListener<Collection<PhoneNumber>> listener, ErrorListener errorListener) {
        query = QUERY_BASE + concatenateFilters.concatenate(filters);
        super.invoke(listener, errorListener);
    }

    public void setFilters(Collection<Filter> filters) {
        this.filters = filters;
    }
}
