package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.AdminUsecase;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.Navigator;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

import java.util.concurrent.ExecutorService;

public class AdminViewModel implements ViewModel {
    private final Navigator navigator;
    private final ExecutorService executorService;
    private final AdminUsecase adminUsecase;
    private final StringProperty response;

    public AdminViewModel(Navigator navigator, ExecutorService executorService, AdminUsecase adminUsecase) {
        this(navigator, executorService, adminUsecase, new SimpleStringProperty());
    }

    public AdminViewModel(Navigator navigator, ExecutorService executorService, AdminUsecase adminUsecase, StringProperty response) {
        this.navigator = navigator;
        this.executorService = executorService;
        this.adminUsecase = adminUsecase;
        this.response = response;
    }

    public ReadOnlyStringProperty responseProperty() {
        return response;
    }

    public void onPublicPhonesClicked() {
        navigator.gotoPublicPhonesScreen();
    }

    public void onExchangesClicked() {
        navigator.gotoExchangesScreen();
    }

    public void onQueueClicked() {
        navigator.gotoQueueScreen();
    }

    public void exit() {
        navigator.gotoLoginScreen();
    }

    public void chargeSubscriptionFee() {
        executorService.execute(() ->
                adminUsecase.chargeSubscriptionFee(() -> setResponse("Абонентская плата взята!"), this::setResponse));
    }

    public void disableExpiredClientsButton() {
        executorService.execute(() ->
                adminUsecase.disableExpiredClients(() -> setResponse("Услуги отключены!"), this::setResponse));
    }

    private void setResponse(String message) {
        Platform.runLater(() -> response.setValue(message));
    }

    public void onSubscribersClicked() {
        navigator.gotoSubscribersScreen();
    }

    public void onCitiesButtonClicked() {
        navigator.gotoCitiesScreen();
    }
}
