package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

public enum BalanceState {
    payed, expired, disabled;
    public String toRussianString() {
        switch(this) {
            case payed: return "Оплачено";
            case expired: return "Просрочено";
            case disabled: return "Отключено";
        }
        return null;
    }
}
