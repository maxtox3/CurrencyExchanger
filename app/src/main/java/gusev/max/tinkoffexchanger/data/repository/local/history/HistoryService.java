package gusev.max.tinkoffexchanger.data.repository.local.history;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import gusev.max.tinkoffexchanger.data.cache.database.DatabaseFactory;
import gusev.max.tinkoffexchanger.data.cache.database.ExchangeDao;
import gusev.max.tinkoffexchanger.data.model.dto.Exchange;
import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import gusev.max.tinkoffexchanger.data.model.vo.FilterVO;
import io.reactivex.Observable;

public class HistoryService implements HistoryServiceProtocol {

    private ExchangeDao exchangeDao = DatabaseFactory.getExchangeDao();

    @Override
    public Observable<List<ExchangeVO>> getExchangesByFilter(FilterVO filter) {
        return getExchangesViewObjects(filter.getPeriodType(), filter.getChecks());
    }

    private Observable<List<ExchangeVO>> getExchangesViewObjects(String periodType, Set<String> checkers) {
        return exchangeDao.getAllExchanges()
                .map(exchanges ->  dateFilter(exchanges, periodType))
                .map(list -> currencyFilter(list, checkers))
                .map(exchange1 -> {
                    List<ExchangeVO> viewObjectsList = new ArrayList<>(exchange1.size());
                    for(Exchange each : exchange1){
                        viewObjectsList.add(new ExchangeVO(
                                each.getBaseFrom(),
                                each.getBaseTo(),
                                each.getAmountFrom(),
                                each.getAmountTo()));
                    }

                    return viewObjectsList;
                })
                .toObservable();
    }

    private List<Exchange> dateFilter(List<Exchange> exchanges, String typeOfPeriod) {

        if (typeOfPeriod.equals("all_time")) {
            return exchanges;
        } else if (typeOfPeriod.equals("week")) {
            List<Exchange> result = new ArrayList<>();
            Date today = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, -7);
            Date last = calendar.getTime();

            for (Exchange each : exchanges) {
                Date date = new Date(each.getDate());
                if (date.compareTo(today) <= 0 && date.compareTo(last) >= 0) {
                    result.add(each);
                }
            }
            return result;
        } else if (typeOfPeriod.equals("month")) {
            List<Exchange> result = new ArrayList<>();
            Date today = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, -30);
            Date last = calendar.getTime();

            for (Exchange each : exchanges) {
                Date date = new Date(each.getDate());
                if (date.compareTo(today) <= 0 && date.compareTo(last) >= 0) {
                    result.add(each);
                }
            }

            return result;
        } else {
            return exchanges;
        }
    }

    private List<Exchange> currencyFilter(List<Exchange> exchanges, Set<String> checkers) {
        if (checkers.isEmpty()) {
            return exchanges;
        } else {
            List<Exchange> result = new ArrayList<>();
            for (Exchange each: exchanges) {
                for (String check: checkers) {
                    if (check.equals(each.getBaseFrom())
                            || check.equals(each.getBaseTo())) {
                        result.add(each);
                    }
                }
            }
            return result;
        }
    }
}
