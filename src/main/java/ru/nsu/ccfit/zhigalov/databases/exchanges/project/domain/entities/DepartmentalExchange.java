package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

public class DepartmentalExchange extends Exchange {
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    private String departmentName;
    public DepartmentalExchange(int exchangeId, String departmentName) {
        super(exchangeId);
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "Ведомственная АТС: " + departmentName;
    }
}
