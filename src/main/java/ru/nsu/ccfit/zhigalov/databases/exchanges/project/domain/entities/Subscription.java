package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Subscription {
    private final int id;
    private final PhoneNumber phoneNumber;
    private final Address address;
    private final String apartment;
    private final Person person;
    private final Double balance;
    private final BalanceState state, ldState;
    private final Date expiredDate, ldExpiredDate;

    public Subscription(int id,
                        Person person,
                        Address address,
                        PhoneNumber phoneNumber,
                        String apartment,
                        Double balance,
                        BalanceState state,
                        BalanceState ldState,
                        Date expiredDate,
                        Date ldExpiredDate) {
        this.person = person;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.apartment = apartment;
        this.id = id;
        this.balance = balance;
        this.state = state;
        this.ldState = ldState;
        this.expiredDate = expiredDate;
        this.ldExpiredDate = ldExpiredDate;
    }

    public Subscription(ResultSet resultSet, Exchange exchange) throws SQLException {
        person = new Person(resultSet.getString("person_id"), resultSet.getString("person_name"), resultSet.getString("surname"),
                resultSet.getString("gender"), resultSet.getInt("age"),
                resultSet.getInt("is_beneficiary") == 1);
        id = resultSet.getInt("subscriber_id");
        phoneNumber = new PhoneNumber(resultSet.getString("phone_number"),
                PhoneType.valueOf(resultSet.getString("type")), exchange);
        address = new Address(resultSet);
        apartment = resultSet.getString("apartment");
        balance = resultSet.getDouble("balance");
        state = BalanceState.valueOf(resultSet.getString("state"));
        ldState = BalanceState.valueOf(resultSet.getString("ld_state"));
        expiredDate = resultSet.getDate("expired_date");
        ldExpiredDate = resultSet.getDate("ld_expired_date");
    }

    public Person getPerson() {
        return person;
    }

    public Address getAddress() {
        return address;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public String getApartment() {
        return apartment;
    }

    public Integer getId() {
        return id;
    }

    public Double getBalance() {
        return balance;
    }

    public BalanceState getState() {
        return state;
    }

    public BalanceState getLdState() {
        return ldState;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public Date getLdExpiredDate() {
        return ldExpiredDate;
    }

    @Override
    public String toString() {
        return phoneNumber.toString();
    }
}
