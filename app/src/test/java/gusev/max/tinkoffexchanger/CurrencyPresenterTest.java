package gusev.max.tinkoffexchanger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import gusev.max.tinkoffexchanger.data.model.vo.CurrencyVO;
import gusev.max.tinkoffexchanger.data.repository.RepositoryImpl;
import gusev.max.tinkoffexchanger.screen.currency.CurrencyContract;
import gusev.max.tinkoffexchanger.screen.currency.CurrencyPresenter;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class CurrencyPresenterTest {
    private static final Currency currency1 = new Currency();
    private static final Currency currency2 = new Currency();
    private static final Currency currency3 = new Currency();
    private static final Currency currency4 = new Currency();
    private List<Currency> currencies = Arrays.asList(currency1, currency2, currency3);
    private CurrencyVO currencyVO = new CurrencyVO(currencies, currency4);

    @Mock
    private RepositoryImpl repository;

    @Mock
    private CurrencyContract.View view;

    private TestScheduler testScheduler;

    private Scheduler ioScheduler;
    private Scheduler uiScheduler;

    private CurrencyPresenter presenter;

    @Before
    public void setUp() {
        testScheduler = new TestScheduler();
        ioScheduler = testScheduler;
        uiScheduler = testScheduler;
        presenter = new CurrencyPresenter(view, repository, ioScheduler, uiScheduler);

        // Update stub
        currency1.setBase("RUB");
        currency2.setBase("USD");
        currency3.setBase("EUR");
    }


    @Test
    public void getCurrencies_ShouldShowCurrenciesOnView_WithDataReturned() {
        // Given
        doReturn(Observable.just(currencyVO)).when(repository).getCachedCurrencies();

        // When
        presenter.getCurrencies();
        testScheduler.triggerActions();

        // Then
        verify(view).showLoading(true);
        verify(view).showCurrencies(currencyVO);
        verify(view, atLeastOnce()).showLoading(false);
    }

    @Test
    public void refreshCurrencies_ShouldShowCurrenciesOnView_WithDataReturned() {
        // Given
        doReturn(Observable.just(currencyVO)).when(repository).loadCurrencies();

        // When
        presenter.refreshCurrencies();
        testScheduler.triggerActions();

        // Then
        verify(view).showLoading(true);
        verify(view).showCurrencies(currencyVO);
        verify(view, atLeastOnce()).showLoading(false);
    }
}
