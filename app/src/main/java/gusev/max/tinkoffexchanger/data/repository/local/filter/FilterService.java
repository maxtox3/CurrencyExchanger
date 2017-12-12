package gusev.max.tinkoffexchanger.data.repository.local.filter;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import gusev.max.tinkoffexchanger.data.cache.database.DatabaseFactory;
import gusev.max.tinkoffexchanger.data.cache.database.ExchangeDao;
import gusev.max.tinkoffexchanger.data.model.dto.Filter;
import gusev.max.tinkoffexchanger.data.model.vo.FilterVO;
import io.reactivex.Observable;

public class FilterService implements FiltersServiceProtocol {

    private ExchangeDao exchangeDao = DatabaseFactory.getExchangeDao();
    private Filter filter;

    @Override
    public Observable<FilterVO> getFilter() {
        return exchangeDao.getAllCurrencies()
                .map(currencies -> {
                    if (filter == null) {

                        filter = new Filter(currencies, new HashSet<>(),
                                "all_time",getNowDateString(),getNowDateString());

                        return new FilterVO(currencies, new HashSet<>(),
                                "all_time", getNowDateString(),getNowDateString());
                    } else if(filter.getCurrencies() == null) {
                        return new FilterVO(
                                currencies,
                                filter.getChecks(),
                                filter.getPeriodType(),
                                filter.getDateTo(),
                                filter.getDateFrom()
                        );
                    } else {
                        return new FilterVO(
                                filter.getCurrencies(),
                                filter.getChecks(),
                                filter.getPeriodType(),
                                filter.getDateTo(),
                                filter.getDateFrom()
                        );
                    }
                }).toObservable();
    }

    @Override
    public void setPeriodType(String periodType) {
        filter.setPeriodType(periodType);
    }

    @Override
    public void setCheckers(Set<String> checkers) {
        if(filter != null) {
            filter.setChecks(checkers);
        }
    }

    @Override
    public void setPeriodEdges(String dateFrom, String dateTo) {
        filter.setDateFrom(dateFrom);
        filter.setDateTo(dateTo);
    }

    @Override
    public void removeFilterFromCache() {
        this.filter = null;
    }

    @Override
    public void saveFilter(FilterVO viewObject) {
        if(viewObject != null) {
            this.filter = new Filter(
                    viewObject.getCurrencies(),
                    viewObject.getChecks(),
                    viewObject.getPeriodType(),
                    viewObject.getDateTo(),
                    viewObject.getDateFrom());
        }
    }


    private String getNowDateString() {
        return new SimpleDateFormat("dd/MM/yyyy")
                .format(System.currentTimeMillis());
    }
}
