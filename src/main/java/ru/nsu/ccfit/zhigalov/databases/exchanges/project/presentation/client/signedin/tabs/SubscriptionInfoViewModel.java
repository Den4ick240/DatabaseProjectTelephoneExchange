package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.tabs;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Subscription;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

public class SubscriptionInfoViewModel implements ViewModel {
    private Subscription subscription;
    private final StringProperty exchange, phoneNumber, address, subscriptionId;



    public SubscriptionInfoViewModel(Subscription subscription) {
        this(subscription,
                new SimpleStringProperty(),
                new SimpleStringProperty(),
                new SimpleStringProperty(),
                new SimpleStringProperty());
    }

    public SubscriptionInfoViewModel(Subscription subscription,
                                     StringProperty exchange, StringProperty phoneNumber,
                                     StringProperty address, StringProperty subscriptionId) {
        this.subscription = subscription;
        this.exchange = exchange;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.subscriptionId = subscriptionId;

        setData();
    }

    public ObservableStringValue exchangeProperty() {
        return exchange;
    }

    public ObservableStringValue phoneNumberProperty() {
        return phoneNumber;
    }

    public ObservableStringValue addressProperty() {
        return address;
    }

    public ObservableStringValue subscriptionIdProperty() {
        return subscriptionId;
    }


    private void setData() {
        String exchange = subscription.getPhoneNumber().getExchange().toString();
        String address = subscription.getAddress().toString() + ", кв. " + subscription.getApartment();
        String phoneNumber = subscription.getPhoneNumber().getNumber();
        String subscriptionId = subscription.getId().toString();
        this.exchange.set(exchange);
        this.address.set(address);
        this.phoneNumber.set(phoneNumber);
        this.subscriptionId.set(subscriptionId);
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
        setData();
    }
}
