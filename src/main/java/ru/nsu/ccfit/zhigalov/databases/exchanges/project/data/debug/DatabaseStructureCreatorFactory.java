package ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.debug;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;

import java.util.Collection;

public class DatabaseStructureCreatorFactory {

    private static final String[] CREATES = {
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/create/tables.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/create/exchanges.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/create/publicPhones.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/create/views.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/create/queue.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/create/subscribers.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/create/roles.sql"
    };
    private static final String[] DROPS = {
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/drop/subscribers.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/drop/queue.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/drop/views.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/drop/publicPhones.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/drop/exchanges.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/drop/roles.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/drop/tables.sql"
    };

    private static final String[] INSERTS = {
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/insert/prices.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/insert/addresses.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/insert/exchanges.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/insert/exchangesWithAddresses.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/insert/phoneTypes.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/insert/phoneNumbers.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/insert/publicPhones.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/insert/cities.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/insert/genders.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/insert/people.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/insert/queue.sql",
            "/ru/nsu/ccfit/zhigalov/databases/exchanges/project/data/debugQueries/insert/subscribers.sql"
    };
    private final Collection<String> create, insert, drop;

    public DatabaseStructureCreatorFactory(QueryFileReader queryFileReader) {
        this.create = queryFileReader.getQueries(CREATES);
        this.drop = queryFileReader.getQueries(DROPS);
        this.insert = queryFileReader.getQueries(INSERTS);
    }

    public DatabaseStructureCreator newDatabaseStructureCreator(DatabaseController controller) {
        return new DatabaseStructureCreator(controller, drop, create, insert);
    }
}
