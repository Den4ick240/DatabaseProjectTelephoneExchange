package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.Consts;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Subscription;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.Listener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.DatabaseInteractor;

public class ConnectCalls {
    private final DatabaseInteractor databaseInteractor;

    public ConnectCalls(DatabaseController databaseController) {
        this.databaseInteractor = new DatabaseInteractor(databaseController);
    }

    public void connectLocalCalls(Subscription subscription, Listener listener, ErrorListener errorListener) {
        databaseInteractor.runQuery(
                String.format("call %senable_local_calls(%d)", Consts.getSchemaName(), subscription.getId()),
                listener, errorListener);
    }

    public void connectLdCalls(Subscription subscription, Listener listener, ErrorListener errorListener) {
        databaseInteractor.runQuery(
                String.format("call %senable_ld_calls(%d)", Consts.getSchemaName(), subscription.getId()),
                listener, errorListener);
    }
}
