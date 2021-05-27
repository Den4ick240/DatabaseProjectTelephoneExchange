package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.*;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.GetPersonById;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.GetSubscribers;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters.PersonFilter;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.Navigator;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.tabs.AbstractTab;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.tabs.NewSubscriptionTabView;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.tabs.SubscriptionTabView;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class ClientViewModel implements ViewModel {
    private final StringProperty nameProperty, surnameProperty, genderProperty, beneficiaryProperty, ageProperty, errorProperty;
    private final Navigator navigator;
    private final ExecutorService executorService;
    private final GetSubscribers getSubscribers;
    private final Property<AbstractTab> tab;
    private final ListProperty<AbstractTab> tabs;
    private final TabFactory tabFactory;
    private List<SubscriptionTabView> subscriptionTabs;

    public ObservableValue<String> getNameObservable() {
        return nameProperty;
    }

    public ObservableValue<String> getSurnameObservable() {
        return surnameProperty;
    }

    public ObservableValue<String> getAgeObservable() {
        return ageProperty;
    }

    public ObservableValue<String> getGenderObservable() {
        return genderProperty;
    }

    public ObservableValue<String> getBeneficiaryObservable() {
        return beneficiaryProperty;
    }

    public ReadOnlyListProperty<AbstractTab> tabsProperty() {
        return tabs;
    }

    public ClientViewModel(String clientId, Navigator navigator, ExecutorService executorService,
                           GetPersonById getPersonById, TabFactory tabFactory, GetSubscribers getSubscribers) {
        this(clientId, getPersonById,
                new SimpleStringProperty(),
                new SimpleStringProperty(),
                new SimpleStringProperty(),
                new SimpleStringProperty(),
                new SimpleStringProperty(),
                new SimpleStringProperty(),
                navigator, executorService,
                getSubscribers, new SimpleObjectProperty<>(),
                new SimpleListProperty<>(FXCollections.observableArrayList()), tabFactory,
                new ArrayList<>());
    }

    public ClientViewModel(String clientId,
                           GetPersonById getPersonById,
                           StringProperty nameProperty,
                           StringProperty surnameProperty,
                           StringProperty genderProperty,
                           StringProperty beneficiaryProperty,
                           StringProperty ageProperty,
                           StringProperty errorProperty,
                           Navigator navigator,
                           ExecutorService executorService,
                           GetSubscribers getSubscribers, Property<AbstractTab> tab, ListProperty<AbstractTab> tabs,
                           TabFactory tabFactory, List<SubscriptionTabView> subscriptionTabs) {
        this.nameProperty = nameProperty;
        this.surnameProperty = surnameProperty;
        this.genderProperty = genderProperty;
        this.beneficiaryProperty = beneficiaryProperty;
        this.ageProperty = ageProperty;
        this.errorProperty = errorProperty;
        this.navigator = navigator;
        this.executorService = executorService;
        this.getSubscribers = getSubscribers;
        this.tab = tab;
        this.tabs = tabs;
        this.tabFactory = tabFactory;
        this.subscriptionTabs = subscriptionTabs;
        executorService.execute(() ->
                getPersonById.invoke(clientId, this::onPersonReceived, this::onError));
        fillTabs();
    }

    public void goToLoginScreen() {
        navigator.gotoClientSignInScreen();
    }

    public void onPersonReceived(Person person) {
        Platform.runLater(() -> {
            nameProperty.set(person.getName());
            surnameProperty.set(person.getSurname());
            ageProperty.set(Integer.toString(person.getAge()));
            genderProperty.set(person.getGender());
            beneficiaryProperty.set(person.isBeneficiary() ? "да" : "нет");
            tabFactory.setPerson(person);
            getSubscribers.setFilters(Collections.singletonList(new PersonFilter(person)));
            getSubscribers();
            fillTabs();
        });
    }

    public void onError(String message) {
        Platform.runLater(() -> errorProperty.set(message));
    }

    public Property<AbstractTab> getTabProperty() {
        return tab;
    }

    public void exit() {
        navigator.gotoLoginScreen();
    }

    private void fillTabs() {
        NewSubscriptionTabView newSubscriptionTab = tabFactory.createNewSubscriptionTab();
        ObservableList<AbstractTab> newTabList = FXCollections.observableArrayList(newSubscriptionTab);
        newTabList.addAll(subscriptionTabs);
        tabs.set(newTabList);
        tab.setValue(tabs.get(tabs.size() - 1));
    }

    private void getSubscribers() {
        executorService.execute(() ->
                getSubscribers.invoke(this::onSubscriptionsReceived, this::onError));
    }

    private void onSubscriptionsReceived(Collection<Subscription> subscriptions) {
        subscriptionTabs = subscriptions.stream().map(tabFactory::createSubscriptionTabView).collect(Collectors.toList());
        Platform.runLater(this::fillTabs);
    }
}
