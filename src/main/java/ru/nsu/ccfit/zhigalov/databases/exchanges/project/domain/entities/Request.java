package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;


import java.sql.Date;

public class Request {
    private final Integer id;
    private final Person person;
    private final Address address;
    private final String apartment;
    private final Exchange exchange;
    private final Date requestDate;

    public Request(Integer id, Address address, String apartment,
                   Exchange exchange, Person person) {
        this(id, address, apartment, exchange, person,
                new Date(System.currentTimeMillis()));
    }

    public Request(Integer id, Address address, String apartment,
                   Exchange exchange, Person person, Date requestDate) {
        this.person = person;
        this.address = address;
        this.apartment = apartment;
        this.exchange = exchange;
        this.id = id;
        this.requestDate = requestDate;
    }

    public Person getPerson() {
        return person;
    }

    public Address getAddress() {
        return address;
    }

    public String getApartment() {
        return apartment;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return requestDate;
    }
}
