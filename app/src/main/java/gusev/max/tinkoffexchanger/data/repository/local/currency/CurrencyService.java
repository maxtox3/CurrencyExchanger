package gusev.max.tinkoffexchanger.data.repository.local.currency;

import java.util.List;
import java.util.Objects;

import gusev.max.tinkoffexchanger.data.cache.database.CurrencyDao;
import gusev.max.tinkoffexchanger.data.cache.database.DatabaseFactory;
import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public class CurrencyService implements CurrenciesServiceProtocol {

    private CurrencyDao currencyDao = DatabaseFactory.getCurrencyDao();
    private Currency anchoredCurrency;
    private Currency currencyForRates;

    @Override
    public Currency getAnchoredCurrency() {
        return anchoredCurrency == null ? new Currency() : anchoredCurrency;
    }

    @Override
    public void setAnchoredCurrency(Currency anchoredCurrency) {
        this.anchoredCurrency = anchoredCurrency;
    }

    @Override
    public Observable<List<Currency>> getCurrencies() {
        return Observable.fromCallable(this::getAnchoredCurrency)
                .flatMap(currency -> currency.getBase() == null ?
                        currencyDao.getAllCurrencies().toObservable() :
                        currencyDao.getCurrenciesWithoutAnchored(currency.getBase()).toObservable()
                );
    }

    @Override
    public Completable saveCurrencies(List<Currency> currencies) {
        return Flowable.just(currencies)
                .flatMap(Flowable::fromIterable)
                .doOnNext(this::saveCurrency)
                .filter(currency -> !Objects.equals(currency.getBase(), this.getAnchoredCurrency().getBase()))
                .toList()
                .toCompletable();
    }

    private Completable saveCurrency(Currency currency) {
        Currency savedInDb = currencyDao.getCurrency(currency.getBase());
        if (savedInDb != null) {
            currency.setLiked(savedInDb.getLiked());
            currency.setLastUsed(savedInDb.getLastUsed());
            if (anchoredCurrency != null) {
                if (anchoredCurrency.getBase().equals(currency.getBase())) {
                    setAnchoredCurrency(currency);
                }
            }
        }
        currencyDao.insert(currency);
        return Completable.complete();
    }

    @Override
    public Completable updateCurrency(Currency currency) {
        return Completable.fromAction(() -> currencyDao.insert(currency));
    }

    @Override
    public void setCurrencyForRates(Currency currencyForRates) {
        this.currencyForRates = currencyForRates;
    }

    public Currency getCurrencyForRates(){
        return currencyForRates;
    }
}
