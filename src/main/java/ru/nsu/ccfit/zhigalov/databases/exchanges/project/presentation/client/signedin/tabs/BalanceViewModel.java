package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.tabs;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.BalancePayment;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.BalanceState;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Subscription;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.BalanceInteractor;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.ConnectCalls;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.GetEnableCallsPrice;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

import java.util.concurrent.ExecutorService;

public class BalanceViewModel implements ViewModel {
    private final ExecutorService executorService;
    private final GetEnableCallsPrice getEnableCallsPrice;
    private final ConnectCalls connectCalls;
    private Subscription subscription;
    private final BalanceInteractor balanceInteractor;
    private final StringProperty value, balance, connectLocalCallsPrice, connectLdCallsPrice;
    private BalancePayment payment = null;
    private final StringProperty response;
    private final BooleanProperty localCallsWarningVisible, ldCallsWarningVisible, connectLocalCallsVisible, connectLdCallsVisible;
    private Runnable updateSubscription = () -> {
    };

    public BalanceViewModel(ExecutorService executorService, Subscription subscription, BalanceInteractor balanceInteractor, GetEnableCallsPrice getEnableCallsPrice, ConnectCalls connectCalls) {
        this(executorService, getEnableCallsPrice, connectCalls, subscription, balanceInteractor, new SimpleStringProperty(), new SimpleStringProperty(),
                new SimpleStringProperty(), new SimpleStringProperty(),
                new SimpleStringProperty(), new SimpleBooleanProperty(), new SimpleBooleanProperty(),
                new SimpleBooleanProperty(), new SimpleBooleanProperty());
    }

    public BalanceViewModel(ExecutorService executorService, GetEnableCallsPrice getEnableCallsPrice, ConnectCalls connectCalls, Subscription subscription,
                            BalanceInteractor balanceInteractor, StringProperty value,
                            StringProperty balance, StringProperty connectLocalCallsPrice, StringProperty connectLdCallsPrice, StringProperty response, BooleanProperty localCallsWarningVisible,
                            BooleanProperty connectLdCallsVisible, BooleanProperty ldCallsWarningVisible,
                            BooleanProperty connectLocalCallsVisible) {
        this.executorService = executorService;
        this.getEnableCallsPrice = getEnableCallsPrice;
        this.connectCalls = connectCalls;
        this.subscription = subscription;
        this.balanceInteractor = balanceInteractor;
        this.value = value;
        this.balance = balance;
        this.connectLocalCallsPrice = connectLocalCallsPrice;
        this.connectLdCallsPrice = connectLdCallsPrice;
        this.response = response;
        this.localCallsWarningVisible = localCallsWarningVisible;
        this.connectLdCallsVisible = connectLdCallsVisible;
        this.ldCallsWarningVisible = ldCallsWarningVisible;
        this.connectLocalCallsVisible = connectLocalCallsVisible;

        updateBalance();
        setVisibleProperties();
        getPrices();
        value.addListener(this::onValueChanged);
    }

    private void getPrices() {
        executorService.execute(() -> {
            getEnableCallsPrice.getLocalCallsEnablePrice(subscription, price ->
                    Platform.runLater(() -> connectLocalCallsPrice.set(price.toString())), this::onError);
            getEnableCallsPrice.getLdCallsEnablePrice(subscription, price ->
                    Platform.runLater(() -> connectLdCallsPrice.set(price.toString())), this::onError);
        });
    }

    public void setUpdateSubscriptionCallback(Runnable updateSubscription) {
        this.updateSubscription = updateSubscription;
    }

    public void topUpBalance() {
        if (payment == null) return;
        executorService.execute(() ->
                balanceInteractor.topUpBalance(payment, this::onSuccess, this::onError));
    }

    public StringProperty responseProperty() {
        return response;
    }

    public StringProperty balanceProperty() {
        return balance;
    }

    public StringProperty valueProperty() {
        return value;
    }

    private void updateBalance() {
        balance.set(subscription.getBalance().toString());
    }

    private void onValueChanged(Observable observable, String oldValue, String newValue) {
        if (!newValue.matches("[0-9]*[.]?[0-9]?[0-9]?")) {
            value.set(oldValue);
        } else if (newValue.isEmpty()) {
            payment = null;
        } else {
            payment = new BalancePayment(Double.parseDouble(newValue), subscription);
        }
    }

    private void onSuccess() {
        Platform.runLater(updateSubscription);
    }

    private void onError(String message) {
        Platform.runLater(() -> response.set(message));
    }

    private void setVisibleProperties() {
        ldCallsWarningVisible.set(subscription.getLdState().equals(BalanceState.expired));
        localCallsWarningVisible.set(subscription.getState().equals(BalanceState.expired));

        boolean localCallsDisabled = subscription.getState().equals(BalanceState.disabled);
        connectLdCallsVisible.set(!localCallsDisabled && subscription.getLdState().equals(BalanceState.disabled));
        connectLocalCallsVisible.set(localCallsDisabled);
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
        updateBalance();
        setVisibleProperties();
    }

    public ReadOnlyBooleanProperty localCallsWarningVisibleProperty() {
        return localCallsWarningVisible;
    }

    public ReadOnlyBooleanProperty ldCallsWarningVisibleProperty() {
        return ldCallsWarningVisible;
    }

    public ReadOnlyBooleanProperty connectLocalCallsVisibleProperty() {
        return connectLocalCallsVisible;
    }

    public ReadOnlyBooleanProperty connectLdCallsVisibleProperty() {
        return connectLdCallsVisible;
    }

    public ReadOnlyStringProperty enableLocalCallsPriceProperty() {
        return connectLocalCallsPrice;
    }

    public ReadOnlyStringProperty enableLdCallsPriceProperty() {
        return connectLdCallsPrice;
    }

    public void connectLocalCalls() {
        executorService.execute(() ->
                connectCalls.connectLocalCalls(subscription, updateSubscription::run, this::onError));
    }

    public void connectLdCalls() {
        executorService.execute(() ->
                connectCalls.connectLdCalls(subscription, updateSubscription::run, this::onError));
    }
}
