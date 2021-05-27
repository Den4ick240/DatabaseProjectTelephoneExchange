package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.usecases.filters;

import java.util.Collection;
import java.util.Iterator;

public class ConcatenateFilters {
    public String concatenate(Collection<Filter> filters) {
        StringBuilder query = new StringBuilder();
        if (!filters.isEmpty())
            query.append(" where ");
        for (Iterator<Filter> it = filters.iterator(); it.hasNext();) {
            query.append(it.next().getSqlWhereClause());
            if (it.hasNext())
                query.append(" and ");
        }
        return query.toString();
    }
}
