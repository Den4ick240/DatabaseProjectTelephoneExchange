package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.Consts;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PhoneNumber;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PhoneType;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Request;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.Listener;

public class RequestInteractor extends AbstractTableInteractor<Request> {
    public RequestInteractor(DatabaseController databaseController) {
        super(databaseController);
    }

    @Override
    protected String getUpdateQuery(Request object) {
        return String.format(
                "update %squeue set " +
                        "exchange_id = %d " +
                        "address_id = (select address_id from %saddresses where street = '%s' and house = '%s') " +
                        "apartment = '%s' " +
                        "where request_id = %d",
                Consts.getSchemaName(), object.getExchange().getId(), Consts.getSchemaName(),
                object.getAddress().getStreet(),
                object.getAddress().getHouse(), object.getApartment(), object.getId());
    }

    @Override
    protected String getDeleteQuery(Request object) {
        return String.format(
                "delete from %squeue where request_id = %d", Consts.getSchemaName(), object.getId());
    }

    @Override
    protected String getInsertQuery(Request object) {
        return String.format(
                "insert into %squeue (exchange_id, address_id, apartment, person_id) " +
                        "values(%d,(select address_id from %saddresses where house = '%s' and street = '%s')," +
                        "'%s', '%s')",
                Consts.getSchemaName(), object.getExchange().getId(), Consts.getSchemaName(), object.getAddress().getHouse(),
                object.getAddress().getStreet(), object.getApartment(), object.getPerson().getId());
    }

    public void assignNumberToRequest(Request request, PhoneNumber phoneNumber, PhoneType phoneType, Listener listener, ErrorListener errorListener) {
        runQuery(String.format(
                "call assign_number_to_client(%d, " +
                        "'%s', %d," +
                        "'%s')",
                request.getId(), phoneNumber.getNumber(), phoneNumber.getExchange().getId(), phoneType.toString()
        ), listener, errorListener);
    }
}
