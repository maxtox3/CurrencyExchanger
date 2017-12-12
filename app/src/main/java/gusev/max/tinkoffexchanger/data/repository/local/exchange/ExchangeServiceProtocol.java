package gusev.max.tinkoffexchanger.data.repository.local.exchange;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import gusev.max.tinkoffexchanger.data.model.dto.Exchange;
import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import io.reactivex.Completable;
import io.reactivex.Observable;

public interface ExchangeServiceProtocol {

    Observable<ExchangeVO> getRates(Currency anchoredCurrency, Currency currencyForRates);

    Completable saveExchange(ExchangeVO exchangeVO);

    void cacheExchange(ExchangeVO viewObject);

    void setExchange(Exchange exchange);
}
