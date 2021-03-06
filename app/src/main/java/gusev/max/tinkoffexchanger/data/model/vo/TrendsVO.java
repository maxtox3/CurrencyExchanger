package gusev.max.tinkoffexchanger.data.model.vo;

import java.util.List;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;

public class TrendsVO {

    private final String baseOfSelectedCurrency;
    private final String period;
    private final List<Currency> currencies;
    private final List<Float> rates;

    public TrendsVO(String selectedCurrency, String period, List<Currency> currencies, List<Float> rates) {
        this.baseOfSelectedCurrency = selectedCurrency;
        this.period = period;
        this.currencies = currencies;
        this.rates = rates;
    }

    public String getPeriod() {
        return period;
    }

    public String getBaseOfSelectedCurrency() {
        return baseOfSelectedCurrency;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public List<Float> getRates() {
        return rates;
    }
}
