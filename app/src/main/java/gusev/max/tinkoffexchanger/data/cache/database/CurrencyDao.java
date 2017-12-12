package gusev.max.tinkoffexchanger.data.cache.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import io.reactivex.Flowable;

@Dao
public interface CurrencyDao {

    @Query("SELECT * FROM (SELECT * FROM currency ORDER BY lastUsed DESC) ORDER BY liked DESC")
    Flowable<List<Currency>> getAllCurrencies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Currency currency);

    @Query("DELETE FROM currency")
    void deleteAll();

    @Query("SELECT * FROM currency WHERE base = :base")
    Currency getCurrency(String base);

    @Query("SELECT * FROM currency WHERE base = :base")
    Flowable<Currency> getCurrencyFlowable(String base);

    @Query("SELECT * FROM currency WHERE base != :base ORDER BY liked DESC")
    Flowable<List<Currency>> getCurrenciesWithoutAnchored(String base);

    @Query("SELECT * FROM (SELECT * FROM currency WHERE liked = 1 AND base != :pressed ORDER BY liked) ORDER BY lastUsed")
    Flowable<List<Currency>> getLikedCurrenciesWithoutPressed(String pressed);

    @Query("SELECT * FROM (SELECT * FROM currency WHERE base != "+ "'EUR'" +" ORDER BY base) ORDER BY liked DESC")
    Flowable<List<Currency>> getCurrenciesWithoutEuro();

    @Query("UPDATE currency SET lastUsed = :time WHERE base = :baseOfCurrency")
    void setLastUsed(String baseOfCurrency, long time);
}
