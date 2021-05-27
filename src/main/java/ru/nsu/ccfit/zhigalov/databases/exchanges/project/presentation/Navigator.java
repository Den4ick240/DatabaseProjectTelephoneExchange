package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseConnector;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.debug.DatabaseStructureCreatorFactory;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.ExchangeFactory;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.*;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.*;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters.ConcatenateFilters;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.AdminViewModel;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.cities.CitiesViewModel;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.exchanges.ExchangesViewModel;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.publicphones.PublicPhonesViewModel;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.queue.QueueViewModel;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.subscribers.SubscribersViewModel;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.SignInViewModel;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.SignUpViewModel;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.*;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.debug.DatabaseLoginViewModel;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.debug.DebugViewModel;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;

public class Navigator {
    public static final String
            DEBUG_SCREEN = "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/presentation/fxml/debugScreen.fxml",
            LOGIN_SCREEN = "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/presentation/fxml/databaseLoginScreen.fxml",
            CLIENT_SIGNIN_SCREEN = "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/presentation/fxml/client/signInScreen.fxml",
            ADMIN_SCREEN = "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/presentation/fxml/admin/adminScreen.fxml",
            CLIENT_SCREEN = "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/presentation/fxml/client/clientScreen/clientScreen.fxml",
            PUBLIC_PHONE_SCREEN = "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/presentation/fxml/admin/publicPhonesScreen.fxml",
            CLIENT_SIGNUP_SCREEN = "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/presentation/fxml/client/signUpScreen.fxml",
            EXCHANGES_SCREEN = "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/presentation/fxml/admin/exchangesScreen.fxml",
            SUBSCRIBERS_SCREEN = "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/presentation/fxml/admin/subscribersScreen.fxml",
            CITIES_SCREEN = "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/presentation/fxml/admin/citiesScreen.fxml",
            QUEUE_SCREEN = "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/presentation/fxml/admin/queueScreen.fxml";
    private final Stage primaryStage;
    private DatabaseController databaseController = null;
    private final Navigator navigator = this;
    private final ExecutorService executorService;
    private final DatabaseStructureCreatorFactory databaseStructureCreatorFactory;
    private final ExchangeFactory exchangeFactory = new ExchangeFactory();
    private final ConcatenateFilters concatenateFilters = new ConcatenateFilters();


    public Navigator(Stage primaryStage,
                     DatabaseStructureCreatorFactory databaseStructureCreatorFactory,
                     ExecutorService executorService) {
        this.primaryStage = primaryStage;
        this.databaseStructureCreatorFactory = databaseStructureCreatorFactory;
        this.executorService = executorService;
    }

    public void gotoCitiesScreen() {
        ViewModel vm = new CitiesViewModel(navigator, executorService, new GetCitiesWithNumberOfLdCalls(new GetEntityListUsecase<>(databaseController)));
        gotoScreen(CITIES_SCREEN, vm);
    }

    public void gotoSubscribersScreen() {
        ViewModel vm = new SubscribersViewModel(navigator::gotoAdminScreen, executorService,
                new GetSubscribers(exchangeFactory, new GetEntityListUsecase<>(databaseController), concatenateFilters), new GetExchanges(exchangeFactory, databaseController));
        gotoScreen(SUBSCRIBERS_SCREEN, vm);
    }

    public void gotoLoginScreen() {
        if (databaseController != null) {
            databaseController.close();
        }
        databaseController = new DatabaseController();
        ViewModel viewModel = new DatabaseLoginViewModel(new DatabaseConnector(executorService, databaseController), navigator);
        gotoScreen(Navigator.LOGIN_SCREEN, viewModel);
    }

    public void gotoDebugScreen() {
        ViewModel viewModel = new DebugViewModel(navigator, databaseStructureCreatorFactory.newDatabaseStructureCreator(databaseController), databaseController);
        gotoScreen(DEBUG_SCREEN, viewModel);
    }

    public void gotoAdminScreen() {
        ViewModel vm = new AdminViewModel(navigator, executorService, new AdminUsecase(new DatabaseInteractor(databaseController)));
        gotoScreen(ADMIN_SCREEN, vm);
    }

    public void gotoPublicPhonesScreen() {
        ViewModel vm = new PublicPhonesViewModel(navigator, executorService, new GetAddresses(new GetEntityListUsecase<>(databaseController)), new GetExchanges(exchangeFactory, databaseController), new GetPhoneNumbers(databaseController, exchangeFactory, concatenateFilters), new GetPublicPhonesList(exchangeFactory, new GetEntityListUsecase<>(databaseController)),
                new PublicPhoneTableInteractor(databaseController), new GetStreets(new GetEntityListUsecase<>(databaseController)));
        gotoScreen(PUBLIC_PHONE_SCREEN, vm);
    }

    public void gotoExchangesScreen() {
        ViewModel vm = new ExchangesViewModel(navigator, executorService, new GetExchanges(exchangeFactory, databaseController),
                new ExchangesInteractor(databaseController));
        gotoScreen(EXCHANGES_SCREEN, vm);
    }

    public void gotoClientScreen(String clientId) {
        ViewModel vm = new ClientViewModel(clientId, navigator, executorService,
                new GetPersonById(databaseController, executorService),
                new TabFactory(databaseController, executorService,exchangeFactory, concatenateFilters), new GetSubscribers(exchangeFactory, new GetEntityListUsecase<>(databaseController), concatenateFilters));
        gotoScreen(CLIENT_SCREEN, vm);
    }

    public void gotoQueueScreen() {
        ViewModel vm = new QueueViewModel(navigator, executorService, new GetRequestsWithNumberOfCables(exchangeFactory, new GetEntityListUsecase<>(databaseController)),
                new GetPhoneNumbersWithSubscribers(databaseController,
                        new GetSubscribers(exchangeFactory, new GetEntityListUsecase<>(databaseController), concatenateFilters), new GetPhoneNumbers(databaseController, exchangeFactory, concatenateFilters)),
                new RequestInteractor(databaseController));
        gotoScreen(QUEUE_SCREEN, vm);
    }

    public void gotoClientSignInScreen() {
        ViewModel vm = new SignInViewModel(new SimpleStringProperty(), executorService, navigator, new SignInUseCase(
                new DatabaseConnector(executorService, databaseController), new DatabaseInteractor(databaseController)),
                new DatabaseConnector(executorService, databaseController));

        gotoScreen(CLIENT_SIGNIN_SCREEN, vm);
    }

    public void gotoClientSignUpScreen() {
        PeopleTableInteractor peopleTableInteractor = new PeopleTableInteractor(databaseController);
        DatabaseInteractor databaseInteractor = new DatabaseInteractor(databaseController);
        DatabaseConnector databaseConnector = new DatabaseConnector(executorService, databaseController);
        ViewModel vm = new SignUpViewModel(navigator, executorService, new GetGenders(databaseController),
                peopleTableInteractor,
                new SignUpUseCase(new UserCreator(databaseInteractor),
                        databaseConnector,
                        peopleTableInteractor,
                        databaseInteractor,
                        new SignInUseCase(databaseConnector, databaseInteractor))
        );
        gotoScreen(CLIENT_SIGNUP_SCREEN, vm);
    }

    private void gotoScreen(String fxmlName, ViewModel vm) {
        try {
            Parent root = null;
            URL resource = getClass().getResource(fxmlName);
            if (resource == null) {
                new Alert(Alert.AlertType.ERROR, "Application missing fxml file resources").showAndWait();
                Platform.exit();
                return;
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(resource);
            root = loader.load();
            Object controller = loader.getController();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            ((View) controller).setViewModel(vm);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            Platform.exit();
        }
    }

    public void closeConnection() {
        databaseController.close();
    }
}
