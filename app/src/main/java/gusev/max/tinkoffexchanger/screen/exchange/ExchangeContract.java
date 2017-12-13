package gusev.max.tinkoffexchanger.screen.exchange;

import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import gusev.max.tinkoffexchanger.screen.base.BasePresenter;

public interface ExchangeContract {

    interface View {

        //void showRates(String baseFrom, String baseTo, Double amountFrom, Double amountTo);

        void showLoading(Boolean show);

        void enableFields(Boolean enable);

        void showDialog(ExchangeVO exchangeVO);

        void showSuccess();

        ExchangeVO getExchangeVO();

        void showRatesAfterLoading(String baseFrom, String baseTo, Double amountFrom, Double amountTo);
    }

    interface Presenter extends BasePresenter {

        void getRates();

        void onExchangeButtonClick(ExchangeVO viewObject);

        void exchange(ExchangeVO viewObject);

        void onFieldsChange(ExchangeVO viewObject, Boolean isFrom);

        void cacheExchange(ExchangeVO viewObject);
    }
}
