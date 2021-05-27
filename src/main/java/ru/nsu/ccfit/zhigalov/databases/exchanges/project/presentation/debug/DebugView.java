package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.debug;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

public class DebugView implements View {
    @FXML
    private Button loadTestDataButton,
            loginScreenButton,
            executeButton,
            clientScreenButton,
            adminScreenButton,
            createDatabaseButton,
            dropDatabaseButton;
    @FXML
    private TextArea executeTextArea;
    @FXML
    private Label responseLabel;

    private DebugViewModel model;

    @Override
    public void setViewModel(ViewModel _model) {
        model = (DebugViewModel) _model;
        createDatabaseButton.setOnAction(event -> model.createDatabaseStructure());
        dropDatabaseButton.setOnAction(event -> model.dropDatabaseStructure());
        loadTestDataButton.setOnAction(event -> model.loadTestData());
        responseLabel.textProperty().bind(model.getObservableResponseString());
        clientScreenButton.setOnAction(event -> model.gotoClientScreen());
        adminScreenButton.setOnAction(event -> model.gotoAdminScreen());
        loginScreenButton.setOnAction(event -> model.gotoLoginScreen());
        executeButton.setOnAction(event -> model.execute(executeTextArea.getText()));
    }
}
