package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.debug;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.debug.DatabaseStructureCreator;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.Navigator;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

import java.sql.SQLException;

public class DebugViewModel implements ViewModel {
    private final Navigator navigator;
    private final DatabaseStructureCreator structureCreator;
    private final StringProperty responseStringProperty = new SimpleStringProperty();
    private final DatabaseController controller;

    public DebugViewModel(Navigator navigator, DatabaseStructureCreator structureCreator, DatabaseController controller) {
        this.navigator = navigator;
        this.structureCreator = structureCreator;
        this.controller = controller;
    }

    public void createDatabaseStructure() {
        Runnable task = () -> {
            setResponse("Creating...");
                structureCreator.createDatabaseStructure();
                setResponse("Database structure created.");

        };
        new Thread(task).start();
    }

    public void dropDatabaseStructure() {
        Runnable task = () -> {
            setResponse("Dropping...");
                structureCreator.dropDatabaseStructure();
                setResponse("Database structure dropped.");
        };
        new Thread(task).start();
    }

    public ObservableValue<String> getObservableResponseString() {
        return responseStringProperty;
    }


    public void loadTestData() {
        Runnable task = () -> {
            setResponse("Loading data...");
                structureCreator.loadTestData();
                setResponse("Test data inserted.");
        };
        new Thread(task).start();
    }

    private void setResponse(String response) {
        Platform.runLater(() -> responseStringProperty.setValue(response));
    }

    public void gotoAdminScreen() {
        navigator.gotoAdminScreen();
    }

    public void gotoClientScreen() {
        navigator.gotoClientSignInScreen();
    }

    public void gotoLoginScreen() {
        navigator.gotoLoginScreen();
    }

    public void execute(String text) {
        Runnable task = () -> {
            try {
                controller.executeUpdateQuery(text);
                setResponse("Executed!");
            } catch (SQLException e) {
                e.printStackTrace();
                setResponse(e.getMessage());
            }
        };
        new Thread(task).start();
    }
}
