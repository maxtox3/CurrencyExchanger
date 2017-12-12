package gusev.max.tinkoffexchanger.data.model.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class FilterVO implements Serializable {

    private final List<String> currencies;
    private final Set<String> checks;
    private final String periodType;
    private final String dateTo;
    private final String dateFrom;

    public FilterVO(List<String> currencies, Set<String> checks, String periodType, String dateTo, String dateFrom) {
        this.currencies = currencies;
        this.checks = checks;
        this.periodType = periodType;
        this.dateTo = dateTo;
        this.dateFrom = dateFrom;
    }

    public List<String> getCurrencies() {
        return currencies;
    }

    public Set<String> getChecks() {
        return checks;
    }

    public String getPeriodType() {
        return periodType;
    }

    public String getDateTo() {
        return dateTo;
    }

    public String getDateFrom() {
        return dateFrom;
    }
}
