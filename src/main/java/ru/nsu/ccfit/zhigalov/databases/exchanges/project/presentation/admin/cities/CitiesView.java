package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.cities;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.City;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.CityWithNumberOfLdCalls;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.View;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

public class CitiesView implements View {
    @FXML
    private TableView<CityWithNumberOfLdCalls> citiesTable;
    @FXML
    private TableColumn<CityWithNumberOfLdCalls, City> cityColumn;
    @FXML
    private TableColumn<CityWithNumberOfLdCalls, Integer> callsColumn;
    @FXML
    private Button goBackButton;

    @Override
    public void setViewModel(ViewModel _model) {
        CitiesViewModel model = (CitiesViewModel) _model;

        citiesTable.itemsProperty().bind(model.citiesProperty());
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        callsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfCalls"));
        goBackButton.setOnAction(event -> model.goBack());

    }
}
