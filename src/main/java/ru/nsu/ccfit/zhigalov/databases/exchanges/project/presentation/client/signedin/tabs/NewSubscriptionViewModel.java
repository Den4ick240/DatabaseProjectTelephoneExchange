package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.tabs;

import javafx.application.Platform;
import javafx.beans.property.*;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.RequestInteractor;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

import java.util.concurrent.ExecutorService;

public class NewSubscriptionViewModel implements ViewModel {
    private final ExecutorService executorService;
    private final StringProperty response;
    private final RequestInteractor requestInteractor;
    private final RequestAttributesViewModel requestAttributesViewModel;

    public NewSubscriptionViewModel(ExecutorService executorService,
                                    RequestInteractor requestInteractor, RequestAttributesViewModel requestAttributesViewModel) {
        this(executorService, requestInteractor, requestAttributesViewModel,
                new SimpleStringProperty());
    }

    public NewSubscriptionViewModel(ExecutorService executorService, RequestInteractor requestInteractor,
                                    RequestAttributesViewModel requestAttributesViewModel, StringProperty response) {
        this.executorService = executorService;
        this.requestAttributesViewModel = requestAttributesViewModel;
        this.requestInteractor = requestInteractor;
        this.response = response;
    }

    public void createRequest() {
        executorService.execute(() ->
                requestInteractor.insert(
                        requestAttributesViewModel.getSelectedRequest(),
                        () -> Platform.runLater(this::onRequestCreated),
                        this::setResponse
                ));
    }

    private void onRequestCreated() {
        setResponse("Success!");
    }

    private void setResponse(String message) {
        Platform.runLater(() -> response.set(message));
    }

    public ReadOnlyStringProperty responseProperty() {
        return response;
    }

    public ViewModel getRequestAttributesViewModel() {
        return requestAttributesViewModel;
    }
}
