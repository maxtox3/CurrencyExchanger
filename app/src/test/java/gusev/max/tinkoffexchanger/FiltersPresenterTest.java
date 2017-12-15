package gusev.max.tinkoffexchanger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gusev.max.tinkoffexchanger.data.model.vo.FilterVO;
import gusev.max.tinkoffexchanger.data.repository.RepositoryImpl;
import gusev.max.tinkoffexchanger.screen.filters.FiltersContract;
import gusev.max.tinkoffexchanger.screen.filters.FiltersPresenter;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FiltersPresenterTest {
    private List<String> currencies = Arrays.asList("AUD", "RUB", "USD");
    private Set<String> checks = new HashSet<>();
    private FilterVO filterVO = new FilterVO(currencies, checks, "all_time", "10/12/2017", "10/12/2017");

    @Mock
    private RepositoryImpl repository;

    @Mock
    private FiltersContract.View view;

    private TestScheduler testScheduler;

    private Scheduler ioScheduler;
    private Scheduler uiScheduler;

    private FiltersPresenter presenter;

    @Before
    public void setUp() {
        testScheduler = new TestScheduler();
        ioScheduler = testScheduler;
        uiScheduler = testScheduler;
        presenter = new FiltersPresenter(view, repository, ioScheduler, uiScheduler);
    }

    @Test
    public void getData_ShouldShowCurrenciesOnView_WithDataReturned() {
        // Given
        doReturn(Observable.just(filterVO)).when(repository).getFilter();

        // When
        presenter.getData();
        testScheduler.triggerActions();

        // Then
        verify(view).showCurrencies(filterVO.getCurrencies(), filterVO.getChecks());
    }

    @Test
    public void getData_ShouldShowPeriodOnView_WithDataReturned(){
        // Given
        doReturn(Observable.just(filterVO)).when(repository).getFilter();

        // When
        presenter.getData();
        testScheduler.triggerActions();

        // Then
        verify(view).showPeriod(filterVO.getPeriodType());
    }

    @Test
    public void getData_ShouldShowDataEdgesOnView_WithDataReturned(){
        // Given
        doReturn(Observable.just(filterVO)).when(repository).getFilter();

        // When
        presenter.getData();
        testScheduler.triggerActions();

        // Then
        verify(view).showDateEdges(filterVO.getDateFrom(), filterVO.getDateTo());
    }
}
