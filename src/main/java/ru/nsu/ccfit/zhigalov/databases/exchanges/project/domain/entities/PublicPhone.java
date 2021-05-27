package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

public class PublicPhone {
    private Address address;
    private PhoneNumber phoneNumber;
    private int id;

    public PublicPhone() {}

    public PublicPhone(Address address, PhoneNumber phoneNumber) {
       this(address, phoneNumber, 0);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PublicPhone(Address address, PhoneNumber phoneNumber, int id) {
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }

}
