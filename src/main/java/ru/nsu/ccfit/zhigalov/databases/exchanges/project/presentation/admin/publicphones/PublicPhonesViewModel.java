package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.publicphones;

import javafx.beans.Observable;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Address;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Exchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PhoneNumber;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PublicPhone;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.PublicPhoneTableInteractor;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.*;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters.ExchangeFilter;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.Navigator;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class PublicPhonesViewModel implements ViewModel {
    private final Property<Boolean> disableCreate, disableUpdateAndDelete;
    private final ListProperty<PublicPhoneRow> publicPhoneList;
    private final ListProperty<Exchange> exchanges, exchangeFilters;
    private final ListProperty<Address> addresses;
    private final ListProperty<PhoneNumber> phoneNumbers;
    private final ListProperty<Integer> ids;
    private final ListProperty<String> streets;
    private final ObjectProperty<Exchange> selectedExchange;
    private final Property<Integer> selectedId;
    private final ObjectProperty<PhoneNumber> selectedPhoneNumber;
    private final ObjectProperty<Address> selectedAddress;
    private final StringProperty responseProperty;
    private final ExecutorService executorService;
    private final GetPublicPhonesList getPublicPhonesList;
    private final PublicPhoneTableInteractor publicPhoneTableInteractor;
    private final Navigator navigator;
    private final ErrorListener errorListener = this::setResponse;
    private final GetExchanges getExchanges;


    public PublicPhonesViewModel(Navigator navigator, ExecutorService executorService, GetAddresses getAddresses, GetExchanges getExchanges,
                                 GetPhoneNumbers getPhoneNumbers, GetPublicPhonesList getPublicPhonesList,
                                 PublicPhoneTableInteractor publicPhoneTableInteractor, GetStreets getStreets) {
        this(navigator, executorService, getAddresses, getExchanges, getPhoneNumbers, getPublicPhonesList, publicPhoneTableInteractor,
                new SimpleObjectProperty<>(), new SimpleObjectProperty<>(), new SimpleListProperty<>(FXCollections.observableArrayList()),
                new SimpleListProperty<>(FXCollections.observableArrayList()), new SimpleListProperty<>(FXCollections.observableArrayList()),
                new SimpleListProperty<>(FXCollections.observableArrayList()), new SimpleListProperty<>(FXCollections.observableArrayList()),
                new SimpleObjectProperty<>(), new SimpleObjectProperty<>(), new SimpleStringProperty(), new SimpleObjectProperty<>(), new SimpleObjectProperty<>(),
                new SimpleListProperty<>(FXCollections.observableArrayList()), getStreets, new SimpleListProperty<>(FXCollections.observableArrayList()));
    }

    public PublicPhonesViewModel(Navigator navigator, ExecutorService executorService, GetAddresses getAddresses, GetExchanges getExchanges,
                                 GetPhoneNumbers getPhoneNumbers, GetPublicPhonesList getPublicPhonesList, PublicPhoneTableInteractor publicPhoneTableInteractor,
                                 Property<Boolean> disableCreate, Property<Boolean> disableUpdateAndDelete, ListProperty<Address> addresses,
                                 ListProperty<PhoneNumber> phoneNumbers, ListProperty<Integer> ids, ListProperty<PublicPhoneRow> publicPhoneList,
                                 ListProperty<Exchange> exchanges, ObjectProperty<Exchange> selectedExchange, ObjectProperty<Address> selectedAddress,
                                 StringProperty responseProperty, ObjectProperty<PhoneNumber> selectedPhoneNumber, Property<Integer> selectedId,
                                 ListProperty<String> streets, GetStreets getStreets, ListProperty<Exchange> exchangeFilters) {
        this.getPublicPhonesList = getPublicPhonesList;
        this.executorService = executorService;
        this.disableCreate = disableCreate;
        this.disableUpdateAndDelete = disableUpdateAndDelete;
        this.publicPhoneList = publicPhoneList;
        this.exchanges = exchanges;
        this.addresses = addresses;
        this.phoneNumbers = phoneNumbers;
        this.ids = ids;
        this.selectedExchange = selectedExchange;
        this.selectedId = selectedId;
        this.selectedPhoneNumber = selectedPhoneNumber;
        this.selectedAddress = selectedAddress;
        this.responseProperty = responseProperty;
        this.publicPhoneTableInteractor = publicPhoneTableInteractor;
        this.navigator = navigator;
        this.streets = streets;
        this.exchangeFilters = exchangeFilters;
        this.publicPhoneList.setValue(FXCollections.observableArrayList());
        setAvailableOperations();
        this.selectedExchange.addListener((observable, oldValue, newValue) -> {
            setAvailableOperations();
            if (newValue == null) return;
            getPhoneNumbers.setFilters(Collections.singletonList(new ExchangeFilter(newValue)));
            executorService.execute(() -> getPhoneNumbers.invoke(object -> setListProperty(object, phoneNumbers), errorListener));
        });
        selectedId.addListener((observable, oldValue, newValue) -> {
            setAvailableOperations();
            if (newValue == null) return;
            Optional<PublicPhoneRow> optionalPublicPhoneRow = publicPhoneList.stream()
                    .filter(row -> newValue.equals(row.getPublicPhone().getId())).findAny();
            if (!optionalPublicPhoneRow.isPresent()) return;
            PublicPhone selectedPhone = optionalPublicPhoneRow.get().getPublicPhone();
            setSelectedPhone(selectedPhone);
        });
        selectedAddress.addListener(this::onAddressSelected);
        selectedPhoneNumber.addListener((observable, oldValue, newValue) -> setAvailableOperations());
        this.getExchanges = getExchanges;
        executorService.execute(() -> {
            getAddresses.invoke(object -> setListProperty(object, addresses), errorListener);
            getExchanges.invoke(object -> setListProperty(object, exchangeFilters), errorListener);
            getStreets.invoke(object -> setListProperty(object, streets), errorListener);
        });
        updatePhoneList();
    }

    private void onAddressSelected(Observable observable, Address oldValue, Address newValue) {
        setAvailableOperations();
        if (newValue == null) return;
        getExchanges.setAddressFilter(newValue);
        getExchanges.invoke(object -> setListProperty(object, exchanges), errorListener);
    }

    public Property<ObservableList<PublicPhoneRow>> getPhoneListProperty() {
        return publicPhoneList;
    }

    public ObservableValue<ObservableList<Exchange>> exchangeFiltersProperty() {
        return exchangeFilters;
    }

    public ObservableValue<ObservableList<Exchange>> exchangesProperty() {
        return exchanges;
    }

    public ObservableValue<ObservableList<Address>> getAddressesProperty() {
        return addresses;
    }

    public ObservableValue<ObservableList<PhoneNumber>> getPhoneNumbersProperty() {
        return phoneNumbers;
    }

    public ObservableValue<String> getResponseProperty() {
        return responseProperty;
    }

    public Property<Address> selectedAddressProperty() {
        return selectedAddress;
    }

    public Property<PhoneNumber> selectedPhoneNumber() {
        return selectedPhoneNumber;
    }

    public Property<Exchange> selectedExchangeProperty() {
        return selectedExchange;
    }

    public Property<Integer> selectedIdProperty() {
        return selectedId;
    }

    public ListProperty<Integer> getIdsProperty() {
        return ids;
    }

    public Property<Boolean> disableCreateProperty() {
        return disableCreate;
    }

    public Property<Boolean> disableUpdateAndDeleteProperty() {
        return disableUpdateAndDelete;
    }

    public void onItemClicked(PublicPhoneRow item) {
        setSelectedPhone(item.getPublicPhone());
    }

    public void insert() {
        disableAllOperations();
        setResponse("Inserting...");
        executorService.execute(() ->
                publicPhoneTableInteractor.insert(getSelectedPublicPhone(), () -> setResponse("Inserted!"), errorListener));
        updatePhoneList();
    }

    public void delete() {
        disableAllOperations();
        setResponse("Deleting...");
        executorService.execute(() ->
                publicPhoneTableInteractor.delete(getSelectedPublicPhone(), () -> setResponse("Deleted!"), errorListener));
        updatePhoneList();
    }

    public void update() {
        disableAllOperations();
        setResponse("Updating...");
        executorService.execute(() ->
                publicPhoneTableInteractor.update(getSelectedPublicPhone(), () -> setResponse("Updated!"), errorListener));
        updatePhoneList();
    }

    public void goBack() {
        navigator.gotoAdminScreen();
    }

    public ObservableValue<ObservableList<String>> streetsProperty() {
        return streets;
    }

    public void applyExchangeFilter(Exchange exchange) {
        getPublicPhonesList.setExchangeFilter(exchange);
        updatePhoneList();
    }

    public void applyStreetFilter(String street) {
        getPublicPhonesList.setStreetFilter(street);
        updatePhoneList();
    }


    private void updatePhoneList() {
        executorService.execute(() ->
                getPublicPhonesList.invoke(object -> Platform.runLater(() -> onPhoneListReceived(object)), errorListener));

    }

    private PublicPhone getSelectedPublicPhone() {
        Integer id = selectedId.getValue();
        return new PublicPhone(selectedAddress.get(), selectedPhoneNumber.get(), id == null ? 0 : id);
    }

    private void disableAllOperations() {
        disableCreate.setValue(true);
        disableUpdateAndDelete.setValue(true);
    }

    private void onPhoneListReceived(Collection<PublicPhone> phoneCollection) {
        publicPhoneList.setAll(phoneCollection.stream().map(PublicPhoneRow::new).collect(Collectors.toList()));
        ids.setAll(phoneCollection.stream().map(PublicPhone::getId).collect(Collectors.toList()));
    }

    private void setResponse(String response) {
        Platform.runLater(() -> responseProperty.setValue(response));
    }

    private <T> void setListProperty(Collection<T> input, ListProperty<T> property) {
        Platform.runLater(() -> property.setAll(input));
    }

    private void setAvailableOperations() {
        boolean idIsNull = selectedId.getValue() == null,
                everythingElseIsNull = selectedAddress.get() == null
                        || selectedExchange.get() == null
                        || selectedPhoneNumber.get() == null;
        disableCreate.setValue(everythingElseIsNull);
        disableUpdateAndDelete.setValue(idIsNull || everythingElseIsNull);
    }

    private void setSelectedPhone(PublicPhone selectedPhone) {
        selectedId.setValue(selectedPhone.getId());
        selectedAddress.setValue(selectedPhone.getAddress());
        selectedExchange.setValue(selectedPhone.getPhoneNumber().getExchange());
        Platform.runLater(() -> selectedPhoneNumber.setValue(selectedPhone.getPhoneNumber()));
    }
}