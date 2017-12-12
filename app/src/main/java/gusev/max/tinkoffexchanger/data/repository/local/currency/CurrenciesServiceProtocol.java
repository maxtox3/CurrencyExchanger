package gusev.max.tinkoffexchanger.data.repository.local.currency;

import java.util.List;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import io.reactivex.Completable;
import io.reactivex.Observable;

public interface CurrenciesServiceProtocol {

    void setAnchoredCurrency(Currency anchoredCurrency);

    Observable<List<Currency>> getCurrencies();

    Completable saveCurrencies(List<Currency> currencies);

    Completable updateCurrency(Currency currency);

    void setCurrencyForRates(Currency currencyForRates);

    Currency getAnchoredCurrency();

    Currency getCurrencyForRates();

}
