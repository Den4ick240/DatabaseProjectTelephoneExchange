package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities;

public class InstitutionalExchange extends Exchange {
    private String institutionName;

    public InstitutionalExchange(int exchangeId, String institutionName) {
        super(exchangeId);
        this.institutionName = institutionName;
    }

    @Override
    public String toString() {
        return "Учрежденческая АТС: " + institutionName;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }
}
