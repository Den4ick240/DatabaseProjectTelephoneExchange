package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Exchange;

import java.util.concurrent.Exchanger;

public class ExchangeFilter extends AbstractFilter {
    public ExchangeFilter(Exchange exchange) {
        super("exchange_id = " + Integer.toString(exchange.getId()));
    }
}
