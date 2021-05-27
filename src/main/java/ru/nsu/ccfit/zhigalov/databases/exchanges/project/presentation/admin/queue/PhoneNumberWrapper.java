package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.queue;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PhoneNumber;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.PhoneNumberWithSubscribers;

public class PhoneNumberWrapper {
    private final PhoneNumberWithSubscribers phoneNumberWithSubscribers;
    private final PhoneNumber phoneNumber;

    public PhoneNumberWrapper(PhoneNumberWithSubscribers phoneNumberWithSubscribers) {
        this.phoneNumberWithSubscribers = phoneNumberWithSubscribers;
        this.phoneNumber = phoneNumberWithSubscribers.getPhoneNumber();
    }

    public PhoneNumberWithSubscribers getPhoneNumberWithSubscribers() {
        return phoneNumberWithSubscribers;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return phoneNumber.getNumber() + " " + phoneNumber.getType().russianString() + " " + phoneNumberWithSubscribers.getSubscribers().size() + " пользователей";
    }
}
