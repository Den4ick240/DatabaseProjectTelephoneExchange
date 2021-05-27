package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

public class CityExchange extends Exchange {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Городская АТС: " + name;
    }

    public CityExchange(int id, String name) {
        super(id);
        this.name = name;
    }

    private String name;
}