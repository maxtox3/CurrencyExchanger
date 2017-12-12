package gusev.max.tinkoffexchanger.screen.history;

import java.util.List;

import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import gusev.max.tinkoffexchanger.screen.base.BasePresenter;

public interface HistoryContract {

    interface View {

        void showExchanges(List<ExchangeVO> exchanges);

        void showChecks(String checks);

        void showPeriodType(String periodType);

        void showDateToDateFrom(String s);

        void showEmptyHistory();
    }

    interface Presenter extends BasePresenter {

        void getHistory();

        void getFilter();
    }
}
