package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

public class RequestWithNumberOfCables {
    private final Request request;
    private final int numberOfCables;
    private final int freeCables;

    public Request getRequest() {
        return request;
    }

    public int getNumberOfCables() {
        return numberOfCables;
    }

    public RequestWithNumberOfCables(Request request, int numberOfCables, int freeCables) {
        this.request = request;
        this.numberOfCables = numberOfCables;
        this.freeCables = freeCables;
    }

    public Integer getFreeCables() {
        return freeCables;
    }
}
