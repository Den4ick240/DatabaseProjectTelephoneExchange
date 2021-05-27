package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PhoneNumber;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PhoneType;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Request;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.Listener;

import java.sql.SQLException;

public class AssignNumberToRequest {
    private final DatabaseController databaseController;

    public AssignNumberToRequest(DatabaseController databaseController) {
        this.databaseController = databaseController;
    }

    public void invoke(Request request, PhoneNumber phoneNumber, PhoneType newPhoneType, Listener listener, ErrorListener errorListener) {
        try {
            databaseController.executeUpdateQuery(
                    String.format(
                            "call assign_number_to_client(%d, " +
                                    "(select number_id from phoneNumbers where number = '%s' and exchange_id = %d), " +
                                    "'%s')", //TODO:PHONETYPE
                            request.getId(), phoneNumber.getNumber(), phoneNumber.getExchange().getId(),
                            newPhoneType.toString()
                    ));
            listener.onSuccess();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            errorListener.onError(throwables.getMessage());
        }
    }
}
