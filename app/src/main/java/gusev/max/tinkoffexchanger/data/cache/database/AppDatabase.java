package gusev.max.tinkoffexchanger.data.cache.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import gusev.max.tinkoffexchanger.data.model.dto.Exchange;

@Database(entities = {Currency.class, Exchange.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {

    public abstract CurrencyDao currencyDao();

    public abstract ExchangeDao exchangeDao();

}
