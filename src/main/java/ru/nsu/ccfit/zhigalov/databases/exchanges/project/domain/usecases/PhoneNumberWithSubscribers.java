package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PhoneNumber;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Subscription;

import java.util.Collection;

public class PhoneNumberWithSubscribers {
    private final PhoneNumber phoneNumber;
    private final Collection<Subscription> subscribers;

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public Collection<Subscription> getSubscribers() {
        return subscribers;
    }

    public PhoneNumberWithSubscribers(PhoneNumber phoneNumber, Collection<Subscription> subscribers) {
        this.phoneNumber = phoneNumber;
        this.subscribers = subscribers;
    }
}
