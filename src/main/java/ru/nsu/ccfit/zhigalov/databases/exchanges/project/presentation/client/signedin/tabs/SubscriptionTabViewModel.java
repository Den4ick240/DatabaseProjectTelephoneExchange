package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.tabs;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.BalanceState;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.City;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PhoneNumber;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Subscription;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.SubscriptionInteractor;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.GetCities;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.GetPhoneNumbers;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.GetSubscribers;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters.Filter;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters.PersonFilter;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters.PhoneNumberFilter;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;

public class SubscriptionTabViewModel implements ViewModel {
    private final ExecutorService executorService;
    private final GetSubscribers getSubscribers;
    private final GetPhoneNumbers getPhoneNumbers;
    private final BalanceViewModel balanceViewModel;
    private final GetCities getCities;
    private final SubscriptionInfoViewModel subscriptionInfoViewModel;
    private final SubscriptionInteractor subscriptionInteractor;
    private Subscription subscription;
    private final StringProperty tabName;
    private final ListProperty<PhoneNumber> phoneNumbers;
    private final ListProperty<City> cities;
    private final StringProperty callLength;
    private final BooleanProperty localCallsDisabled, ldCallsDisabled;
    private final StringProperty response;

    public SubscriptionTabViewModel(ExecutorService executorService, GetSubscribers getSubscribers,
                                    BalanceViewModel balanceViewModel, SubscriptionInfoViewModel subscriptionInfoViewModel,
                                    Subscription subscription, SubscriptionInteractor subscriptionInteractor, GetPhoneNumbers getPhoneNumbers, GetCities getCities) {
        this(executorService, getSubscribers, getPhoneNumbers, balanceViewModel, getCities, subscriptionInfoViewModel, subscriptionInteractor,
                subscription, new SimpleStringProperty(), new SimpleListProperty<>(FXCollections.observableArrayList()),
                new SimpleListProperty<>(FXCollections.observableArrayList()), new SimpleStringProperty(), new SimpleBooleanProperty(),
                new SimpleBooleanProperty(), new SimpleStringProperty());
    }

    public SubscriptionTabViewModel(ExecutorService executorService, GetSubscribers getSubscribers,
                                    GetPhoneNumbers getPhoneNumbers, BalanceViewModel balanceViewModel, GetCities getCities, SubscriptionInfoViewModel subscriptionInfoViewModel,
                                    SubscriptionInteractor subscriptionInteractor, Subscription subscription,
                                    StringProperty tabName, ListProperty<PhoneNumber> phoneNumbers, ListProperty<City> cities,
                                    StringProperty callLength, BooleanProperty localCallsDisabled, BooleanProperty ldCallsDisabled, StringProperty response) {
        this.executorService = executorService;
        this.getSubscribers = getSubscribers;
        this.getPhoneNumbers = getPhoneNumbers;
        this.balanceViewModel = balanceViewModel;
        this.getCities = getCities;
        this.subscriptionInfoViewModel = subscriptionInfoViewModel;
        this.subscriptionInteractor = subscriptionInteractor;
        this.subscription = subscription;
        this.tabName = tabName;
        this.phoneNumbers = phoneNumbers;
        this.cities = cities;
        this.callLength = callLength;
        this.localCallsDisabled = localCallsDisabled;
        this.ldCallsDisabled = ldCallsDisabled;
        this.response = response;
        balanceViewModel.setUpdateSubscriptionCallback(this::updateSubscription);
        getPhoneNumbersAndCities();
        callLength.addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]*")) {
                callLength.set(oldValue);
            }
        });
        setDisabledProperties();
    }

    public ReadOnlyStringProperty tabNameProperty() {
        return tabName;
    }

    public ReadOnlyListProperty<PhoneNumber> phoneNumbersProperty() {
        return phoneNumbers;
    }

    public BalanceViewModel getBalanceViewModel() {
        return balanceViewModel;
    }

    public SubscriptionInfoViewModel getSubscriptionInfoViewModel() {
        return subscriptionInfoViewModel;
    }

    public ListProperty<City> citiesProperty() {
        return cities;
    }

    public StringProperty responseProperty() {
        return response;
    }

    public void localCall(PhoneNumber phoneNumber) {
        if (phoneNumber == null) {
            onError("Choose phone number to call");
        } else {
            executorService.execute(() ->
                    subscriptionInteractor.localCall(subscription, phoneNumber, this::updateSubscription, this::onError));
        }
    }

    public void longDistanceCall(City city) {
        if (city == null) {
            onError("Choose city to call");
        } else if (callLength.get().isEmpty()) {
            onError("Choose call length to call");
        } else {
            executorService.execute(() ->
                    subscriptionInteractor.longDistanceCall(subscription, city, Integer.parseInt(callLength.get()),
                            this::updateSubscription, this::onError));
        }
    }

    private void updateSubscription() {
//        getSubscribers.setPersonFilter(subscription.getPerson());
//        getSubscribers.setPhoneNumberFilter(subscription.getPhoneNumber());
        getSubscribers.setFilters(Arrays.asList(
                        new PersonFilter(subscription.getPerson()),
                        new PhoneNumberFilter(subscription.getPhoneNumber())));
        executorService.execute(() ->
                getSubscribers.invoke(this::onSubscriptionReceived, this::onError));
    }

    private void onSubscriptionReceived(Collection<Subscription> subscriptions) {
        Platform.runLater(() -> {
            subscription = subscriptions.iterator().next();
            balanceViewModel.setSubscription(subscription);
            subscriptionInfoViewModel.setSubscription(subscription);
            setDisabledProperties();
        });

    }

    private void setDisabledProperties() {
        localCallsDisabled.set(subscription.getState().equals(BalanceState.disabled));
        ldCallsDisabled.set(subscription.getLdState().equals(BalanceState.disabled));
    }

    private void onError(String s) {
        Platform.runLater(() -> response.set(s));
    }

    private void getPhoneNumbersAndCities() {
        executorService.execute(() -> {
            getPhoneNumbers.invoke(this::onPhoneNumbersReceived, this::onError);
            getCities.invoke(cities::setAll, this::onError);
        });
    }

    private void onPhoneNumbersReceived(Collection<PhoneNumber> phoneNumbers) {
        this.phoneNumbers.setAll(phoneNumbers);
    }

    public StringProperty callLengthProperty() {
        return callLength;
    }

    public ReadOnlyBooleanProperty localCallsDisabledProperty() {
        return localCallsDisabled;
    }

    public ReadOnlyBooleanProperty ldCallsDisabledProperty() {
        return ldCallsDisabled;
    }
}
