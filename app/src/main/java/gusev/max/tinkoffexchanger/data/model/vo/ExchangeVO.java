package gusev.max.tinkoffexchanger.data.model.vo;

import java.io.Serializable;

public class ExchangeVO implements Serializable {

    private final String baseFrom;
    private final String baseTo;
    private final Double amountFrom;
    private final Double amountTo;

    public ExchangeVO( String baseFrom, String baseOfCurrencyTo, Double amountFrom, Double amountTo) {
        this.baseFrom = baseFrom;
        this.baseTo = baseOfCurrencyTo;
        this.amountFrom = amountFrom;
        this.amountTo = amountTo;
    }

    public String getBaseFrom() {
        return baseFrom;
    }

    public String getBaseTo() {
        return baseTo;
    }

    public Double getAmountFrom() {
        return amountFrom;
    }

    public Double getAmountTo() {
        return amountTo;
    }
}
