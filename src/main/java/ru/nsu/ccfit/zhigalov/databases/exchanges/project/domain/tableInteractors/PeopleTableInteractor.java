package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Person;

public class PeopleTableInteractor extends AbstractTableInteractor<Person> {
    public PeopleTableInteractor(DatabaseController databaseController) {
        super(databaseController);
    }

    @Override
    protected String getUpdateQuery(Person object) {
        throw new UnsupportedOperationException("Update unsupported");
    }

    @Override
    protected String getDeleteQuery(Person object) {
        throw new UnsupportedOperationException("Delete unsupported");
    }

    @Override
    protected String getInsertQuery(Person person) {
        return String.format(
                "insert into people(person_id, name, surname, age, gender, is_beneficiary) " +
                        "values('%s','%s','%s','%d','%s','%d')",
                person.getId(), person.getName(), person.getSurname(), person.getAge(), person.getGender(),
                person.isBeneficiary() ? 1 : 0);
    }
}
