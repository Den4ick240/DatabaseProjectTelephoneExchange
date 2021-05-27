package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.Consts;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseConnector;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.Listener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.SignInUseCase;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.Navigator;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

import java.util.concurrent.ExecutorService;

public class SignInViewModel implements ViewModel, Listener, ErrorListener {
    private final StringProperty responseStringProperty;
    private final ExecutorService executorService;
    private final SignInUseCase signInUseCase;
    private final DatabaseConnector databaseConnector;
    private final Navigator navigator;
    private final BooleanProperty loading, loginDisabled;

    public SignInViewModel(StringProperty responseStringProperty, ExecutorService executorService,
                           Navigator navigator, SignInUseCase signInUseCase, DatabaseConnector databaseConnector) {
        this(responseStringProperty, executorService, signInUseCase, databaseConnector, navigator, new SimpleBooleanProperty(), new SimpleBooleanProperty());

    }

    public SignInViewModel(StringProperty responseStringProperty, ExecutorService executorService,
                           SignInUseCase signInUseCase, DatabaseConnector databaseConnector, Navigator navigator, BooleanProperty loading, BooleanProperty loginDisabled) {
        this.responseStringProperty = responseStringProperty;
        this.executorService = executorService;
        this.signInUseCase = signInUseCase;
        this.databaseConnector = databaseConnector;
        this.navigator = navigator;
        this.loading = loading;
        this.loginDisabled = loginDisabled;
        loading.set(true);
        loginDisabled.set(true);
        databaseConnector.changeUser(Consts.getLogin(), Consts.getPassword(), this, this);
    }

    public ObservableValue<String> getResponseStringProperty() {
        return responseStringProperty;
    }

    public void signUp() {
        navigator.gotoClientSignUpScreen();
    }

    public void signIn(String login, String password) {
        Runnable task = () -> {
            signInUseCase.signIn(login, password, () -> onSignedIn(login), this);
        };
        executorService.execute(task);
    }

    private void onSignedIn(String login) {
        Platform.runLater(() ->
                navigator.gotoClientScreen(login));
    }

    public void exit() {
        navigator.gotoLoginScreen();
    }

    public ReadOnlyBooleanProperty getLoadingProperty() {
        return loading;
    }

    @Override
    public void onError(String message) {
        responseStringProperty.set(message);
    }

    @Override
    public void onSuccess() {
        loading.set(false);
        loginDisabled.set(false);
    }

    public ObservableValue<Boolean> getLoginDisabledProperty() {
        return loginDisabled;
    }
}
