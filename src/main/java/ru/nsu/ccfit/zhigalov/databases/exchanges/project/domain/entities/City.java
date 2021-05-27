package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

public class City {
    private final String name;

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public City(String name) {
        this.name = name;
    }
}
