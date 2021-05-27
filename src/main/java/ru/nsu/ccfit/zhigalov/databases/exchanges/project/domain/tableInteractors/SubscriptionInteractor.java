package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.Consts;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.City;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PhoneNumber;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Subscription;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.Listener;

public class SubscriptionInteractor extends DatabaseInteractor {

    public SubscriptionInteractor(DatabaseController databaseController) {
        super(databaseController);
    }

    public void localCall(Subscription subscription, PhoneNumber receivingNumber, Listener listener, ErrorListener errorListener) {
        runQuery(
                String.format("call %slocal_call(%d, '%s')", Consts.getSchemaName(),
                        subscription.getId(), receivingNumber.getNumber()),
                listener, errorListener
        );
    }

    public void longDistanceCall(Subscription subscription, City city, int lengthMinutes, Listener listener, ErrorListener errorListener) {
        runQuery(
                String.format("call %slong_distance_call(%d, '%s', %d)", Consts.getSchemaName(),
                        subscription.getId(), city.getName(), lengthMinutes),
                listener, errorListener
        );
    }
}
