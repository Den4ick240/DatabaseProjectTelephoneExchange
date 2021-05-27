package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.publicphones;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PublicPhone;

public class PublicPhoneRow {

    private final PublicPhone publicPhone;

    public PublicPhoneRow(PublicPhone publicPhone) {
        this.publicPhone = publicPhone;
    }

    public PublicPhone getPublicPhone() {
        return publicPhone;
    }

    public String getPhoneNumber() {
        return publicPhone.getPhoneNumber().getNumber();
    }

    public String getAddress() {
        return publicPhone.getAddress().getStreet() + ", " + publicPhone.getAddress().getHouse();
    }

    public String getExchange() {
        return publicPhone.getPhoneNumber().getExchange().toString();
    }
}
