package gusev.max.tinkoffexchanger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import gusev.max.tinkoffexchanger.data.repository.RepositoryImpl;
import gusev.max.tinkoffexchanger.screen.exchange.ExchangeContract;
import gusev.max.tinkoffexchanger.screen.exchange.ExchangePresenter;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ExchangePresenterTest {
    private ExchangeVO exchangeVO = new ExchangeVO("AUD", "RUB", 1.0, 15.0);

    @Mock
    private RepositoryImpl repository;

    @Mock
    private ExchangeContract.View view;

    private TestScheduler testScheduler;

    private Scheduler ioScheduler;
    private Scheduler uiScheduler;

    private ExchangePresenter presenter;

    @Before
    public void setUp() {
        testScheduler = new TestScheduler();
        ioScheduler = testScheduler;
        uiScheduler = testScheduler;
        presenter = new ExchangePresenter(view, repository, ioScheduler, uiScheduler);
    }

    @Test
    public void getRates_ShouldShowRatesOnView_WithDataReturned() {
        // Given
        doReturn(Observable.just(exchangeVO)).when(repository).getRates();

        // When
        presenter.getRates();
        testScheduler.triggerActions();

        // Then
        verify(view).showLoading(true);
        verify(view).enableFields(false);
        verify(view).showRatesAfterLoading(exchangeVO.getBaseFrom(), exchangeVO.getBaseTo(), exchangeVO.getAmountFrom(), exchangeVO.getAmountTo());
        verify(view, atLeastOnce()).showLoading(false);
        verify(view, atLeastOnce()).enableFields(true);
    }


}
