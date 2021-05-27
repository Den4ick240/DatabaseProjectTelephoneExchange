package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.tabs;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SubscriptionInfoView implements View {
    @FXML
    private Label subscriptionIdLabel, phoneNumberLabel, addressLabel, exchangeLabel;

    @Override
    public void setViewModel(ViewModel _model) {
        SubscriptionInfoViewModel model = (SubscriptionInfoViewModel) _model;
        subscriptionIdLabel.textProperty().bind(model.subscriptionIdProperty());
        phoneNumberLabel.textProperty().bind(model.phoneNumberProperty());
        addressLabel.textProperty().bind(model.addressProperty());
        exchangeLabel.textProperty().bind(model.exchangeProperty());
    }
}
