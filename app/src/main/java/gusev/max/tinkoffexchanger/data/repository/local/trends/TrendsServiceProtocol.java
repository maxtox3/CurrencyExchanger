package gusev.max.tinkoffexchanger.data.repository.local.trends;

import java.util.List;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import gusev.max.tinkoffexchanger.data.model.vo.TrendsVO;
import io.reactivex.Observable;

public interface TrendsServiceProtocol {

    Observable<List<Currency>> getCurrenciesForTrends(Currency anchoredCurrency);

    void setSelectedPeriod(String selectedPeriod);

    String getSelectedTrendsCurrency();

    void setSelectedTrendsCurrency(String selectedTrendsCurrency);

    boolean isFirstTimeLoadTrendsCurrency();

    void setFirstTimeLoadTrendsCurrency(boolean firstTimeLoadTrendsCurrency);

    TrendsVO buildTrends(List<Currency> currencies, List<Float> rates);

    String[] generateDatesForTrends();
}
