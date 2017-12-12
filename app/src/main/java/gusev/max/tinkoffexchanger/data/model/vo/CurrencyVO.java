package gusev.max.tinkoffexchanger.data.model.vo;

import java.util.List;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;

public class CurrencyVO {

    private final List<Currency> currencyList;
    private final Currency onTopCurrency;

    public CurrencyVO(List<Currency> currencyList, Currency onTopCurrency) {
        this.currencyList = currencyList;
        this.onTopCurrency = onTopCurrency;
    }

    public List<Currency> getCurrencyList() {
        return currencyList;
    }

    public Currency getOnTopCurrency() {
        return onTopCurrency;
    }
}
