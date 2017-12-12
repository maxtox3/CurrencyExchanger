package gusev.max.tinkoffexchanger.screen.filters;

import java.util.List;
import java.util.Set;

import gusev.max.tinkoffexchanger.data.model.vo.FilterVO;
import gusev.max.tinkoffexchanger.screen.base.BasePresenter;

public interface FiltersContract {

    interface View {

        void showCurrencies(List<String> currencies, Set<String> checks);

        void showPeriod(String period);

        void showDateEdges(String dateFrom, String dateTo);
    }

    interface Presenter extends BasePresenter {

        void getData();

        void setCheckers(Set<String> checkers);

        void setPeriodType(String type);

        void setDateEdges(String dateFrom, String dateTo);

        void saveFilter(FilterVO viewObject);
    }
}
