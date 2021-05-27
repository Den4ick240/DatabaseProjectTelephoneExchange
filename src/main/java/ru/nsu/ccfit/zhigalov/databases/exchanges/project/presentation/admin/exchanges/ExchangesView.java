package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.exchanges;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.CityExchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.DepartmentalExchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Exchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.InstitutionalExchange;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

public class ExchangesView implements View {
    @FXML
    private TableView<CityExchange> cityExchangesTableView;
    @FXML
    private TableColumn<CityExchange, Integer> cityExchangeIdColumn;
    @FXML
    private TableColumn<CityExchange, String> nameColumn;
    @FXML
    private TableView<DepartmentalExchange> departmentalExchangesTableView;
    @FXML
    private TableColumn<DepartmentalExchange, Integer> departmentalExchangeIdColumn;
    @FXML
    private TableColumn<DepartmentalExchange, String> departmentNameColumn;
    @FXML
    private TableView<InstitutionalExchange> institutionalExchangesTableView;
    @FXML
    private TableColumn<InstitutionalExchange, Integer> institutionalExchangeIdColumn;
    @FXML
    private TableColumn<InstitutionalExchange, String> institutionNameColumn;
    @FXML
    private Button goBackButton;
    @FXML
    private Label responseLabel;
    @FXML
    private Button insertButton, updateButton, deleteButton;
    @FXML
    private ChoiceBox<ExchangesViewModel.SelectedType> exchangeTypeChoiceBox;
    @FXML
    private FlowPane institutionPane, cityPane, departmentPane;
    @FXML
    private TextField institutionNameTextField, nameTextField, departmentNameTextField, idTextField;
    private ExchangesViewModel model;

    private class TableRowFactory<T extends Exchange> implements Callback<TableView<T>, TableRow<T>> {
        @Override
        public TableRow<T> call(TableView<T> param) {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> model.onTableItemClicked(row.getItem()));
            return row;
        }
    }

    @Override
    public void setViewModel(ViewModel _model) {
        model = (ExchangesViewModel) _model;

        cityExchangeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        departmentalExchangeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        departmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        institutionalExchangeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        institutionNameColumn.setCellValueFactory(new PropertyValueFactory<>("institutionName"));

        cityExchangesTableView.setRowFactory(new TableRowFactory<>());
        departmentalExchangesTableView.setRowFactory(new TableRowFactory<>());
        institutionalExchangesTableView.setRowFactory(new TableRowFactory<>());

        cityExchangesTableView.itemsProperty().bind(model.cityExchangesProperty());
        institutionalExchangesTableView.itemsProperty().bind(model.institutionalExchangesProperty());
        departmentalExchangesTableView.itemsProperty().bind(model.departmentalExchangesProperty());

        goBackButton.setOnAction(event -> model.goBack());
        responseLabel.textProperty().bind(model.responseProperty());

        exchangeTypeChoiceBox.setItems(model.getTabs());
        exchangeTypeChoiceBox.valueProperty().bindBidirectional(model.selectedTabProperty());

        nameTextField.textProperty().bindBidirectional(model.selectedNameProperty());
        departmentNameTextField.textProperty().bindBidirectional(model.selectedDepartmentNameProperty());
        institutionNameTextField.textProperty().bindBidirectional(model.selectedInstitutionNameProperty());
        idTextField.textProperty().bindBidirectional(model.selectedIdProperty());

        cityPane.visibleProperty().bind(model.cityPaneVisibleProperty());
        departmentPane.visibleProperty().bind(model.departmentPaneVisibleProperty());
        institutionPane.visibleProperty().bind(model.institutionPaneVisibleProperty());

        updateButton.setOnAction(event -> model.update());
        deleteButton.setOnAction(event -> model.delete());
        insertButton.setOnAction(event -> model.insert());
    }
}
