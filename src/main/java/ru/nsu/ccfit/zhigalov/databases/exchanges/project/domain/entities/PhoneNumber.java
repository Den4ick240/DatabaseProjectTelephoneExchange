package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

public class PhoneNumber {
    private String number;
    private PhoneType type;
    private Exchange exchange;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public PhoneType getType() {
        return type;
    }

    public void setType(PhoneType type) {
        this.type = type;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public PhoneNumber(String number, PhoneType type, Exchange exchange) {
        this.number = number;
        this.type = type;
        this.exchange = exchange;
    }

    @Override
    public String toString() {
        return number;
    }
}
