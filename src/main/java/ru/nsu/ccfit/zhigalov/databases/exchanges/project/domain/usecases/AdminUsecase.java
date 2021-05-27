package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.Listener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.DatabaseInteractor;

public class AdminUsecase {
    private final DatabaseInteractor databaseInteractor;

    public AdminUsecase(DatabaseInteractor databaseInteractor) {
        this.databaseInteractor = databaseInteractor;
    }

    public void chargeSubscriptionFee(Listener listener, ErrorListener errorListener) {
        databaseInteractor.runQuery("call charge_subscription_fee()", listener, errorListener);
    }

    public void disableExpiredClients(Listener listener, ErrorListener errorListener) {
        databaseInteractor.runQuery("call disable_expired_clients()", listener, errorListener);
    }
}
