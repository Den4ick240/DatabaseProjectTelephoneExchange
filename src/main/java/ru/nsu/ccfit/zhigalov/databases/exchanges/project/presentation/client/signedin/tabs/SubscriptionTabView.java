package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.tabs;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.City;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PhoneNumber;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

public class SubscriptionTabView extends AbstractTab {
    @FXML
    private Group localCallsGroup, ldCallsGroup;
    @FXML
    private ComboBox<PhoneNumber> numberToCallComboBox;
    @FXML
    private ComboBox<City> cityToCallComboBox;
    @FXML
    private Button localCallButton, dropSubscriptionButton, longDistanceButton;
    @FXML
    private View subscriptionInfoController, balanceViewController;
    @FXML
    private TextField callLengthTextField;
    @FXML
    private Label responseLabel;

    @Override
    public void setViewModel(ViewModel _model) {
        SubscriptionTabViewModel model = (SubscriptionTabViewModel) _model;

        model.tabNameProperty().addListener((observable, oldValue, newValue) -> setTabName(newValue));
        subscriptionInfoController.setViewModel(model.getSubscriptionInfoViewModel());
        balanceViewController.setViewModel(model.getBalanceViewModel());
        numberToCallComboBox.itemsProperty().bind(model.phoneNumbersProperty());
        cityToCallComboBox.itemsProperty().bind(model.citiesProperty());
        model.callLengthProperty().bindBidirectional(callLengthTextField.textProperty());
        model.callLengthProperty().addListener((observable, oldValue, newValue) -> callLengthTextField.setText(newValue));

        localCallButton.setOnAction(event -> model.localCall(numberToCallComboBox.getValue()));
        longDistanceButton.setOnAction(event -> model.longDistanceCall(cityToCallComboBox.getValue()));

        localCallsGroup.disableProperty().bind(model.localCallsDisabledProperty());
        ldCallsGroup.visibleProperty().bind(model.ldCallsDisabledProperty().not());

        responseLabel.textProperty().bind(model.responseProperty());
    }
}
