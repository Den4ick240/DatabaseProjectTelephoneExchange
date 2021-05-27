package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseConnector;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Person;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.Listener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.DatabaseInteractor;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors.PeopleTableInteractor;

public class SignUpUseCase {
    private final UserCreator userCreator;
    private final DatabaseConnector databaseConnector;
    private final PeopleTableInteractor peopleTableInteractor;
    private final DatabaseInteractor databaseInteractor;
    private final SignInUseCase signInUseCase;

    private boolean errorOccurred = false;


    public SignUpUseCase(UserCreator userCreator, DatabaseConnector databaseConnector,
                         PeopleTableInteractor peopleTableInteractor,
                         DatabaseInteractor databaseInteractor, SignInUseCase signInUseCase) {
        this.userCreator = userCreator;
        this.databaseConnector = databaseConnector;
        this.peopleTableInteractor = peopleTableInteractor;
        this.databaseInteractor = databaseInteractor;
        this.signInUseCase = signInUseCase;
    }

    public void signUp(Person person, String password, Listener listener, ErrorListener errorListener) {
        ErrorListener errorListenerWrapper = message -> {
            errorOccurred = true;
            errorListener.onError(message);
        };
        userCreator.createUser(person.getId(), password, () -> {}, errorListenerWrapper);
        if (errorOccurred) return;
        peopleTableInteractor.insert(person, () -> {}, errorListenerWrapper);
        if (errorOccurred) return;
        signInUseCase.signIn(person.getId(), password, listener, errorListener);
    }
}
