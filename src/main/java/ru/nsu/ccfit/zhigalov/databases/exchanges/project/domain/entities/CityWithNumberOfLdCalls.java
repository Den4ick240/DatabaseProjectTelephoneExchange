package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

public class CityWithNumberOfLdCalls {
    private final City city;
    private final Integer numberOfCalls;

    public City getCity() {
        return city;
    }

    public Integer getNumberOfCalls() {
        return numberOfCalls;
    }

    public CityWithNumberOfLdCalls(City city, Integer numberOfCalls) {
        this.city = city;
        this.numberOfCalls = numberOfCalls;
    }
}
