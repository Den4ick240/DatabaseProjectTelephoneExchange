package ru.nsu.ccfit.zhigalov.databases.exchanges.project;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.debug.DatabaseStructureCreatorFactory;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.debug.QueryFileReader;
import javafx.application.Application;
import javafx.stage.Stage;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.Navigator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private Navigator navigator;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void start(Stage primaryStage) {
        navigator = new Navigator(primaryStage, new DatabaseStructureCreatorFactory(
                new QueryFileReader()
        ), executorService);
        navigator.gotoLoginScreen();
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            try {
                stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        executorService.shutdown();
        navigator.closeConnection();
    }
}
