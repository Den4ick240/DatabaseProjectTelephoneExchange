package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.CityExchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.DepartmentalExchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Exchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.InstitutionalExchange;

public class ExchangesInteractor extends AbstractTableInteractor<Exchange> {
    private static final String BASE_UPDATE = "UPDATE %s SET %s = '%s' WHERE exchange_id = %d";
    private static final String BASE_INSERT = "INSERT INTO %s (%s) VALUES ('%s')";
    private static final String BASE_DELETE = "DELETE FROM %s WHERE exchange_id = %d";

    public ExchangesInteractor(DatabaseController databaseController) {
        super(databaseController);
    }

    private static class Names {
        String tableName, parameterName, name;

        Names(Exchange exchange) {
            if (exchange instanceof CityExchange) {
                tableName = "CityExchanges";
                parameterName = "name";
                name = ((CityExchange) exchange).getName();
            } else if (exchange instanceof DepartmentalExchange) {
                tableName = "DepartmentalExchanges";
                parameterName = "department_name";
                name = ((DepartmentalExchange) exchange).getDepartmentName();
            } else if (exchange instanceof InstitutionalExchange) {
                tableName = "InstitutionalExchanges";
                parameterName = "institution_name";
                name = ((InstitutionalExchange) exchange).getInstitutionName();
            }
        }
    }

    @Override
    protected String getUpdateQuery(Exchange exchange) {
        Names names = new Names(exchange);
        return String.format(BASE_UPDATE, names.tableName, names.parameterName, names.name, exchange.getId());
    }

    @Override
    protected String getDeleteQuery(Exchange exchange) {
        Names names = new Names(exchange);
        return String.format(BASE_DELETE, names.tableName, exchange.getId());
    }

    @Override
    protected String getInsertQuery(Exchange exchange) {
        Names names = new Names(exchange);
        return String.format(BASE_INSERT, names.tableName, names.parameterName, names.name);
    }
}
