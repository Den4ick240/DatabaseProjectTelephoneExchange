package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.tabs;

import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

public class BalanceView implements View {
    @FXML
    private Label localCallsWarningLabel, ldCallsWarningLabel, localCallsLabel, ldCallsLabel;
    @FXML
    private Node connectLocalCallsNode, connectLdCallsNode;
    @FXML
    private Label balanceLabel;
    @FXML
    private TextField paymentTextField;
    @FXML
    private Button paymentButton, localCallsButton, ldCallsButton;

    @Override
    public void setViewModel(ViewModel _model) {
        BalanceViewModel model = (BalanceViewModel) _model;

        balanceLabel.textProperty().bind(model.balanceProperty());
        paymentTextField.textProperty().bindBidirectional(model.valueProperty());
        model.valueProperty().addListener((observable, oldValue, newValue) -> paymentTextField.setText(newValue));
        paymentButton.setOnAction(event -> model.topUpBalance());

        localCallsWarningLabel.visibleProperty().bind(model.localCallsWarningVisibleProperty());
        ldCallsWarningLabel.visibleProperty().bind(model.ldCallsWarningVisibleProperty());
        connectLdCallsNode.visibleProperty().bind(model.connectLdCallsVisibleProperty());
        connectLocalCallsNode.visibleProperty().bind(model.connectLocalCallsVisibleProperty());

        localCallsLabel.textProperty().bind(
                Bindings.format(localCallsLabel.getText(), model.enableLocalCallsPriceProperty()));
        ldCallsLabel.textProperty().bind(
                Bindings.format(ldCallsLabel.getText(), model.enableLdCallsPriceProperty()));
        localCallsButton.setOnAction(event -> model.connectLocalCalls());
        ldCallsButton.setOnAction(event -> model.connectLdCalls());
    }
}









