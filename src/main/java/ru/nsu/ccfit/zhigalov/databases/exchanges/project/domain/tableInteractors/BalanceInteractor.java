package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.Consts;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.BalancePayment;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.Listener;

import java.util.Locale;

public class BalanceInteractor extends DatabaseInteractor {
    public BalanceInteractor(DatabaseController databaseController) {
        super(databaseController);
    }

    public void topUpBalance(BalancePayment payment, Listener listener, ErrorListener errorListener) {
        String paymentString = String.format(Locale.US, "%.2f", payment.getValue());
        runQuery(
                String.format("call %stop_up_balance(%d, %s)", Consts.getSchemaName(),
                        payment.getSubscription().getId(), paymentString),
                listener, errorListener);
    }
}
