package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.queue;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Exchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PhoneType;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

public class QueueView implements View {
    @FXML
    private ChoiceBox<QueueViewModel.PhoneTypeWrapper> phoneTypeChoiceBox;
    @FXML
    private TableView<QueueRow> queueTable;
    @FXML
    private TableColumn<QueueRow, Integer> requestIdColumn;
    @FXML
    private TableColumn<QueueRow, String> personIdColumn;
    @FXML
    private TableColumn<QueueRow, String> nameColumn;
    @FXML
    private TableColumn<QueueRow, String> surnameColumn;
    @FXML
    private TableColumn<QueueRow, String> beneficiaryColumn;
    @FXML
    private TableColumn<QueueRow, Exchange> exchangeColumn;
    @FXML
    private TableColumn<QueueRow, String> requestDateColumn;
    @FXML
    private TableColumn<QueueRow, String> addressColumn;
    @FXML
    private Label personIdLabel, nameSurnameLabel,
            genderAgeLabel, beneficiaryLabel,
            addressLabel, exchangeIdLabel,
            exchangeNameLabel, requestIdLabel,
            requestDateLabel, responseLabel,
            freeCablesLabel;
    @FXML
    private Button assignButton, dropButton, goBackButton;
    @FXML
    private ComboBox<PhoneNumberWrapper> availablePhoneNumbersComboBox;
    @FXML
    private ChoiceBox<QueueViewModel.BeneficiaryRequestFilter> beneficiaryFilterChoiceBox;

    @Override
    public void setViewModel(ViewModel _model) {
        QueueViewModel model = (QueueViewModel) _model;

        queueTable.setRowFactory(param -> {
            TableRow<QueueRow> row = new TableRow<>();
            row.setWrapText(true);
            row.setOnMouseClicked(event -> model.onRowClicked(row.getItem()));
            return row;
        });

        requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        personIdColumn.setCellValueFactory(new PropertyValueFactory<>("personId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        beneficiaryColumn.setCellValueFactory(new PropertyValueFactory<>("beneficiary"));
        exchangeColumn.setCellValueFactory(new PropertyValueFactory<>("exchange"));
        requestDateColumn.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        queueTable.itemsProperty().bind(model.queueRowsProperty());

        personIdLabel.textProperty().bind(model.personIdProperty());
        nameSurnameLabel.textProperty().bind(model.nameSurnameProperty());
        genderAgeLabel.textProperty().bind(model.genderAgeProperty());
        beneficiaryLabel.textProperty().bind(model.beneficiaryProperty());
        addressLabel.textProperty().bind(model.addressProperty());
        exchangeIdLabel.textProperty().bind(model.exchangeIdProperty());
        exchangeNameLabel.textProperty().bind(model.exchangeNameProperty());
        requestIdLabel.textProperty().bind(model.requestIdProperty());
        requestDateLabel.textProperty().bind(model.requestDateProperty());
        responseLabel.textProperty().bind(model.responseProperty());
        freeCablesLabel.textProperty().bind(model.freeCablesProperty());
        phoneTypeChoiceBox.itemsProperty().bind(model.phoneTypesProperty());
        phoneTypeChoiceBox.valueProperty().bindBidirectional(model.selectedPhoneType());

        availablePhoneNumbersComboBox.itemsProperty().bind(model.phoneNumbersProperty());

        dropButton.setOnAction(event -> model.dropRequest());
        assignButton.setOnAction(event -> model.assignNumber(availablePhoneNumbersComboBox.getValue()));
        goBackButton.setOnAction(event -> model.goBack());

        beneficiaryFilterChoiceBox.itemsProperty().bind(model.beneficiaryFiltersProperty());
        beneficiaryFilterChoiceBox.valueProperty().addListener(
                (observable, oldValue, newValue) -> model.onBeneficiaryFilterSelected(newValue)
        );


    }
}
