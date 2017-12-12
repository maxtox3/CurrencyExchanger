package gusev.max.tinkoffexchanger.data.repository;

import java.util.List;
import java.util.Set;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import gusev.max.tinkoffexchanger.data.model.dto.Exchange;
import gusev.max.tinkoffexchanger.data.model.vo.CurrencyVO;
import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import gusev.max.tinkoffexchanger.data.model.vo.FilterVO;
import gusev.max.tinkoffexchanger.data.model.vo.TrendsVO;
import io.reactivex.Completable;
import io.reactivex.Observable;

public interface Repository {

    Observable<CurrencyVO> loadCurrencies();

    Observable<CurrencyVO> getCachedCurrencies();

    Completable updateCurrency(Currency currency);

    Completable setAnchoredCurrency(Currency currency);

    void setCurrencyForRates(Currency currency);


    Observable<ExchangeVO> getRates();

    Observable<ExchangeVO> getRatesOrRefresh();

    Boolean checkRatesFreshness();

    Completable exchange(ExchangeVO viewObject);

    void cacheExchange(ExchangeVO viewObject);


    Observable<List<ExchangeVO>> getHistory();


    Observable<FilterVO> getFilter();

    void setPeriodType(String type);

    void setCheckers(Set<String> checkers);

    void setPeriodEdges(String dateFrom, String dateTo);

    void saveFilter(FilterVO viewObject);


    Observable<TrendsVO> getTrends();

    void setTrendsPeriod(String period);

    void setSelectedTrendsCurrency(String selectedTrendsCurrency);

    FilterVO getFilterForHistory();

    void setExchange(Exchange o);
}
