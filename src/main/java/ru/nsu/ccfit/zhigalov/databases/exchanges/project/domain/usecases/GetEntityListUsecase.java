package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ObjectListener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class GetEntityListUsecase<T> {
    private final DatabaseController databaseController;

    public interface ItemFactory<T> {
        T getItem(ResultSet resultSet) throws SQLException;
    }

    public GetEntityListUsecase(DatabaseController databaseController) {
        this.databaseController = databaseController;
    }

    public void invoke(ItemFactory<T> itemFactory, String query, ObjectListener<Collection<T>> listener, ErrorListener errorListener) {
        List<T> list = new LinkedList<T>();
        try {
            ResultSet resultSet = databaseController.executeQuery(query);
            while (resultSet.next()) {
                list.add(itemFactory.getItem(resultSet));
            }
            listener.onReceived(list);
        } catch (SQLException e) {
            e.printStackTrace();
            errorListener.onError(e.getMessage());
        }

    }
}
