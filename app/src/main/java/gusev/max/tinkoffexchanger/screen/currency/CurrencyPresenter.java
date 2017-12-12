package gusev.max.tinkoffexchanger.screen.currency;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import gusev.max.tinkoffexchanger.data.model.vo.CurrencyVO;
import gusev.max.tinkoffexchanger.data.repository.Repository;
import gusev.max.tinkoffexchanger.data.repository.RepositoryProvider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CurrencyPresenter implements CurrencyContract.Presenter, LifecycleObserver {

    private CurrencyContract.View currencyView;

    private CompositeDisposable disposeBag;

    private Repository dataRepository = RepositoryProvider.provideRepository();

    CurrencyPresenter(CurrencyContract.View view) {
        this.currencyView = view;

        if (view instanceof LifecycleOwner) {
            ((LifecycleOwner) view).getLifecycle().addObserver(this);
        }

        disposeBag = new CompositeDisposable();
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onAttach() {
        getCurrencies();
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleReturnedData, this::handleError));
    }

    @Override
    public void refreshCurrencies() {
        currencyView.showLoading(true);
        disposeBag.add(dataRepository.loadCurrencies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleReturnedData, this::handleError));
    }

    @Override
    public void onStarButtonClick(Currency currency) {
        currency.changeLiked();

        disposeBag.add(dataRepository.updateCurrency(currency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(this::getCurrencies)
                .subscribe());

    }

    @Override
    public void onCurrencyLongCLick(Currency currency) {

        disposeBag.add(dataRepository.setAnchoredCurrency(currency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
