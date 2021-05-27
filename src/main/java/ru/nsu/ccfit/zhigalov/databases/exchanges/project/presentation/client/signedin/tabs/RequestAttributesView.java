package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Exchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

public class RequestAttributesView implements View {
    @FXML
    private ComboBox<String> streetComboBox, houseComboBox;
    @FXML
    private ComboBox<Exchange> exchangeComboBox;
    @FXML
    private TextField apartmentTextField;

    @Override
    public void setViewModel(ViewModel _model) {
        RequestAttributesViewModel model = (RequestAttributesViewModel) _model;

        exchangeComboBox.itemsProperty().bind(model.exchangesProperty());
        streetComboBox.itemsProperty().bind(model.streetsProperty());
        houseComboBox.itemsProperty().bind(model.housesProperty());

        exchangeComboBox.valueProperty().bindBidirectional(model.selectedExchangeProperty());
        streetComboBox.valueProperty().bindBidirectional(model.selectedStreetProperty());
        houseComboBox.valueProperty().bindBidirectional(model.selectedHouseProperty());

        apartmentTextField.textProperty().bindBidirectional(model.selectedApartmentProperty());
    }
}
