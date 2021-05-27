package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Person {
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public boolean isBeneficiary() {
        return isBeneficiary;
    }

    public String getId() {
        return id;
    }

    final String id;
    final String name;
    final String surname;
    final String gender;
    final int age;
    final boolean isBeneficiary;

    public Person(String id, String name, String surname, String gender, int age, boolean isBeneficiary) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.age = age;
        this.isBeneficiary = isBeneficiary;
    }

    public Person(ResultSet resultSet) throws SQLException {
        id = resultSet.getString("person_id");
        name = resultSet.getString("name");
        surname = resultSet.getString("surname");
        gender = resultSet.getString("gender");
        age = resultSet.getInt("age");
        isBeneficiary = resultSet.getInt("is_beneficiary") != 0;
    }
}
