package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.Consts;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Address;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Exchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.ExchangeFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GetExchanges extends AbstractGetEntityListUsecase<Exchange> {
    private final ExchangeFactory exchangeFactory;
    private Address addressFilter = null;

    public GetExchanges(ExchangeFactory exchangeFactory, DatabaseController databaseController) {
        super(new GetEntityListUsecase<>(databaseController));
        this.exchangeFactory = exchangeFactory;
    }

    public void setAddressFilter(Address addressFilter) {
        this.addressFilter = addressFilter;
    }

    @Override
    protected Exchange getItem(ResultSet resultSet) throws SQLException {
        return exchangeFactory.getExchange(resultSet);
    }

    @Override
    protected String getQuery() {
        if (addressFilter == null)
            return "select * from " + Consts.getSchemaName() + "exchange_view";
        return String.format(
                "select * from %sexchanges_with_addresses_view where street = '%s' and house = '%s'",
                Consts.getSchemaName(), addressFilter.getStreet(), addressFilter.getHouse()
        );
    }
}
