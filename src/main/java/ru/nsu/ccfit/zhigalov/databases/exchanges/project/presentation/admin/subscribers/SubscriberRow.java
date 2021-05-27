package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.subscribers;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Address;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.BalanceState;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Subscription;

public class SubscriberRow {
    private final Subscription subscription;

    public SubscriberRow(Subscription subscription) {
        this.subscription = subscription;
    }

    public String getId() {
        return subscription.getId().toString();
    }

    public String getSurname() {
        return subscription.getPerson().getSurname();
    }
    public String getName() {
        return subscription.getPerson().getName();
    }
    public String getAddress() {
        Address a = subscription.getAddress();
        return "ул." + a.getStreet() + " д." + a.getHouse() + " кв." + subscription.getApartment();
    }
    public String getExchange() {
        return subscription.getPhoneNumber().getExchange().toString();
    }
    public String getPhoneNumber() {
        return subscription.getPhoneNumber().getNumber() + " " + subscription.getPhoneNumber().getType().russianString();
    }
    public String getBalance() {
        return subscription.getBalance().toString();
    }
    public String getState() {
        BalanceState s = subscription.getState();
        if (s.equals(BalanceState.expired)) return "Просрочено с " + subscription.getExpiredDate().toString();
        return s.toRussianString();
    }
    public String getLdState() {
        BalanceState s = subscription.getLdState();
        if (s.equals(BalanceState.expired)) return "Просрочено с " + subscription.getLdExpiredDate().toString();
        return s.toRussianString();
    }
}
