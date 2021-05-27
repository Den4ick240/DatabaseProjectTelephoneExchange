package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.client;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Person;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.PeopleTableInteractor;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.GetGenders;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.SignUpUseCase;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.Navigator;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.mvvm.ViewModel;

import java.util.concurrent.ExecutorService;

public class SignUpViewModel implements ViewModel {
    private final Navigator navigator;
    private final ExecutorService executorService;
    private final SignUpUseCase signUpUseCase;
    private final PeopleTableInteractor peopleTableInteractor;
    private final ListProperty<String> genderList;
    private final StringProperty responseString;
    private final StringProperty age;
    private final StringProperty id;

    public SignUpViewModel(Navigator navigator, ExecutorService executorService, GetGenders getGenders,
                           PeopleTableInteractor peopleTableInteractor, SignUpUseCase signUpUseCase) {
        this(navigator, executorService, getGenders,
                signUpUseCase, peopleTableInteractor, new SimpleListProperty<>(FXCollections.observableArrayList()),
                new SimpleStringProperty(), new SimpleStringProperty(), new SimpleStringProperty());
    }

    public SignUpViewModel(Navigator navigator, ExecutorService executorService, GetGenders getGenders,
                           SignUpUseCase signUpUseCase, PeopleTableInteractor peopleTableInteractor, ListProperty<String> genderList,
                           StringProperty responseString, StringProperty age, StringProperty id) {
        this.navigator = navigator;
        this.executorService = executorService;
        this.signUpUseCase = signUpUseCase;
        this.peopleTableInteractor = peopleTableInteractor;
        this.genderList = genderList;
        this.responseString = responseString;
        this.age = age;
        this.id = id;
        executorService.execute(() ->
                getGenders.invoke(genderList::setAll, this::setResponse));

        age.addListener((observable, oldValue, newValue) -> {
            String correctedValue = newValue;
            if (!newValue.matches("\\d*"))
                correctedValue = newValue.replaceAll("[^\\d]", "");
            if (correctedValue.length() > 3)
                correctedValue = correctedValue.substring(0, 3);
            age.setValue(correctedValue);
        });
    }

    public ObservableValue<ObservableList<String>> gendersProperty() {
        return genderList;
    }

    public ObservableValue<String> responseStringProperty() {
        return responseString;
    }

    public StringProperty ageProperty() {
        return age;
    }

    public StringProperty idProperty() {
        return id;
    }

    public void signUp(String name, String surname, String gender, boolean isBeneficiary, String password) {
        setResponse("Signing up...");
        executorService.execute(() ->
//                peopleTableInteractor.insert(
                signUpUseCase.signUp(
                        new Person(id.get(), name, surname, gender, Integer.parseInt(age.get()), isBeneficiary),
                        password,
                        () -> Platform.runLater(this::onSignedUp), this::setResponse));
    }

    public void onSignedUp() {
        setResponse("Signed up!");
        navigator.gotoClientScreen(id.get());
    }

    public void goBack() {
        navigator.gotoClientSignInScreen();
    }

    private void setResponse(String message) {
        Platform.runLater(() -> responseString.setValue(message));
    }
}
