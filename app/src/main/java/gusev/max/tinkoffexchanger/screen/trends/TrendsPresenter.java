package gusev.max.tinkoffexchanger.screen.trends;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import gusev.max.tinkoffexchanger.data.model.vo.TrendsVO;
import gusev.max.tinkoffexchanger.data.repository.Repository;
import gusev.max.tinkoffexchanger.data.repository.RepositoryProvider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TrendsPresenter implements TrendsContract.Presenter, LifecycleObserver {

    private Repository dataRepository = RepositoryProvider.provideRepository();
    private TrendsContract.View trendsView;
    private CompositeDisposable disposeBag;

    TrendsPresenter(TrendsContract.View view) {
        this.trendsView = view;

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
        trendsView.showLoading();
        disposeBag.add(dataRepository.getTrends()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleReturnedData, this::handleError));
    }

    @Override
    public void onPeriodSelect(String period) {
        trendsView.showLoading();
        dataRepository.setTrendsPeriod(period);
        getData();
    }

    @Override
    public void onCurrencySelect(Currency currency) {
        trendsView.showLoading();
        dataRepository.setSelectedTrendsCurrency(currency.getBase());
        getData();
    }

    private void handleReturnedData(TrendsVO viewObject) {
        trendsView.showCurrencies(viewObject.getCurrencies());
        trendsView.setPeriod(viewObject.getPeriod());
        trendsView.setSelectedCurrency(viewObject.getBaseOfSelectedCurrency());
        trendsView.setRates(viewObject.getRates());
        trendsView.hideLoading();
    }

    private void handleError(Throwable error) {
    }
}
