package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Person;

public class PersonFilter extends AbstractFilter {
    public PersonFilter(Person person) {
        super("person_id = '" + person.getId() + "'");
    }
}
