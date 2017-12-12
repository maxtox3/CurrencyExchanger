package gusev.max.tinkoffexchanger.data.repository.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gusev.max.tinkoffexchanger.data.api.ApiFactory;
import gusev.max.tinkoffexchanger.data.api.ApiProtocol;
import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import io.reactivex.Flowable;

public class RemoteDataSource {

    private ApiProtocol api = ApiFactory.getApiProtocol();

    @NonNull
    public Flowable<List<Currency>> loadCurrencies() {
        return api.loadCurrencies()
                .map(response -> {
                    List<Currency> currencyList = new ArrayList<>();
                    currencyList.add(new Currency(response.getBase(), false, System.currentTimeMillis(), 1.0));

                    for (Map.Entry<String, Double> entry : response.getRates().entrySet()) {
                        currencyList.add(new Currency(entry.getKey(), false, System.currentTimeMillis(), entry.getValue()));
                    }

                    return currencyList;
                });
    }

    public Flowable<ArrayList<Float>> getRatesForTrends(String[] dates, String baseOfSelectedCurrency){
        return Flowable.fromArray(dates)
                .flatMapSingle(param -> api.getCurrencyByDate(param))
                .reduce(new ArrayList<Float>(), (list, element) -> {
                    list.add((float) element.getRates().get(baseOfSelectedCurrency).doubleValue());
                    return list;
                }).toFlowable();
    }

}
