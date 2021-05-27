package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.exchanges;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.CityExchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.DepartmentalExchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Exchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.InstitutionalExchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.ExchangesInteractor;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.GetExchanges;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.Navigator;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class ExchangesViewModel implements ViewModel {
    private final Navigator navigator;
    private final ExecutorService executorService;
    private final GetExchanges getExchanges;
    private final ExchangesInteractor exchangesInteractor;
    private final ListProperty<CityExchange> cityExchanges;
    private final ListProperty<DepartmentalExchange> departmentalExchanges;
    private final ListProperty<InstitutionalExchange> institutionalExchanges;
    private final StringProperty response, selectedId, selectedName, selectedDepartmentName, selectedInstitutionName;
    private final BooleanProperty cityPaneVisible, departmentPaneVisible, institutionPaneVisible,
            insertDisabled, updateAndDeleteDisabled;
    private final Property<SelectedType> selectedTab;

    abstract class SelectedType {
        private final BooleanProperty visible;
        private final String tabName;

        SelectedType(BooleanProperty visible, String tabName) {
            this.visible = visible;
            this.tabName = tabName;
        }

        void set() {
            cityPaneVisible.set(false);
            departmentPaneVisible.set(false);
            institutionPaneVisible.set(false);
            visible.set(true);
        }

        @Override
        public String toString() {
            return tabName;
        }

        abstract Exchange getExchange();

        abstract boolean fieldsAreNull();

    }

    private final SelectedType cityTab, departmentTab, institutionTab;


    public ExchangesViewModel(Navigator navigator, ExecutorService executorService,
                              GetExchanges getExchanges, ExchangesInteractor exchangesInteractor) {
        this(navigator, executorService, getExchanges, exchangesInteractor,
                new SimpleListProperty<>(FXCollections.observableArrayList()),
                new SimpleListProperty<>(FXCollections.observableArrayList()),
                new SimpleListProperty<>(FXCollections.observableArrayList()),
                new SimpleStringProperty(),
                new SimpleStringProperty(),
                new SimpleStringProperty(),
                new SimpleStringProperty(),
                new SimpleStringProperty(),
                new SimpleBooleanProperty(),
                new SimpleBooleanProperty(),
                new SimpleBooleanProperty(),
                new SimpleBooleanProperty(),
                new SimpleBooleanProperty(),
                new SimpleObjectProperty<>());
    }

    public ExchangesViewModel(Navigator navigator, ExecutorService executorService,
                              GetExchanges getExchanges, ExchangesInteractor exchangesInteractor,
                              ListProperty<CityExchange> cityExchanges,
                              ListProperty<DepartmentalExchange> departmentalExchanges,
                              ListProperty<InstitutionalExchange> institutionalExchanges,
                              StringProperty response, StringProperty selectedId,
                              StringProperty selectedName, StringProperty selectedDepartmentName,
                              StringProperty selectedInstitutionName, BooleanProperty institutionPaneVisible,
                              BooleanProperty departmentPaneVisible, BooleanProperty cityPaneVisible,
                              BooleanProperty insertDisabled, BooleanProperty updateAndDeleteDisabled,
                              Property<SelectedType> selectedTab) {
        this.navigator = navigator;
        this.executorService = executorService;
        this.getExchanges = getExchanges;
        this.exchangesInteractor = exchangesInteractor;
        this.cityExchanges = cityExchanges;
        this.departmentalExchanges = departmentalExchanges;
        this.institutionalExchanges = institutionalExchanges;
        this.response = response;
        this.selectedId = selectedId;
        this.selectedName = selectedName;
        this.selectedDepartmentName = selectedDepartmentName;
        this.selectedInstitutionName = selectedInstitutionName;
        this.institutionPaneVisible = institutionPaneVisible;
        this.cityPaneVisible = cityPaneVisible;
        this.departmentPaneVisible = departmentPaneVisible;
        this.insertDisabled = insertDisabled;
        this.updateAndDeleteDisabled = updateAndDeleteDisabled;
        this.selectedTab = selectedTab;

        cityTab = new SelectedType(cityPaneVisible, "Городская АТС") {
            @Override
            Exchange getExchange() {
                return new CityExchange(getSelectedId(), selectedName.get());
            }

            @Override
            boolean fieldsAreNull() {
                return selectedName.get() == null;
            }
        };
        departmentTab = new SelectedType(departmentPaneVisible, "Ведомственная АТС") {
            @Override
            Exchange getExchange() {
                return new DepartmentalExchange(getSelectedId(), selectedDepartmentName.get());
            }

            @Override
            boolean fieldsAreNull() {
                return selectedDepartmentName.get() == null;
            }
        };
        institutionTab = new SelectedType(institutionPaneVisible, "Учрежденческая АТС") {
            @Override
            Exchange getExchange() {
                return new InstitutionalExchange(getSelectedId(), selectedInstitutionName.get());
            }

            @Override
            boolean fieldsAreNull() {
                return selectedInstitutionName.get() == null;
            }
        };
        updateExchanges();
        selectedTab.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) newValue.set();
        });
    }

    public void onTableItemClicked(Exchange exchange) {
        selectedId.set(String.valueOf(exchange.getId()));
        if (exchange instanceof CityExchange) {
//            cityTab.set();
            selectedTab.setValue(cityTab);
            selectedName.set(((CityExchange) exchange).getName());
        }
        if (exchange instanceof DepartmentalExchange) {
            selectedTab.setValue(departmentTab);
            selectedDepartmentName.set(((DepartmentalExchange) exchange).getDepartmentName());
        }
        if (exchange instanceof InstitutionalExchange) {
//            institutionTab.set();
            selectedTab.setValue(institutionTab);
            selectedInstitutionName.set(((InstitutionalExchange) exchange).getInstitutionName());
        }
    }

    public void update() {
        executorService.execute(() ->
                exchangesInteractor.update(selectedTab.getValue().getExchange(),
                        () -> setResponse("Updated!"), this::setResponse));
        updateExchanges();
    }

    public void delete() {
        executorService.execute(() ->
                exchangesInteractor.delete(selectedTab.getValue().getExchange(),
                        () -> setResponse("Deleted!"), this::setResponse));
        updateExchanges();
    }

    public void insert() {
        executorService.execute(() ->
                exchangesInteractor.insert(selectedTab.getValue().getExchange(),
                        () -> setResponse("Inserted!"), this::setResponse));
        updateExchanges();
    }

    public void goBack() {
        navigator.gotoAdminScreen();
    }

    public ObservableList<SelectedType> getTabs() {
        return FXCollections.observableArrayList(
                cityTab, departmentTab, institutionTab);
    }

    public Property<SelectedType> selectedTabProperty() {
        return selectedTab;
    }

    private int getSelectedId() {
        if (selectedId.get() == null) return 0;
        else return Integer.parseInt(selectedId.get());
    }

    public ObservableBooleanValue insertDisabledObservable() {
        return insertDisabled;
    }

    public ObservableBooleanValue updateAndDeleteDisabledObservable() {
        return updateAndDeleteDisabled;
    }

    public BooleanProperty cityPaneVisibleProperty() {
        return cityPaneVisible;
    }

    public BooleanProperty departmentPaneVisibleProperty() {
        return departmentPaneVisible;
    }

    public BooleanProperty institutionPaneVisibleProperty() {
        return institutionPaneVisible;
    }

    public ListProperty<CityExchange> cityExchangesProperty() {
        return cityExchanges;
    }

    public ListProperty<DepartmentalExchange> departmentalExchangesProperty() {
        return departmentalExchanges;
    }

    public ListProperty<InstitutionalExchange> institutionalExchangesProperty() {
        return institutionalExchanges;
    }

    public StringProperty responseProperty() {
        return response;
    }

    public StringProperty selectedIdProperty() {
        return selectedId;
    }

    public StringProperty selectedNameProperty() {
        return selectedName;
    }

    public StringProperty selectedDepartmentNameProperty() {
        return selectedDepartmentName;
    }

    public StringProperty selectedInstitutionNameProperty() {
        return selectedInstitutionName;
    }

    private void setDisabledOperations() {
        boolean selectedIdIsNull = selectedId.get() == null,
                selectedParameterIsNull = selectedTab.getValue().fieldsAreNull();
        insertDisabled.set(selectedParameterIsNull);
        updateAndDeleteDisabled.set(selectedIdIsNull || selectedParameterIsNull);
    }

    private void setResponse(String message) {
        Platform.runLater(() -> response.set(message));
    }

    private void onExchangesReceived(Collection<Exchange> exchanges) {
        cityExchanges.setAll(exchanges.stream()
                .filter(e -> e instanceof CityExchange)
                .map(e -> (CityExchange) e)
                .collect(Collectors.toList()));
        departmentalExchanges.setAll(exchanges.stream()
                .filter(e -> e instanceof DepartmentalExchange)
                .map(e -> (DepartmentalExchange) e)
                .collect(Collectors.toList()));
        institutionalExchanges.setAll(exchanges.stream()
                .filter(e -> e instanceof InstitutionalExchange)
                .map(e -> (InstitutionalExchange) e)
                .collect(Collectors.toList()));
    }

    private void updateExchanges() {
        executorService.execute(() ->
                getExchanges.invoke(this::onExchangesReceived, this::setResponse));
    }
}
