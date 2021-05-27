package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.cities;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.CityWithNumberOfLdCalls;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.GetCitiesWithNumberOfLdCalls;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.Navigator;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

import java.util.concurrent.ExecutorService;

public class CitiesViewModel implements ViewModel {
    private final Navigator navigator;
    private final ExecutorService executorService;
    private final GetCitiesWithNumberOfLdCalls getCitiesWithNumberOfLdCalls;
    private final ListProperty<CityWithNumberOfLdCalls> cities;
    private final StringProperty response;

    public CitiesViewModel(Navigator navigator, ExecutorService executorService, GetCitiesWithNumberOfLdCalls getCitiesWithNumberOfLdCalls,
                           ListProperty<CityWithNumberOfLdCalls> cities, StringProperty response) {
        this.navigator = navigator;
        this.executorService = executorService;
        this.getCitiesWithNumberOfLdCalls = getCitiesWithNumberOfLdCalls;
        this.cities = cities;
        this.response = response;
        getCities();
    }

    public CitiesViewModel(Navigator navigator, ExecutorService executorService, GetCitiesWithNumberOfLdCalls getCitiesWithNumberOfLdCalls) {
        this(navigator, executorService, getCitiesWithNumberOfLdCalls,
                new SimpleListProperty<>(FXCollections.observableArrayList()), new SimpleStringProperty());
    }

    private void getCities() {
        executorService.execute(() ->
                getCitiesWithNumberOfLdCalls.invoke(cities::setAll, this::onError));
    }

    private void onError(String s) {
        Platform.runLater(()->response.set(s));
    }

    public ReadOnlyListProperty<CityWithNumberOfLdCalls> citiesProperty() {
        return cities;
    }

    public void goBack() {
        navigator.gotoAdminScreen();
    }
}
