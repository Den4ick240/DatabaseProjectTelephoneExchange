package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters;

public abstract class AbstractFilter implements Filter {
    private final String filter;

    protected AbstractFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public String getSqlWhereClause() {
        return filter;
    }
}
