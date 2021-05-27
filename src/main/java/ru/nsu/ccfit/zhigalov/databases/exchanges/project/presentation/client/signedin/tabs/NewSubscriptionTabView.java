package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

public class NewSubscriptionTabView extends AbstractTab {
    @FXML
    private RequestAttributesView requestAttributesController;
    @FXML
    private Button createRequestButton;
    @FXML
    private Label responseLabel;

    @Override
    public void setViewModel(ViewModel _model) {
        NewSubscriptionViewModel model = (NewSubscriptionViewModel) _model;
        requestAttributesController.setViewModel(model.getRequestAttributesViewModel());
        createRequestButton.setOnAction(event -> model.createRequest());
        responseLabel.textProperty().bind(model.responseProperty());
    }
}