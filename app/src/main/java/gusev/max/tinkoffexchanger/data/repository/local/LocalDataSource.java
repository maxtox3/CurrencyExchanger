package gusev.max.tinkoffexchanger.data.repository.local;

import java.util.List;
import java.util.Set;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import gusev.max.tinkoffexchanger.data.model.dto.Exchange;
import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import gusev.max.tinkoffexchanger.data.model.vo.FilterVO;
import gusev.max.tinkoffexchanger.data.model.vo.TrendsVO;
import gusev.max.tinkoffexchanger.data.repository.local.currency.CurrenciesServiceProtocol;
import gusev.max.tinkoffexchanger.data.repository.local.currency.CurrencyService;
import gusev.max.tinkoffexchanger.data.repository.local.exchange.ExchangeService;
import gusev.max.tinkoffexchanger.data.repository.local.exchange.ExchangeServiceProtocol;
import gusev.max.tinkoffexchanger.data.repository.local.filter.FilterService;
import gusev.max.tinkoffexchanger.data.repository.local.filter.FiltersServiceProtocol;
import gusev.max.tinkoffexchanger.data.repository.local.history.HistoryService;
import gusev.max.tinkoffexchanger.data.repository.local.history.HistoryServiceProtocol;
import gusev.max.tinkoffexchanger.data.repository.local.trends.TrendsService;
import gusev.max.tinkoffexchanger.data.repository.local.trends.TrendsServiceProtocol;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class LocalDataSource {

    private CurrenciesServiceProtocol currenciesService = new CurrencyService();
    private ExchangeServiceProtocol exchangeService = new ExchangeService();
    private HistoryServiceProtocol historyService = new HistoryService();
    private FiltersServiceProtocol filtersService = new FilterService();
    private TrendsServiceProtocol trendsService = new TrendsService();

//    private CurrencyDao currencyDao = DatabaseFactory.getCurrencyDao();
//    private ExchangeDao exchangeDao = DatabaseFactory.getExchangeDao();
//    private Currency anchoredCurrency;
//    private Currency currencyForRates;
//    private String selectedPeriod;
//    private Exchange exchange;
//    private Filter filter;
//    private String selectedTrendsCurrency;
//    private boolean firstTimeLoadTrendsCurrency = true;

    public Currency getAnchoredCurrency() {
        return currenciesService.getAnchoredCurrency();
    }

    public void setAnchoredCurrency(Currency anchoredCurrency) {
        currenciesService.setAnchoredCurrency(anchoredCurrency);
    }

    public Observable<List<Currency>> getCurrencies() {
        return currenciesService.getCurrencies();
    }

    public Completable saveCurrencies(List<Currency> currencies) {
        return currenciesService.saveCurrencies(currencies);
    }

    public Completable updateCurrency(Currency currency) {
        return currenciesService.updateCurrency(currency);
    }

    public void setCurrencyForRates(Currency currencyForRates) {
        currenciesService.setCurrencyForRates(currencyForRates);
    }

    //<-------------------------Exchange------------------------->

    public Observable<ExchangeVO> getRates() {
        return exchangeService.getRates(
                currenciesService.getAnchoredCurrency(),
                currenciesService.getCurrencyForRates()
        );
    }

    public Completable saveExchange(ExchangeVO exchangeVO) {
        currenciesService.setCurrencyForRates(null);
        currenciesService.setAnchoredCurrency(null);
        return exchangeService.saveExchange(exchangeVO);
    }

    public void cacheExchange(ExchangeVO viewObject) {
        exchangeService.cacheExchange(viewObject);
    }

    public void setExchange(Exchange exchange) {
        exchangeService.setExchange(exchange);
    }

    //<-------------------------History------------------------->

    public Observable<List<ExchangeVO>> getExchangesByFilter(FilterVO filter) {
        return historyService.getExchangesByFilter(filter);
    }

    //<-------------------------Filters------------------------->

    public Observable<FilterVO> getFilter() {
        return filtersService.getFilter();
    }

    public void setPeriodType(String periodType) {
        filtersService.setPeriodType(periodType);
    }

    public void setCheckers(Set<String> checkers) {
        filtersService.setCheckers(checkers);
    }

    public void setPeriodEdges(String dateFrom, String dateTo) {
        filtersService.setPeriodEdges(dateFrom, dateTo);
    }

    public void removeFilterFromCache() {
        filtersService.removeFilterFromCache();
    }

    public void saveFilter(FilterVO viewObject) {
        filtersService.saveFilter(viewObject);
    }

    //<-------------------------Trends------------------------->

    public Observable<List<Currency>> getCurrenciesForTrends() {
        return trendsService.getCurrenciesForTrends(currenciesService.getAnchoredCurrency());
    }

    public void setSelectedPeriod(String selectedPeriod) {
        trendsService.setSelectedPeriod(selectedPeriod);
    }

    public String getSelectedTrendsCurrency() {
        return trendsService.getSelectedTrendsCurrency();
    }

    public void setSelectedTrendsCurrency(String selectedTrendsCurrency){
        trendsService.setSelectedTrendsCurrency(selectedTrendsCurrency);
    }

    public boolean isFirstTimeLoadTrendsCurrency() {
        return trendsService.isFirstTimeLoadTrendsCurrency();
    }

    public void setFirstTimeLoadTrendsCurrency(boolean firstTimeLoadTrendsCurrency) {
        trendsService.setFirstTimeLoadTrendsCurrency(firstTimeLoadTrendsCurrency);
    }

    public TrendsVO buildTrends(List<Currency> currencies, List<Float> rates) {
        return trendsService.buildTrends(currencies, rates);
    }

    public String[] generateDatesForTrends() {
        return trendsService.generateDatesForTrends();
    }

    public Boolean checkFreshness(long lastTime) {
        long now = System.currentTimeMillis();
        long millis = now - lastTime;
        int minutes = (int) ((millis / (1000 * 60)) % 60);
        return minutes >= 5;
    }
}
