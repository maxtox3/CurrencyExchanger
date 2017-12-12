package gusev.max.tinkoffexchanger.screen.exchange;

import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import gusev.max.tinkoffexchanger.screen.base.BasePresenter;

public interface ExchangeContract {

    interface View {

        void showRates(ExchangeVO rates);

        void showLoading(Boolean show);

        void enableFields(Boolean enable);

        void showDialog();

        void showSuccess();

        ExchangeVO getExchangeVO();
    }

    interface Presenter extends BasePresenter {

        void getRates();

        void onExchangeButtonClick(ExchangeVO viewObject);

        void exchange(ExchangeVO viewObject);

        void onFieldsChange(ExchangeVO viewObject);
    }
}
