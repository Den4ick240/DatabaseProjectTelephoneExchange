package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client.signedin.tabs;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Address;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Exchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Person;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Request;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.GetExchanges;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.GetHouses;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.GetStreets;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

import java.util.concurrent.ExecutorService;

public class RequestAttributesViewModel implements ViewModel {
    private final ExecutorService executorService;
    private final GetExchanges getExchanges;
    private final GetStreets getStreets;
    private final GetHouses getHouses;
    private final ListProperty<Exchange> exchanges;
    private final ListProperty<String> streets, houses;
    private final Property<String> selectedStreet, selectedHouse;
    private final Property<Exchange> selectedExchange;
    private final StringProperty selectedApartment;
    private final Integer requestId;
    private final Person person;

    public RequestAttributesViewModel(ExecutorService executorService, GetExchanges getExchanges, GetStreets getStreets,
                                      GetHouses getHouses, Person person, int requestId) {
        this(executorService, getExchanges, getStreets, getHouses, person, requestId,
                new SimpleListProperty<>(FXCollections.observableArrayList()),
                new SimpleListProperty<>(FXCollections.observableArrayList()),
                new SimpleListProperty<>(FXCollections.observableArrayList()),
                new SimpleObjectProperty<>(), new SimpleObjectProperty<>(),
                new SimpleObjectProperty<>(),
                new SimpleStringProperty());
    }

    public RequestAttributesViewModel(ExecutorService executorService, GetExchanges getExchanges, GetStreets getStreets,
                                      GetHouses getHouses, Person person, int requestId, ListProperty<Exchange> exchanges,
                                      ListProperty<String> streets, ListProperty<String> houses,
                                      Property<String> selectedStreet, Property<String> selectedHouse,
                                      Property<Exchange> selectedExchange,
                                      StringProperty selectedApartment) {
        this.executorService = executorService;
        this.getExchanges = getExchanges;
        this.getStreets = getStreets;
        this.getHouses = getHouses;
        this.exchanges = exchanges;
        this.streets = streets;
        this.houses = houses;
        this.selectedStreet = selectedStreet;
        this.selectedHouse = selectedHouse;
        this.selectedExchange = selectedExchange;
        this.selectedApartment = selectedApartment;
        this.person = person;
        this.requestId = requestId;

        selectedStreet.addListener(this::onStreetSelected);
        selectedHouse.addListener(this::onHouseSelected);
        getStreets();
    }


    public ReadOnlyListProperty<Exchange> exchangesProperty() {
        return exchanges;
    }

    public ReadOnlyListProperty<String> streetsProperty() {
        return streets;
    }

    public ReadOnlyListProperty<String> housesProperty() {
        return houses;
    }

    public Property<Exchange> selectedExchangeProperty() {
        return selectedExchange;
    }

    public Property<String> selectedStreetProperty() {
        return selectedStreet;
    }

    public Property<String> selectedHouseProperty() {
        return selectedHouse;
    }

    public Property<String> selectedApartmentProperty() {
        return selectedApartment;
    }

    private void onStreetSelected(Observable observable, String oldValue, String newValue) {
        if (newValue == null) {
            houses.clear();
        } else {
            getHouses();
        }
    }

    private void onHouseSelected(Observable observable, String oldValue, String newValue) {
        if (newValue != null && selectedStreet.getValue() != null) {
            getExchanges();
        }
    }

    private void getExchanges() {
        getExchanges.setAddressFilter(new Address(selectedStreet.getValue(), selectedHouse.getValue()));
        executorService.execute(() -> getExchanges.invoke(exchanges::setAll, this::onError));
    }

    private void getHouses() {
        executorService.execute(() ->
                getHouses.invoke(selectedStreet.getValue(),
                        c -> Platform.runLater(() -> houses.setAll(c)), this::onError));
    }

    private void getStreets() {
        executorService.execute(() ->
                getStreets.invoke(streets::setAll, this::onError));
    }

    private void onError(String message) {
        System.out.println(message);
    }

    public Request getSelectedRequest() {
        if (selectedStreet.getValue() == null ||
                selectedHouse.getValue() == null ||
                selectedExchange.getValue() == null ||
                selectedApartment.get() == null)
            return null;
        else
            return new Request(
                    requestId,
                    new Address(selectedStreet.getValue(), selectedHouse.getValue()),
                    selectedApartment.get(),
                    selectedExchange.getValue(),
                    person
            );
    }
}
