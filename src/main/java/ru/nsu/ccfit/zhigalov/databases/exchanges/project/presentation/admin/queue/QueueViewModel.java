package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.queue;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.*;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.RequestInteractor;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.GetPhoneNumbersWithSubscribers;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.GetRequestsWithNumberOfCables;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.PhoneNumberWithSubscribers;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.Navigator;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class QueueViewModel implements ViewModel {
    class PhoneTypeWrapper {
        private final PhoneType phoneType;

        PhoneTypeWrapper(PhoneType phoneType) {
            this.phoneType = phoneType;
        }

        @Override
        public String toString() {
//            switch (phoneType) {
//                case UNOCCUPIED:
//                    return "Свободен";
//                case PAIRED:
//                    return "Спаренный";
//                case PUBLIC:
//                    return "Таксофон";
//                case SIMPLE:
//                    return "Простой";
//                case PARALLEL:
//                    return "Параллельный";
//            }
            return phoneType.russianString();
        }
    }

    private static final String SPECIFY_ATTRIBUTES_MESSAGE = "Вы должны выбрать все атрибуты прежде чем выполнять действия";
    private final Navigator navigator;
    private final ExecutorService executorService;
    private final GetRequestsWithNumberOfCables getRequests;
    private final GetPhoneNumbersWithSubscribers getPhoneNumbersWithSubscribers;
    private final RequestInteractor requestInteractor;
    private final ListProperty<QueueRow> queueRows;
    private final ListProperty<PhoneNumberWrapper> phoneNumbers;
    private final ListProperty<PhoneTypeWrapper> phoneTypes;
    private final Property<PhoneTypeWrapper> selectedPhoneType;
    private final StringProperty nameSurname, address, exchangeName,
            requestDate, genderAge, personId, exchangeId, requestId, beneficiary,
            response, freeCables;
    private Request selectedRequest = null;

    public ObservableValue<String> freeCablesProperty() {
        return freeCables;
    }

    public Property<PhoneTypeWrapper> selectedPhoneType() {
        return selectedPhoneType;
    }

    static class BeneficiaryRequestFilter {
        final Integer filter;
        final String stringValue;

        BeneficiaryRequestFilter(Integer filter, String stringValue) {
            this.filter = filter;
            this.stringValue = stringValue;
        }

        @Override
        public String toString() {
            return stringValue;
        }

    }

    private final ListProperty<BeneficiaryRequestFilter> beneficiaryFilters = new SimpleListProperty<>(FXCollections.observableArrayList(
            new BeneficiaryRequestFilter(null, "Показать всех"),
            new BeneficiaryRequestFilter(0, "Не показывать льготников"),
            new BeneficiaryRequestFilter(1, "Показать только льготников")
    ));

    public ReadOnlyListProperty<BeneficiaryRequestFilter> beneficiaryFiltersProperty() {
        return beneficiaryFilters;
    }

    public void onBeneficiaryFilterSelected(BeneficiaryRequestFilter filter) {
        getRequests.setBeneficiaryFilter(filter.filter);
        getRequests();
    }


    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty exchangeNameProperty() {
        return exchangeName;
    }

    public StringProperty requestDateProperty() {
        return requestDate;
    }

    public StringProperty genderAgeProperty() {
        return genderAge;
    }

    public StringProperty personIdProperty() {
        return personId;
    }

    public StringProperty exchangeIdProperty() {
        return exchangeId;
    }

    public StringProperty requestIdProperty() {
        return requestId;
    }

    public QueueViewModel(Navigator navigator, ExecutorService executorService, GetRequestsWithNumberOfCables getRequests,
                          GetPhoneNumbersWithSubscribers getPhoneNumbersWithSubscribers, RequestInteractor requestInteractor) {
        this(navigator, executorService, getRequests, getPhoneNumbersWithSubscribers,
                requestInteractor, new SimpleListProperty<>(FXCollections.observableArrayList()),
                new SimpleStringProperty(), new SimpleStringProperty(),
                new SimpleStringProperty(),
                new SimpleListProperty<>(FXCollections.observableArrayList()),
                new SimpleListProperty<>(FXCollections.observableArrayList()),
                new SimpleObjectProperty<>(), new SimpleStringProperty(), new SimpleStringProperty(),
                new SimpleStringProperty(), new SimpleStringProperty(),
                new SimpleStringProperty(), new SimpleStringProperty(),
                new SimpleStringProperty(), new SimpleStringProperty());
    }

    public QueueViewModel(Navigator navigator, ExecutorService executorService, GetRequestsWithNumberOfCables getRequests,
                          GetPhoneNumbersWithSubscribers getPhoneNumbersWithSubscribers, RequestInteractor requestInteractor, ListProperty<QueueRow> queueRows, StringProperty address, StringProperty requestDate, StringProperty personId,
                          ListProperty<PhoneNumberWrapper> phoneNumbers,
                          ListProperty<PhoneTypeWrapper> phoneTypes, Property<PhoneTypeWrapper> selectedPhoneType, StringProperty nameSurname, StringProperty exchangeName,
                          StringProperty genderAge, StringProperty exchangeId,
                          StringProperty requestId, StringProperty beneficiary, StringProperty response, StringProperty freeCables) {
        this.navigator = navigator;
        this.executorService = executorService;
        this.getRequests = getRequests;
        this.getPhoneNumbersWithSubscribers = getPhoneNumbersWithSubscribers;
        this.requestInteractor = requestInteractor;
        this.queueRows = queueRows;

        this.address = address;
        this.requestDate = requestDate;
        this.personId = personId;
        this.phoneNumbers = phoneNumbers;
        this.phoneTypes = phoneTypes;
        this.selectedPhoneType = selectedPhoneType;
        this.nameSurname = nameSurname;
        this.exchangeName = exchangeName;
        this.genderAge = genderAge;
        this.exchangeId = exchangeId;
        this.requestId = requestId;
        this.beneficiary = beneficiary;
        this.response = response;
        this.freeCables = freeCables;
        phoneTypes.setAll(new PhoneTypeWrapper(PhoneType.PAIRED),
                new PhoneTypeWrapper(PhoneType.PARALLEL),
                new PhoneTypeWrapper(PhoneType.SIMPLE));
        getRequests();
    }

    public void onRowClicked(QueueRow clickedRow) {
        if (clickedRow == null) return;
        getPhoneNumbers(clickedRow.getExchange(), clickedRow.getRequest().getAddress());
        selectedRequest = clickedRow.getRequest();
        personId.set(clickedRow.getPersonId());
        nameSurname.set(clickedRow.getName() + " " + clickedRow.getSurname());
        genderAge.set(selectedRequest.getPerson().getGender() + ", " + selectedRequest.getPerson().getAge() + " y.o.");
        beneficiary.set(selectedRequest.getPerson().isBeneficiary() ? "да" : "нет");
        requestId.setValue(selectedRequest.getId().toString());
        requestDate.set(clickedRow.getRequestDate());
        freeCables.set(clickedRow.getFreeCables().toString());
        Address address = selectedRequest.getAddress();
        this.address.set(address.getStreet() + " " + address.getHouse() + ", " + selectedRequest.getApartment());
        exchangeId.set(Integer.toString(selectedRequest.getExchange().getId()));
        exchangeName.set(selectedRequest.getExchange().toString());
    }

    private void setSelectedNull() {
        phoneNumbers.get().clear();
        selectedRequest = null;
        personId.set(null);
        nameSurname.set(null);
        genderAge.set(null);
        beneficiary.set(null);
        requestId.set(null);
        requestDate.set(null);
        address.set(null);
        exchangeId.set(null);
        exchangeName.set(null);
    }

    public void goBack() {
        navigator.gotoAdminScreen();
    }

    public void dropRequest() {
        if (selectedRequest == null) return;
        executorService.execute(() ->
                requestInteractor.delete(selectedRequest, () -> {
                    Platform.runLater(this::setSelectedNull);
                    getRequests();
                    setResponse("Dropped!");
                }, this::setResponse));
    }

    public void assignNumber(PhoneNumberWrapper phoneNumberWrapper) {
        if (selectedRequest == null || phoneNumberWrapper == null || selectedPhoneType.getValue() == null) {
            setResponse(SPECIFY_ATTRIBUTES_MESSAGE);
            return;
        }
        requestInteractor.assignNumberToRequest(selectedRequest, phoneNumberWrapper.getPhoneNumber(),
                selectedPhoneType.getValue().phoneType,
                () -> Platform.runLater(() -> {
                    setSelectedNull();
                    setResponse("Assigned");
                }), this::setResponse);
        getRequests();
    }


    private void getPhoneNumbers(Exchange exchange, Address address) {
        executorService.execute(() ->
                getPhoneNumbersWithSubscribers.invoke(
                        this::onPhoneNumbersReceived,
                        this::setResponse, exchange, address));
    }

    private void onPhoneNumbersReceived(Collection<PhoneNumberWithSubscribers> numbers) {
        Platform.runLater(() ->
                phoneNumbers.setAll(numbers.stream().map(PhoneNumberWrapper::new).collect(Collectors.toList())));
    }


    private void getRequests() {
        executorService.execute(() ->
                getRequests.invoke(this::onRequestsReceived, this::setResponse));
    }

    private void onRequestsReceived(Collection<RequestWithNumberOfCables> requests) {
        Platform.runLater(() ->
                queueRows.setAll(requests.stream().map(QueueRow::new).collect(Collectors.toList())));
    }

    private void setResponse(String message) {
        Platform.runLater(() -> response.set(message));
    }

    public ObservableValue<ObservableList<QueueRow>> queueRowsProperty() {
        return queueRows;
    }

    public ReadOnlyListProperty<PhoneNumberWrapper> phoneNumbersProperty() {
        return phoneNumbers;
    }

    public ReadOnlyStringProperty nameSurnameProperty() {
        return nameSurname;
    }

    public ReadOnlyStringProperty beneficiaryProperty() {
        return beneficiary;
    }

    public ReadOnlyStringProperty responseProperty() {
        return response;
    }

    public ReadOnlyListProperty<PhoneTypeWrapper> phoneTypesProperty() {
        return phoneTypes;
    }
}
