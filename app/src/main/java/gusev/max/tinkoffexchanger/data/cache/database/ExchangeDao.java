package gusev.max.tinkoffexchanger.data.cache.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import gusev.max.tinkoffexchanger.data.model.dto.Exchange;
import io.reactivex.Flowable;

@Dao
public interface ExchangeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Exchange exchange);

    @Query("SELECT * FROM exchange")
    Flowable<List<Exchange>> getAllExchanges();

    @Query("SELECT baseFrom FROM exchange UNION SELECT baseTo FROM exchange")//DISTINCT
    Flowable<List<String>> getAllCurrencies();
}
