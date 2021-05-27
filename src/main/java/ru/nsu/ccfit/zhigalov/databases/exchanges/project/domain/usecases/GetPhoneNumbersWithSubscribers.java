package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Address;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Exchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PhoneNumber;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ErrorListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.Listener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.listener.ObjectListener;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters.ExchangeFilter;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters.Filter;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters.PhoneNumberFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class GetPhoneNumbersWithSubscribers {
    private final GetSubscribers getSubscribers;
    private final GetPhoneNumbers getPhoneNumbers;
    private ErrorListener errorListener;
    private ObjectListener<Collection<PhoneNumberWithSubscribers>> listener;
    volatile boolean errorHappened = false;
    private Address address;

    public GetPhoneNumbersWithSubscribers(DatabaseController databaseController, GetSubscribers getSubscribers, GetPhoneNumbers getPhoneNumbers) {
        this.getSubscribers = getSubscribers;
        this.getPhoneNumbers = getPhoneNumbers;
    }

    public void invoke(ObjectListener<Collection<PhoneNumberWithSubscribers>> listener, ErrorListener errorListener, Exchange exchange, Address address) {
        this.errorListener = errorListener;
        this.listener = listener;
        this.address = address;
        getPhoneNumbers.setFilters(Arrays.asList(
                new ExchangeFilter(exchange),
                () -> "not type = 'PUBLIC'",
                () -> String.format(
                        "not exists(select phone_number as pn from (select street, house, phone_number as pn from subscribers_view) s " +
                                "where (not (s.street = '%s' and s.house = '%s')) and pn = phone_number)",
                        address.getStreet(), address.getHouse())
                ));
        getPhoneNumbers.invoke(this::onPhoneNumbersReceived, errorListener);
    }

    private void onPhoneNumbersReceived(Collection<PhoneNumber> phoneNumbers) {
        Collection<PhoneNumberWithSubscribers> phoneNumbersWithSubscribers = new ArrayList<>();
        for (PhoneNumber phoneNumber : phoneNumbers) {
            getSubscribers.setFilters(Collections.singletonList(new PhoneNumberFilter(phoneNumber)));
            getSubscribers.invoke(
                    subscribers -> phoneNumbersWithSubscribers.add(new PhoneNumberWithSubscribers(phoneNumber, subscribers)),
                    this::onError
            );
            if (errorHappened) break;
        }
        listener.onReceived(phoneNumbersWithSubscribers);
    }

    private void onError(String message) {
        errorListener.onError(message);
        errorHappened = true;
    }
}
