package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.Consts;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ObjectListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public class GetPersonById {
    private final DatabaseController controller;
    private final ExecutorService executorService;

    public GetPersonById(DatabaseController controller, ExecutorService executorService) {
        this.controller = controller;
        this.executorService = executorService;
    }

    public void invoke(String id, ObjectListener<Person> personListener, ErrorListener errorListener) {
        Runnable task = () -> {
            try {
                ResultSet resultSet = controller.executeQuery("SELECT * FROM " + Consts.getSchemaName()
                        + "People where person_id = '" + id + "'");
                Person person;
                resultSet.next();
                person = new Person(resultSet.getString("person_id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("gender"),
                        resultSet.getInt("age"),
                        0 != resultSet.getInt("is_beneficiary"));
                personListener.onReceived(person);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                errorListener.onError(throwables.getMessage());
            }
        };
        executorService.execute(task);
    }
}
