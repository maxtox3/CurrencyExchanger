package gusev.max.tinkoffexchanger.screen.trends;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import gusev.max.tinkoffexchanger.data.model.vo.TrendsVO;
import gusev.max.tinkoffexchanger.data.repository.Repository;
import gusev.max.tinkoffexchanger.data.repository.RepositoryProvider;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

public class TrendsPresenter implements TrendsContract.Presenter, LifecycleObserver {

    private Repository dataRepository = RepositoryProvider.provideRepository();
    private TrendsContract.View trendsView;
    private CompositeDisposable disposeBag;
    private Scheduler ioScheduler;
    private Scheduler uiScheduler;

    public TrendsPresenter(TrendsContract.View view, Repository repository, Scheduler io, Scheduler ui) {
        this.trendsView = view;
        this.dataRepository = repository;
        this.ioScheduler = io;
        this.uiScheduler = ui;

        if (view instanceof LifecycleOwner) {
            ((LifecycleOwner) view).getLifecycle().addObserver(this);
        }

        disposeBag = new CompositeDisposable();
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onAttach() {
        getData();
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onDetach() {
        disposeBag.clear();
    }

    @Override
    public void getData() {
        trendsView.showLoading(true);
        disposeBag.add(dataRepository.getTrends()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(this::handleReturnedData, this::handleError));
    }

    @Override
    public void onPeriodSelect(String period) {
        trendsView.showLoading(true);
        dataRepository.setTrendsPeriod(period);
        getData();
    }

    @Override
    public void onCurrencySelect(Currency currency) {
        trendsView.showLoading(true);
        dataRepository.setSelectedTrendsCurrency(currency.getBase());
        getData();
    }

    private void handleReturnedData(TrendsVO viewObject) {
        trendsView.showCurrencies(viewObject.getCurrencies());
        trendsView.setPeriod(viewObject.getPeriod());
        trendsView.setSelectedCurrency(viewObject.getBaseOfSelectedCurrency());
        trendsView.setRates(viewObject.getRates());
        trendsView.showLoading(false);
    }

    private void handleError(Throwable error) {
        trendsView.showLoading(false);
        Log.i("trends", error.getLocalizedMessage());
        trendsView.showError();
    }
}
