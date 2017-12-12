package gusev.max.tinkoffexchanger.data.repository.local.history;

import java.util.List;

import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import gusev.max.tinkoffexchanger.data.model.vo.FilterVO;
import io.reactivex.Observable;

public interface HistoryServiceProtocol {

    Observable<List<ExchangeVO>> getExchangesByFilter(FilterVO filter);
}
