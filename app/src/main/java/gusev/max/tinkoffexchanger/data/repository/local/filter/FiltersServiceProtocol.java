package gusev.max.tinkoffexchanger.data.repository.local.filter;

import java.util.Set;

import gusev.max.tinkoffexchanger.data.model.vo.FilterVO;
import io.reactivex.Observable;

public interface FiltersServiceProtocol {

    Observable<FilterVO> getFilter();

    void setPeriodType(String periodType);

    void setCheckers(Set<String> checkers);

    void setPeriodEdges(String dateFrom, String dateTo);

    void removeFilterFromCache();

    void saveFilter(FilterVO viewObject);
}
