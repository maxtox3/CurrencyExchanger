package gusev.max.tinkoffexchanger.screen.history;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import java.util.List;

import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import gusev.max.tinkoffexchanger.data.model.vo.FilterVO;
import gusev.max.tinkoffexchanger.data.repository.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HistoryPresenter implements HistoryContract.Presenter, LifecycleObserver {

    private final String ALL_TIME = "all_time";
    private final String ALL_TIME_RUS = "За все время";

    private final String WEEK = "week";
    private final String WEEK_RUS = "За неделю";

    private final String MONTH = "month";
    private final String MONTH_RUS = "За месяц";

    private final String PERIOD = "period";
    private final String PERIOD_RUS = "За период";

    private Repository dataRepository;
    private HistoryContract.View historyView;
    private CompositeDisposable disposeBag;

    HistoryPresenter(HistoryContract.View view, Repository repository) {
        this.historyView = view;
        this.dataRepository = repository;

        if (view instanceof LifecycleOwner) {
            ((LifecycleOwner) view).getLifecycle().addObserver(this);
        }

        disposeBag = new CompositeDisposable();
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onAttach() {
        getHistory();
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onDetach() {
        disposeBag.clear();
    }


    @Override
    public void getHistory() {
        disposeBag.add(dataRepository.getHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleReturnedData, this::handleError));
    }

    @Override
    public void getFilter() {
        disposeBag.add(dataRepository.getFilter()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleReturnedFilter, this::handleError));
    }

    private void handleReturnedFilter(FilterVO filterVO) {
        if(filterVO != null) {
            if (!filterVO.getChecks().isEmpty()) {
                StringBuilder currencies = new StringBuilder();
                for (String s : filterVO.getChecks()) {
                    currencies.append(s).append("  ");
                }
                historyView.showChecks(currencies.toString());
            } else {
                historyView.showChecks(null);
            }

            if (filterVO.getPeriodType().equals(PERIOD) && filterVO.getDateFrom() != null) {
                historyView.showDateToDateFrom("от: " + filterVO.getDateFrom() + "   до: " + filterVO.getDateTo());
            } else {
                historyView.showDateToDateFrom(null);
            }

            historyView.showPeriodType(periodToReadableString(filterVO.getPeriodType()));
        }
    }

    private void handleReturnedData(List<ExchangeVO> exchanges) {
        if(exchanges.size() == 0){
            historyView.showEmptyHistory();
        } else {
            historyView.showExchanges(exchanges);
            getFilter();
        }
    }

    private void handleError(Throwable error) {

    }

    private String periodToReadableString(String period){
        switch (period) {
            case ALL_TIME:
                return ALL_TIME_RUS;
            case WEEK:
                return WEEK_RUS;
            case MONTH:
                return MONTH_RUS;
            case PERIOD:
                return PERIOD_RUS;
        }
        return ALL_TIME;
    }
}
