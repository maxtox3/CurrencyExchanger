package gusev.max.tinkoffexchanger.screen.currency;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import gusev.max.tinkoffexchanger.data.model.vo.CurrencyVO;
import gusev.max.tinkoffexchanger.screen.base.BasePresenter;

public interface CurrencyContract {

    interface View {

        void showCurrencies(CurrencyVO currencies);

        void showError();

        void showLoading(Boolean show);
    }

    interface Presenter extends BasePresenter {

        void getCurrencies();

        void refreshCurrencies();

        void onStarButtonClick(Currency currency);

        void onCurrencyLongCLick(Currency currency);

        void onCurrencyClicked(Currency currency);
    }
}
