package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PhoneNumber;

public class PhoneNumberFilter extends AbstractFilter {
    public PhoneNumberFilter(PhoneNumber phoneNumber) {
        super(String.format(
                "exchange_id = %d and phone_number = %s", phoneNumber.getExchange().getId(), phoneNumber.getNumber()
        ));
    }
}
