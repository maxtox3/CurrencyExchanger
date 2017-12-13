package gusev.max.tinkoffexchanger.screen.exchange;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import gusev.max.tinkoffexchanger.data.repository.Repository;
import gusev.max.tinkoffexchanger.data.repository.RepositoryProvider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ExchangePresenter implements ExchangeContract.Presenter, LifecycleObserver {

    private Repository dataRepository = RepositoryProvider.provideRepository();
    private ExchangeContract.View exchangeView;
    private CompositeDisposable disposeBag;

    ExchangePresenter(ExchangeContract.View view) {
        this.exchangeView = view;

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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
            dataRepository.getRatesWithRefresh()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleReturnedData, this::handleError);
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
