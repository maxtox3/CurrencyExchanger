package gusev.max.tinkoffexchanger.screen.filters;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import java.util.Set;

import gusev.max.tinkoffexchanger.data.model.vo.FilterVO;
import gusev.max.tinkoffexchanger.data.repository.Repository;
import gusev.max.tinkoffexchanger.data.repository.RepositoryProvider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FiltersPresenter implements FiltersContract.Presenter, LifecycleObserver {

    private Repository dataRepository = RepositoryProvider.provideRepository();
    private FiltersContract.View filtersView;
    private CompositeDisposable disposeBag;

    FiltersPresenter(FiltersContract.View view) {
        this.filtersView = view;

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
        disposeBag.add(dataRepository.getFilter()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::handleReturnedData, this::handleError));
    }

    @Override
    public void setCheckers(Set<String> checkers) {
        dataRepository.setCheckers(checkers);
    }

    @Override
    public void setPeriodType(String type) {
        dataRepository.setPeriodType(type);
    }

    @Override
    public void setDateEdges(String dateFrom, String dateTo) {
        dataRepository.setPeriodEdges(dateFrom, dateTo);
    }

    @Override
    public void saveFilter(FilterVO viewObject) {
        dataRepository.saveFilter(viewObject);
    }

    private void handleReturnedData(FilterVO viewObject){
        filtersView.showCurrencies(viewObject.getCurrencies(), viewObject.getChecks());
        filtersView.showPeriod(viewObject.getPeriodType());
        filtersView.showDateEdges(viewObject.getDateFrom(), viewObject.getDateTo());
    }

    private void handleError(Throwable error){

    }
}
