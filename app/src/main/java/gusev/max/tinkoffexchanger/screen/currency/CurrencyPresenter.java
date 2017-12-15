package gusev.max.tinkoffexchanger.screen.currency;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import gusev.max.tinkoffexchanger.data.model.vo.CurrencyVO;
import gusev.max.tinkoffexchanger.data.repository.Repository;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

public class CurrencyPresenter implements CurrencyContract.Presenter, LifecycleObserver {

    private CurrencyContract.View currencyView;
    private CompositeDisposable disposeBag;
    private Repository dataRepository;
    private Scheduler ioScheduler;
    private Scheduler uiScheduler;
    private boolean firstTime = true;

    public CurrencyPresenter(CurrencyContract.View view, Repository repository, Scheduler io, Scheduler ui) {
        this.currencyView = view;
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
        if(firstTime){
            firstTime = false;
            getCurrencies();
            refreshCurrencies();
        } else {
            getCurrencies();
        }

    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onDetach() {
        disposeBag.clear();
    }

    @Override
    public void getCurrencies() {
        currencyView.showLoading(true);
        disposeBag.add(dataRepository.getCachedCurrencies()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(this::handleReturnedData, this::handleError));
    }

    @Override
    public void refreshCurrencies() {
        currencyView.showLoading(true);
        disposeBag.add(dataRepository.loadCurrencies()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(this::handleReturnedData, this::handleError));
    }

    @Override
    public void onStarButtonClick(Currency currency) {
        currency.changeLiked();

        disposeBag.add(dataRepository.updateCurrency(currency)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doOnComplete(this::getCurrencies)
                .subscribe());

    }

    @Override
    public void onCurrencyLongCLick(Currency currency) {

        disposeBag.add(dataRepository.setAnchoredCurrency(currency)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doOnComplete(this::getCurrencies)
                .subscribe());
    }

    @Override
    public void onCurrencyClicked(Currency currency) {
        dataRepository.setCurrencyForRates(currency);
    }

    private void handleReturnedData(CurrencyVO currencies) {
        currencyView.showLoading(false);
        if (currencies.getCurrencyList() != null && !currencies.getCurrencyList().isEmpty()) {
            currencyView.showCurrencies(currencies);
        } else {
            currencyView.showError();
        }

    }

    private void handleError(Throwable error) {
        currencyView.showLoading(false);
        currencyView.showError();
    }


}
