package gusev.max.tinkoffexchanger.data.model.dto;

import java.util.List;
import java.util.Set;

public class Filter {

    private List<String> currencies;
    private Set<String> checks;
    private String periodType;
    private String dateTo;
    private String dateFrom;

    public Filter(List<String> currencies, Set<String> checks, String periodType, String dateTo, String dateFrom) {
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

    public void setCurrencies(List<String> currencies) {
        this.currencies = currencies;
    }

    public void setChecks(Set<String> checks) {
        this.checks = checks;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }


}
