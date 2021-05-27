package ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.debug;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;

import java.sql.SQLException;


public class DatabaseStructureCreator {
    private final DatabaseController controller;

    private final Iterable<String> dropQueries, createQueries, insertQueries;

    public DatabaseStructureCreator(DatabaseController controller, Iterable<String> dropQueries, Iterable<String> createQueries, Iterable<String> insertQueries) {
        this.controller = controller;
        this.dropQueries = dropQueries;
        this.createQueries = createQueries;
        this.insertQueries = insertQueries;
    }



    private void executeUpdateQueries(Iterable<String> queries) {
        for (String query : queries) {
            try {
                System.out.println("Executing: \n" + query);
                controller.executeUpdateQuery(query);
                System.out.println("Success");
            } catch (SQLException e) {
                System.out.println("exception while executing query: " + query);
                e.printStackTrace();
            }
        }
    }



    public void createDatabaseStructure() {
        executeUpdateQueries(createQueries);
    }

    public void dropDatabaseStructure() {
        executeUpdateQueries(dropQueries);
    }

    public void loadTestData() {
        executeUpdateQueries(insertQueries);
    }
}
