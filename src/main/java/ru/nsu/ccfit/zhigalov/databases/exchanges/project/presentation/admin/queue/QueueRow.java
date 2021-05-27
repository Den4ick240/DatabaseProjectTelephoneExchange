package ru.nsu.ccfit.zhigalov.databases.exchanges.project.presentation.admin.queue;

import javafx.beans.property.StringProperty;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Exchange;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.Request;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.RequestWithNumberOfCables;

public class QueueRow {
    private final Request request;
    private final RequestWithNumberOfCables requestWithNumberOfCables;

    public QueueRow(RequestWithNumberOfCables requestWithNumberOfCables) {
        request = requestWithNumberOfCables.getRequest();
        this.requestWithNumberOfCables = requestWithNumberOfCables;
    }

    public Request getRequest() {
        return request;
    }

    public Integer getRequestId() {
        return request.getId();
    }

    public String getPersonId() {
        return request.getPerson().getId();
    }

    public String getName() {
        return request.getPerson().getName();
    }

    public String getSurname() {
        return request.getPerson().getSurname();
    }

    public String getBeneficiary() {
        return request.getPerson().isBeneficiary() ? "да" : "нет";
    }

    public Exchange getExchange() {
        return request.getExchange();
    }

    public String getRequestDate() {
        return request.getDate().toString();
    }

    public String getAddress() {
        return request.getAddress().toString() + " " + request.getApartment();
    }

    public Integer getNumberOfCables() {
        return requestWithNumberOfCables.getNumberOfCables();
    }

    public Integer getFreeCables() {
        return requestWithNumberOfCables.getFreeCables();
    }
}
