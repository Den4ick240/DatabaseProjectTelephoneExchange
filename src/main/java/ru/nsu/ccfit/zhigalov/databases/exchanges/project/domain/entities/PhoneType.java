package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

public enum PhoneType {
    UNOCCUPIED, PUBLIC, PARALLEL, PAIRED, SIMPLE;

    public String russianString() {
        switch (this) {
            case UNOCCUPIED:
                return "Свободен";
            case PAIRED:
                return "Спаренный";
            case PUBLIC:
                return "Таксофон";
            case SIMPLE:
                return "Простой";
            case PARALLEL:
                return "Параллельный";
        }
        return null;
    }
}
