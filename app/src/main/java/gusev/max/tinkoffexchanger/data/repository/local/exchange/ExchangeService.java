package gusev.max.tinkoffexchanger.data.repository.local.exchange;

import gusev.max.tinkoffexchanger.data.cache.database.CurrencyDao;
import gusev.max.tinkoffexchanger.data.cache.database.DatabaseFactory;
import gusev.max.tinkoffexchanger.data.cache.database.ExchangeDao;
import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import gusev.max.tinkoffexchanger.data.model.dto.Exchange;
import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class ExchangeService implements ExchangeServiceProtocol {

    private CurrencyDao currencyDao = DatabaseFactory.getCurrencyDao();
    private ExchangeDao exchangeDao = DatabaseFactory.getExchangeDao();
    private Exchange exchange;


    @Override
    public Observable<ExchangeVO> getRates(Currency anchoredCurrency, Currency currencyForRates) {
        if (anchoredCurrency.getBase() != null) {
            return Observable.zip(
                    currencyDao.getCurrencyFlowable(anchoredCurrency.getBase()).toObservable(),
                    currencyDao.getCurrencyFlowable(currencyForRates.getBase()).toObservable(),
                    this::buildRates);
        } else {
            return currencyDao.getCurrencyFlowable(currencyForRates.getBase())
                    .toObservable()
                    .flatMap((currency) -> getSecondCurrencyForExchange(currency.getBase())
                                    .switchIfEmpty(getUsdOrRub(currencyForRates.getBase())),
                            this::buildRates);
        }
    }

    @Override
    public Completable saveExchange(ExchangeVO exchangeVO) {
        Exchange exchange = new Exchange(
                exchangeVO.getBaseFrom(),
                exchangeVO.getBaseTo(),
                exchangeVO.getAmountFrom(),
                exchangeVO.getAmountTo(),
                System.currentTimeMillis()
        );

        return Completable.fromAction(() -> {
            currencyDao.setLastUsed(exchangeVO.getBaseFrom(), System.currentTimeMillis());
            currencyDao.setLastUsed(exchangeVO.getBaseTo(), System.currentTimeMillis());
            exchangeDao.insert(exchange);
        });
    }

    @Override
    public void cacheExchange(ExchangeVO viewObject) {
        if (viewObject == null) {
            exchange = null;
        } else {
            exchange = new Exchange(
                    viewObject.getBaseFrom(),
                    viewObject.getBaseTo(),
                    viewObject.getAmountFrom(),
                    viewObject.getAmountTo(),
                    0L);
        }
    }

    private Observable<Currency> getSecondCurrencyForExchange(String currencyBase) {
        return currencyDao.getLikedCurrenciesWithoutPressed(currencyBase)
                .toObservable()
                .flatMap(currencies -> {
                    if (currencies.isEmpty()) {
                        return getUsdOrRub(currencyBase);
                    } else {
                        if (currencies.size() > 1) {
                            if (currencies.get(0).getBase().equals(currencyBase)) {
                                return Observable.just(currencies.get(1));
                            } else {
                                return Observable.just(currencies.get(0));
                            }
                        } else {
                            return Observable.just(currencies.get(0));
                        }
                    }
                });
    }

    private Observable<Currency> getUsdOrRub(String baseOfFirst) {
        return currencyDao.getCurrency(baseOfFirst).getBase().equals("RUB") ?
                Observable.just(currencyDao.getCurrency("USD")) :
                Observable.just(currencyDao.getCurrency("RUB"));
    }

    private ExchangeVO buildRates(Currency from, Currency to) {
        if (exchange == null) {
            return new ExchangeVO(
                    from.getBase(),
                    to.getBase(),
                    1.0,
                    1.0 / from.getRate() * to.getRate());
        } else {
            return new ExchangeVO(
                    from.getBase(),
                    to.getBase(),
                    exchange.getAmountFrom(),
                    exchange.getAmountFrom() / from.getRate() * to.getRate());
        }
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }
}
