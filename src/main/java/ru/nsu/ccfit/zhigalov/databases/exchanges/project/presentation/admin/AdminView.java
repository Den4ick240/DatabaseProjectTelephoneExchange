package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

public class AdminView implements View {
    @FXML
    private Button citiesButton;
    @FXML
    private Button chargeSubscriptionFeeButton, disableExpiredClientsButton;
    @FXML
    private Button queueButton, exitButton;
    @FXML
    private  Button exchangesButton;
    @FXML
    private Button publicPhonesButton, subscribersButton;


    @Override
    public void setViewModel(ViewModel _model) {
        AdminViewModel model = (AdminViewModel) _model;

        exchangesButton.setOnAction(event -> model.onExchangesClicked());
        publicPhonesButton.setOnAction(event -> model.onPublicPhonesClicked());
        queueButton.setOnAction(event -> model.onQueueClicked());
        exitButton.setOnAction(event -> model.exit());
        chargeSubscriptionFeeButton.setOnAction(event -> model.chargeSubscriptionFee());
        disableExpiredClientsButton.setOnAction(event -> model.disableExpiredClientsButton());
        subscribersButton.setOnAction(event -> model.onSubscribersClicked());
        citiesButton.setOnAction(event -> model.onCitiesButtonClicked());
    }
}
