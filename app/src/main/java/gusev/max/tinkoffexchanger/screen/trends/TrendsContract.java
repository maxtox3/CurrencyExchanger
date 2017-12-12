package gusev.max.tinkoffexchanger.screen.trends;

import java.util.List;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import gusev.max.tinkoffexchanger.screen.base.BasePresenter;

class TrendsContract {

    interface View {

        void showLoading();

        void hideLoading();

        void showCurrencies(List<Currency> currencies);

        void setPeriod(String period);

        void setSelectedCurrency(String baseOfSelectedCurrency);

        void setRates(List<Float> rates);
    }

    interface Presenter extends BasePresenter {

        void getData();

        void onPeriodSelect(String period);

        void onCurrencySelect(Currency currency);
    }
}
