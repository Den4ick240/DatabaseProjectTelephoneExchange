package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin;

import javafx.fxml.FXMLLoader;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.ExchangeFactory;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Person;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Subscription;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.BalanceInteractor;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.RequestInteractor;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.SubscriptionInteractor;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.*;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters.ConcatenateFilters;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.tabs.*;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class TabFactory {
    private static final String
            NEW_SUBSCRIPTION_TAB_FXML = "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/presentation/fxml/client/clientScreen/newSubscriptionTab.fxml",
            SUBSCRIPTION_TAB_FXML = "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/presentation/fxml/client/clientScreen/subscriptionTab.fxml";

    private final DatabaseController databaseController;
    private final ExecutorService executorService;
    private final ExchangeFactory exchangeFactory;
    private final ConcatenateFilters concatenateFilters;
    private Person person;

    public TabFactory(DatabaseController databaseController,
                      ExecutorService executorService,
                      ExchangeFactory exchangeFactory, ConcatenateFilters concatenateFilters) {
        this.databaseController = databaseController;
        this.executorService = executorService;
        this.exchangeFactory = exchangeFactory;
        this.concatenateFilters = concatenateFilters;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public NewSubscriptionTabView createNewSubscriptionTab() {
        return (NewSubscriptionTabView) createTab(NEW_SUBSCRIPTION_TAB_FXML, new NewSubscriptionViewModel(executorService,
                new RequestInteractor(databaseController),
                new RequestAttributesViewModel(executorService,
                        new GetExchanges(exchangeFactory, databaseController),
                        new GetStreets(new GetEntityListUsecase<>(databaseController)), new GetHouses(new GetEntityListUsecase<>(databaseController)), person, 0)),
                "Подать заявку");
    }

    public SubscriptionTabView createSubscriptionTabView(Subscription subscription) {
        return (SubscriptionTabView) createTab(SUBSCRIPTION_TAB_FXML,
                new SubscriptionTabViewModel(
                        executorService, new GetSubscribers(exchangeFactory, new GetEntityListUsecase<>(databaseController), concatenateFilters),
                        new BalanceViewModel(executorService, subscription, new BalanceInteractor(databaseController),
                        new GetEnableCallsPrice(databaseController), new ConnectCalls(databaseController)),
                        new SubscriptionInfoViewModel(subscription),
                        subscription, new SubscriptionInteractor(databaseController), new GetPhoneNumbers(databaseController, exchangeFactory, concatenateFilters),
                        new GetCities(new GetEntityListUsecase<>(databaseController))), subscription.toString());
    }

    private AbstractTab createTab(String fxmlName, ViewModel viewModel, String name) {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource(fxmlName));
        try {
            fxmlLoader.load();
            AbstractTab tabView = fxmlLoader.getController();
            tabView.setViewModel(viewModel);
            tabView.setTabName(name);
            return tabView;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
