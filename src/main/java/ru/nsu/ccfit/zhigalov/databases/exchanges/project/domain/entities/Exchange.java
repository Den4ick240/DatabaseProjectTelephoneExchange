package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

public class Exchange {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Exchange(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "АТС под номером " + id;
    }

    protected int id;
}
