package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.debug;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;


public class DatabaseLoginView implements View {
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField loginField,
            databaseHostField;
    @FXML
    private Label responseLabel;
    @FXML
    private Button loginButton;

    private DatabaseLoginViewModel model;

    public void setViewModel(ViewModel _model) {
        model = (DatabaseLoginViewModel) _model;
        loginButton.setOnAction(event ->
                model.onLoginButtonClicked(databaseHostField.getText(), loginField.getText(), passwordField.getText()));
        responseLabel.textProperty().bind(model.getResponseProperty());
        responseLabel.setAlignment(Pos.CENTER);
        model.getResponseFailureProperty().addListener((unused1, unused2, isConnectionFailed) -> {
            if (isConnectionFailed) {
                responseLabel.setTextFill(Color.RED);
            } else {
                responseLabel.setTextFill(Color.BLACK);
            }
        });
    }
}
