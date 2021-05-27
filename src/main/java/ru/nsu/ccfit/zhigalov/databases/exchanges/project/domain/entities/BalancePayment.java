package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

public class BalancePayment {
    private final double value;
    private final Subscription subscription;

    public BalancePayment(double value, Subscription subscription) {
        this.value = value;
        this.subscription = subscription;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public double getValue() {
        return value;
    }
}
