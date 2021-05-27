package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.subscribers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Exchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

public class SubscribersView implements View {
    @FXML
    private ComboBox<Exchange> exchangesComboBox;
    @FXML
    private TableView<SubscriberRow> subscribersTable;

    @FXML
    private TableColumn<SubscriberRow, String> subscriberIdColumn,
            surnameColumn, nameColumn, addressColumn, exchangeColumn,
            phoneNumberColumn, balanceColumn, stateColumn, ldStateColumn;
    @FXML
    private Button goBackButton;


    @Override
    public void setViewModel(ViewModel _model) {
        SubscribersViewModel model = (SubscribersViewModel) _model;

        subscribersTable.itemsProperty().bind(model.subscriberRowsProperty());

        subscriberIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        exchangeColumn.setCellValueFactory(new PropertyValueFactory<>("exchange"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        ldStateColumn.setCellValueFactory(new PropertyValueFactory<>("ldState"));

        exchangesComboBox.itemsProperty().bind(model.exchangesProperty());
        model.selectedExchangeProperty().bindBidirectional(exchangesComboBox.valueProperty());

        goBackButton.setOnAction(event -> model.goBack());
    }
}
