package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.debug;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseConnector;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.Listener;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.Navigator;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

public class DatabaseLoginViewModel implements ViewModel, Listener, ErrorListener {
    private static final String START_CONNECTING_RESPONSE = "Connecting...";
    private final StringProperty responseStringProperty = new SimpleStringProperty();
    private final BooleanProperty responseFailureProperty = new SimpleBooleanProperty();
    private final DatabaseConnector databaseConnector;
    private final Navigator navigator;
    private volatile boolean connectingInProcess = false;

    public DatabaseLoginViewModel(DatabaseConnector databaseConnector, Navigator navigator) {
        this.databaseConnector = databaseConnector;
        this.navigator = navigator;
    }

    public void onLoginButtonClicked(String host, String login, String password) {
        if (connectingInProcess) return;
        connectingInProcess = true;
        responseStringProperty.setValue(START_CONNECTING_RESPONSE);
        responseFailureProperty.setValue(false);
        Runnable connectionTask = () -> databaseConnector.startConnectingToDatabase(host, login, password, this, this);
        new Thread(connectionTask).start();
    }

    private void onDatabaseConnected() {
        responseStringProperty.setValue("Connected!");
        responseFailureProperty.setValue(false);
        navigator.gotoDebugScreen();
        connectingInProcess = false;
    }

    private void onDatabaseConnectionFailure(String failureMessage) {
        responseStringProperty.setValue(failureMessage);
        responseFailureProperty.setValue(true);
        connectingInProcess = false;
    }

    public ObservableValue<String> getResponseProperty() {
        return responseStringProperty;
    }

    public ObservableValue<Boolean> getResponseFailureProperty() {
        return responseFailureProperty;
    }

    @Override
    public void onError(String message) {
        Platform.runLater(() -> onDatabaseConnectionFailure(message));
    }

    @Override
    public void onSuccess() {
        Platform.runLater(this::onDatabaseConnected);
    }
}
