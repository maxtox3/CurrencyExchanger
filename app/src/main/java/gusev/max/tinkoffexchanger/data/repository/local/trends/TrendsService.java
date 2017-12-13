package gusev.max.tinkoffexchanger.data.repository.local.trends;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gusev.max.tinkoffexchanger.data.cache.database.CurrencyDao;
import gusev.max.tinkoffexchanger.data.cache.database.DatabaseFactory;
import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import gusev.max.tinkoffexchanger.data.model.vo.TrendsVO;
import io.reactivex.Observable;

public class TrendsService implements TrendsServiceProtocol {

    private CurrencyDao currencyDao = DatabaseFactory.getCurrencyDao();
    private String selectedPeriod;
    private String selectedTrendsCurrency;
    private boolean firstTimeLoadTrendsCurrency = true;

    @Override
    public Observable<List<Currency>> getCurrenciesForTrends(Currency anchoredCurrency) {
        if(anchoredCurrency.getBase() == null){
            return currencyDao.getCurrenciesWithoutEuro().toObservable();
        } else {
            return currencyDao.getCurrenciesWithoutEuroAndAnchored(anchoredCurrency.getBase())
                    .map(currencies -> {
                        List<Currency> currenciesForTrends = new ArrayList<>();
                        currenciesForTrends.add(anchoredCurrency);
                        currenciesForTrends.addAll(currencies);
                        return currenciesForTrends;
                    }).toObservable();
        }
    }

    private String getSelectedPeriod() {
        return selectedPeriod == null ? "week" : selectedPeriod;
    }

    @Override
    public void setSelectedPeriod(String selectedPeriod) {
        this.selectedPeriod = selectedPeriod;
    }

    @Override
    public String getSelectedTrendsCurrency() {
        return selectedTrendsCurrency == null ? "RUB" : selectedTrendsCurrency;
    }

    @Override
    public void setSelectedTrendsCurrency(String selectedTrendsCurrency) {
        this.selectedTrendsCurrency = selectedTrendsCurrency;
    }

    @Override
    public boolean isFirstTimeLoadTrendsCurrency() {
        return firstTimeLoadTrendsCurrency;
    }

    @Override
    public void setFirstTimeLoadTrendsCurrency(boolean firstTimeLoadTrendsCurrency) {
        this.firstTimeLoadTrendsCurrency = firstTimeLoadTrendsCurrency;
    }

    @Override
    public TrendsVO buildTrends(List<Currency> currencies, List<Float> rates) {
        return new TrendsVO(
                getSelectedTrendsCurrency(),
                getSelectedPeriod(),
                currencies,
                rates
        );
    }

    @Override
    public String[] generateDatesForTrends() {
        int number = 7;

        if (getSelectedPeriod().equals("two_weeks")) {
            number = 14;
        } else if (getSelectedPeriod().equals("month")) {
            number = 30;
        }

        String[] dates = new String[number];

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date current = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);

        for (int i = 0; i < number; i++) {
            dates[i] = df.format(current);
            calendar.add(Calendar.DATE, -1);
            current = calendar.getTime();
        }
        return dates;
    }
}
