package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

public class SignInView implements View {
    @FXML
    private TextField idTextField;
    @FXML
    private Button signInButton,
            signUpButton, exitButton;
    @FXML
    private Label responseLabel;

    @FXML
    private Node loadingNode;

    @FXML
    private Node loginNode;

    @FXML
    private PasswordField passwordField;

    private SignInViewModel model;

    @Override
    public void setViewModel(ViewModel _model) {
        model = (SignInViewModel) _model;
        responseLabel.textProperty().bind(model.getResponseStringProperty());
        signUpButton.setOnAction(event -> model.signUp());
        signInButton.setOnAction(event -> model.signIn(idTextField.getText(), passwordField.getText()));
        exitButton.setOnAction(event -> model.exit());
        loadingNode.visibleProperty().bind(model.getLoadingProperty());
        loginNode.disableProperty().bind(model.getLoginDisabledProperty());
    }
}
