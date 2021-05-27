package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

public class SignUpView implements View {

    @FXML
    public PasswordField passwordField;

    @FXML
    private TextField idTextField;

    @FXML
    private CheckBox beneficiaryCheckBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private ChoiceBox<String> genderChoiceBox;

    @FXML
    private TextField ageTextField;

    @FXML
    private Label responseLabel;

    @FXML
    private Button signUpButton;

    @FXML
    private Button goBackButton;

    @Override
    public void setViewModel(ViewModel _model) {
        SignUpViewModel model = (SignUpViewModel) _model;
        genderChoiceBox.itemsProperty().bind(model.gendersProperty());
        model.ageProperty().addListener(((observable, oldValue, newValue) -> ageTextField.setText(newValue)));
        ageTextField.textProperty().bindBidirectional(model.ageProperty());
        responseLabel.textProperty().bind(model.responseStringProperty());
        idTextField.textProperty().bindBidirectional(model.idProperty());
        signUpButton.setOnAction(event -> model.signUp(
                nameTextField.getText(),
                surnameTextField.getText(),
                genderChoiceBox.getValue(),
                beneficiaryCheckBox.isSelected(),
                passwordField.getText()
        ));
        goBackButton.setOnAction(event -> model.goBack());
    }
}
