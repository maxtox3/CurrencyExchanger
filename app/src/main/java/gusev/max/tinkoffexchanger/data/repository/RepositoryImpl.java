package gusev.max.tinkoffexchanger.data.repository;

import java.util.List;
import java.util.Set;

import gusev.max.tinkoffexchanger.data.cache.prefs.SharedPrefManager;
import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import gusev.max.tinkoffexchanger.data.model.dto.Exchange;
import gusev.max.tinkoffexchanger.data.model.vo.CurrencyVO;
import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import gusev.max.tinkoffexchanger.data.model.vo.FilterVO;
import gusev.max.tinkoffexchanger.data.model.vo.TrendsVO;
import gusev.max.tinkoffexchanger.data.repository.local.LocalDataSource;
import gusev.max.tinkoffexchanger.data.repository.remote.RemoteDataSource;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class RepositoryImpl implements Repository {

    private LocalDataSource localDataSource = new LocalDataSource();
    private RemoteDataSource remoteDataSource = new RemoteDataSource();
    private SharedPrefManager persistentDataSource = SharedPrefManager.getInstance();

    //<-------------------------Currency------------------------->

    @Override
    public Observable<CurrencyVO> loadCurrencies() {
        return refreshData()
                .doOnComplete(() -> persistentDataSource.writeLastTimeDownloaded(System.currentTimeMillis()))
                .andThen(getCachedCurrencies());
    }

    private Completable refreshData() {
        return Completable.merge(remoteDataSource.loadCurrencies()
                .map(currencies -> {
                    persistentDataSource.writeLastTimeDownloaded(System.currentTimeMillis());
                    return localDataSource.saveCurrencies(currencies);
                }));
    }

    @Override
    public Observable<CurrencyVO> getCachedCurrencies() {
        return Observable.just(localDataSource.getAnchoredCurrency())
                .flatMap((currency) -> localDataSource.getCurrencies(),
                        ((currency, currencies) -> new CurrencyVO(currencies, currency)))
                .flatMap(currencyVO -> currencyVO.getCurrencyList().isEmpty() ?
                        loadCurrencies() : Observable.just(currencyVO));
    }

    @Override
    public Completable updateCurrency(Currency currency) {
        return localDataSource.updateCurrency(currency);
    }

    @Override
    public Completable setAnchoredCurrency(Currency currency) {
        return Completable.fromAction(() -> localDataSource.setAnchoredCurrency(currency));
    }

    @Override
    public void setCurrencyForRates(Currency currency) {
        localDataSource.setCurrencyForRates(currency);
    }


    @Override
    public void setExchange(Exchange o) {
        localDataSource.setExchange(o);
    }

    //<-------------------------Exchange------------------------->

    @Override
    public Observable<ExchangeVO> getRates() {
        return localDataSource.getRates();
    }

    @Override
    public Observable<ExchangeVO> getRatesOrRefresh() {
        if (checkRatesFreshness()) {
            return refreshData().andThen(localDataSource.getRates());
        } else {
            return localDataSource.getRates();
        }
    }

    @Override
    public Boolean checkRatesFreshness() {
        return localDataSource.checkFreshness(persistentDataSource.readLastTimeDownloaded());
    }

    @Override
    public Completable exchange(ExchangeVO viewObject) {
        return localDataSource.saveExchange(viewObject);
    }

    @Override
    public void cacheExchange(ExchangeVO viewObject) {
        localDataSource.cacheExchange(viewObject);
    }



    //<-------------------------History------------------------->

    @Override
    public Observable<List<ExchangeVO>> getHistory() {
        return localDataSource.getFilter()
                .flatMap(filterVO ->
                        localDataSource.getExchangesByFilter(filterVO));
    }

    //<-------------------------Filters------------------------->

    @Override
    public Observable<FilterVO> getFilter() {
        return localDataSource.getFilter();
    }

    @Override
    public void setPeriodType(String type) {
        localDataSource.setPeriodType(type);
    }

    @Override
    public void setCheckers(Set<String> checkers) {
        localDataSource.setCheckers(checkers);
    }

    @Override
    public void setPeriodEdges(String dateFrom, String dateTo) {
        localDataSource.setPeriodEdges(dateFrom, dateTo);
    }


    public void saveFilter(FilterVO viewObject) {
        if(viewObject == null){
            localDataSource.removeFilterFromCache();
        }
        localDataSource.saveFilter(viewObject);
    }

    //<--------------------------Trends------------------------->

    @Override
    public Observable<TrendsVO> getTrends() {

        return Observable.zip(
                localDataSource.getCurrenciesForTrends()
                        .doOnNext(currencies -> {
                            if(localDataSource.isFirstTimeLoadTrendsCurrency()) {
                                localDataSource.setSelectedTrendsCurrency(currencies.get(0).getBase());
                                localDataSource.setFirstTimeLoadTrendsCurrency(false);
                            }
                        }),
                remoteDataSource.getRatesForTrends(
                        localDataSource.generateDatesForTrends(),
                        localDataSource.getSelectedTrendsCurrency())
                        .toObservable(),
                localDataSource::buildTrends);
    }

    @Override
    public void setTrendsPeriod(String period) {
        localDataSource.setSelectedPeriod(period);
    }

    @Override
    public void setSelectedTrendsCurrency(String selectedTrendsCurrency) {
        localDataSource.setSelectedTrendsCurrency(selectedTrendsCurrency);
    }

    @Override
    public FilterVO getFilterForHistory() {
        if(persistentDataSource.getFilter() == null){
            return null;
        } else {
            return persistentDataSource.getFilter();
        }
    }
}






