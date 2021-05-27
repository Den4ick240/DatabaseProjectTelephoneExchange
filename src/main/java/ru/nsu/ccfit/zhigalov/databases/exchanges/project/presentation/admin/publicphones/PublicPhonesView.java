package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.publicphones;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Address;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Exchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PhoneNumber;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

public class PublicPhonesView implements View {
    @FXML
    private Button removeExchangeFilterButton, removeStreetFilterButton;
    @FXML
    private ComboBox<String> streetFilterComBox;
    @FXML
    private ComboBox<Exchange> exchangeFilterComboBox;
    @FXML
    private Button goBackButton;
    @FXML
    private ComboBox<Exchange> exchangeComboBox;
    @FXML
    private ComboBox<Address> addressComboBox;
    @FXML
    private ComboBox<PhoneNumber> phoneNumberComboBox;
    @FXML
    private ComboBox<Integer> idComboBox;
    @FXML
    private Label responseLabel;
    @FXML
    private Button updateButton, createButton, deleteButton;
    @FXML
    private TableColumn<PublicPhoneRow, String> phoneNumberColumn, exchangeColumn, addressColumn;
    @FXML
    private TableView<PublicPhoneRow> publicPhonesTable;


    @Override
    public void setViewModel(ViewModel _model) {
        PublicPhonesViewModel model = (PublicPhonesViewModel) _model;
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        exchangeColumn.setCellValueFactory(new PropertyValueFactory<>("exchange"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        publicPhonesTable.itemsProperty().bind(model.getPhoneListProperty());
        responseLabel.textProperty().bind(model.getResponseProperty());

        exchangeComboBox.itemsProperty().bind(model.exchangesProperty());
        addressComboBox.itemsProperty().bind(model.getAddressesProperty());
        phoneNumberComboBox.itemsProperty().bind(model.getPhoneNumbersProperty());
        idComboBox.itemsProperty().bind(model.getIdsProperty());


        model.selectedExchangeProperty().bindBidirectional(exchangeComboBox.valueProperty());
        model.selectedAddressProperty().bindBidirectional(addressComboBox.valueProperty());
        model.selectedPhoneNumber().bindBidirectional(phoneNumberComboBox.valueProperty());
        model.selectedIdProperty().bindBidirectional(idComboBox.valueProperty());

        publicPhonesTable.setRowFactory(param -> {
            TableRow<PublicPhoneRow> row = new TableRow<>();
            row.setWrapText(true);
            row.setOnMouseClicked(event -> {
                model.onItemClicked(row.getItem());
            });
            return row;
        });

        createButton.setOnAction(event -> model.insert());
        deleteButton.setOnAction(event -> model.delete());
        updateButton.setOnAction(event -> model.update());
        goBackButton.setOnAction(event -> model.goBack());

        createButton.disableProperty().bind(model.disableCreateProperty());
        updateButton.disableProperty().bind(model.disableUpdateAndDeleteProperty());
        deleteButton.disableProperty().bind(model.disableUpdateAndDeleteProperty());

        streetFilterComBox.itemsProperty().bind(model.streetsProperty());
        exchangeFilterComboBox.itemsProperty().bind(model.exchangeFiltersProperty());

        removeExchangeFilterButton.setOnAction(e -> exchangeFilterComboBox.getSelectionModel().clearSelection());
        removeStreetFilterButton.setOnAction(e -> streetFilterComBox.getSelectionModel().clearSelection());

        exchangeFilterComboBox.valueProperty().addListener(
                (observable, oldValue, newValue) -> model.applyExchangeFilter(exchangeFilterComboBox.getValue()));
        streetFilterComBox.valueProperty().addListener(
                (observable, oldValue, newValue) -> model.applyStreetFilter(streetFilterComBox.getValue())
        );
    }
}
