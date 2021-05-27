package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.tabs.AbstractTab;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

public class ClientView implements View {
    @FXML
    private ChoiceBox<AbstractTab> tabComboBox;
    @FXML
    private BorderPane rightPane;
    @FXML
    private Label surnameLabel,
            nameLabel,
            ageLabel,
            genderLabel,
            beneficiaryLabel;
    @FXML
    private Button loginScreenButton, exitButton;

    @Override
    public void setViewModel(ViewModel _model) {
        ClientViewModel model = (ClientViewModel) _model;
        surnameLabel.textProperty().bind(model.getSurnameObservable());
        nameLabel.textProperty().bind(model.getNameObservable());
        ageLabel.textProperty().bind(model.getAgeObservable());
        genderLabel.textProperty().bind(model.getGenderObservable());
        beneficiaryLabel.textProperty().bind(model.getBeneficiaryObservable());
        loginScreenButton.setOnAction(event -> model.goToLoginScreen());
        exitButton.setOnAction(event -> model.exit());
        tabComboBox.itemsProperty().bind(model.tabsProperty());
        model.getTabProperty().bindBidirectional(tabComboBox.valueProperty());

        model.getTabProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue == null) return;
                    Node node = newValue.getNode();
                    rightPane.setCenter(node);
                }
        );
    }
}
