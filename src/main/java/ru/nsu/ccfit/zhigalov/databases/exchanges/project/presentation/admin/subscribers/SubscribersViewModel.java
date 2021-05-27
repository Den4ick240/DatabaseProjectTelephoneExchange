package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.subscribers;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Exchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Subscription;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.GetExchanges;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.GetSubscribers;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters.ExchangeFilter;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class SubscribersViewModel implements ViewModel {
    private final ExecutorService executorService;
    private final Runnable goBack;
    private final GetSubscribers getSubscribers;
    private final GetExchanges getExchanges;
    private final ListProperty<SubscriberRow> subscriberRows;
    private final ListProperty<Exchange> exchanges;
    private final StringProperty response;
    private final Property<Exchange> selectedExchange;

    public SubscribersViewModel(Runnable goBack, ExecutorService executorService, GetSubscribers getSubscribers, GetExchanges getExchanges) {
        this(executorService, goBack, getSubscribers, getExchanges, new SimpleListProperty<>(FXCollections.observableArrayList()),
                new SimpleListProperty<>(FXCollections.observableArrayList()), new SimpleStringProperty(), new SimpleObjectProperty<>());
    }

    public SubscribersViewModel(ExecutorService executorService, Runnable goBack, GetSubscribers getSubscribers,
                                GetExchanges getExchanges,
                                ListProperty<SubscriberRow> subscriberRows, ListProperty<Exchange> exchanges,
                                StringProperty response, Property<Exchange> selectedExchange) {
        this.executorService = executorService;
        this.goBack = goBack;
        this.getSubscribers = getSubscribers;
        this.getExchanges = getExchanges;
        this.subscriberRows = subscriberRows;
        this.exchanges = exchanges;
        this.response = response;
        this.selectedExchange = selectedExchange;
        getExchanges();
        selectedExchange.addListener(this::onExchangeSelected);
    }

    private void onExchangeSelected(Observable observable, Exchange oldValue, Exchange newValue) {
//        getSubscribers.setExchangeFilter(newValue);
        getSubscribers.setFilters(Collections.singleton(new ExchangeFilter(newValue)));
        getSubscribers();
    }

    public ReadOnlyListProperty<SubscriberRow> subscriberRowsProperty() {
        return subscriberRows;
    }

    public ReadOnlyListProperty<Exchange> exchangesProperty() {
        return exchanges;
    }

    public Property<Exchange> selectedExchangeProperty() {
        return selectedExchange;
    }

    public void goBack() {
        goBack.run();
    }

    private void getSubscribers() {
        executorService.execute(() ->
                getSubscribers.invoke(this::onSubscribersReceived, this::onError));
    }

    private void onError(String s) {
        Platform.runLater(() -> response.set(s));
    }

    private void onSubscribersReceived(Collection<Subscription> subscriptions) {
        Platform.runLater(() ->
                subscriberRows.setAll(subscriptions.stream().map(SubscriberRow::new).collect(Collectors.toList())));
    }

    private void getExchanges() {
        executorService.execute(() ->
                getExchanges.invoke(exchanges::setAll, this::onError));
    }
}
