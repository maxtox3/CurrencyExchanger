package gusev.max.tinkoffexchanger.screen.exchange;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import gusev.max.tinkoffexchanger.data.repository.Repository;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

public class ExchangePresenter implements ExchangeContract.Presenter, LifecycleObserver {

    private Repository dataRepository;
    private ExchangeContract.View exchangeView;
    private CompositeDisposable disposeBag;
    private Scheduler ioScheduler;
    private Scheduler uiScheduler;

    public ExchangePresenter(ExchangeContract.View view, Repository repository, Scheduler io, Scheduler ui) {
        this.exchangeView = view;
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
        getRates();
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onDetach() {
        disposeBag.clear();
    }


    @Override
    public void getRates() {
        exchangeView.showLoading(true);
        exchangeView.enableFields(false);

        disposeBag.add(dataRepository.getRates()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(this::handleReturnedData, this::handleError)
        );
    }

    @Override
    public void onExchangeButtonClick(ExchangeVO viewObject) {
        boolean notFresh = dataRepository.checkRatesFreshness();
        if (notFresh) {
            getRatesWithRefresh();
        } else {
            this.exchange(viewObject);
        }
    }

    @Override
    public void exchange(ExchangeVO viewObject) {
        disposeBag.add(dataRepository.exchange(viewObject)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doOnComplete(() -> {
                    dataRepository.cacheExchange(null);
                    exchangeView.showSuccess();
                })
                .doOnError(this::handleError)
                .subscribe()
        );
    }

    @Override
    public void onFieldsChange(ExchangeVO viewObject, Boolean isFrom) {
        dataRepository.cacheExchange(viewObject);

        if (dataRepository.checkRatesFreshness()) {
            dataRepository.cacheExchange(null);
            exchangeView.enableFields(false);
            exchangeView.showLoading(true);
            disposeBag.add(dataRepository.getRatesWithRefresh()
                    .subscribeOn(ioScheduler)
                    .observeOn(uiScheduler)
                    .subscribe(this::handleReturnedData, this::handleError));
        }
    }

    @Override
    public void cacheExchange(ExchangeVO viewObject) {
        dataRepository.cacheExchange(viewObject);
    }

    private void getRatesWithRefresh() {
        exchangeView.showLoading(true);
        exchangeView.enableFields(false);

        disposeBag.add(dataRepository.getRatesOrRefresh()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(exchangeVO -> {
                    dataRepository.cacheExchange(exchangeVO);
                    exchangeView.showLoading(false);
                    exchangeView.enableFields(true);
                    exchangeView.showDialog(exchangeVO);
                }, this::handleError));
    }

    private void handleReturnedData(ExchangeVO rates) {
        exchangeView.enableFields(true);
        exchangeView.showLoading(false);
        exchangeView.showRatesAfterLoading(
                rates.getBaseFrom(),
                rates.getBaseTo(),
                rates.getAmountFrom(),
                rates.getAmountTo());
    }

    private void handleError(Throwable error) {
        exchangeView.showLoading(false);
    }

}
