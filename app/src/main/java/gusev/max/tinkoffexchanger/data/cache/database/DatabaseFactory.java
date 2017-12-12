package gusev.max.tinkoffexchanger.data.cache.database;

import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import gusev.max.tinkoffexchanger.BuildConfig;
import gusev.max.tinkoffexchanger.ExchangerApp;

public class DatabaseFactory {

    private static final String DB_NAME = BuildConfig.DB;//"test_db5";

    private static volatile CurrencyDao sCurrencyService;
    private static volatile ExchangeDao sExchangeService;

    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getInstance() {

        if (INSTANCE == null) {
            INSTANCE = getDatabase();
        }
        return INSTANCE;
    }

    private static AppDatabase getDatabase() {
        return Room.databaseBuilder(ExchangerApp.getContext(), AppDatabase.class, DB_NAME).build();
    }

    @NonNull
    public static CurrencyDao getCurrencyDao() {
        CurrencyDao service = sCurrencyService;
        if (service == null) {
            synchronized (CurrencyDao.class) {
                service = sCurrencyService;
                if (service == null) {
                    service = sCurrencyService = getInstance().currencyDao();
                }
            }
        }
        return service;
    }

    @NonNull
    public static ExchangeDao getExchangeDao() {
        ExchangeDao service = sExchangeService;
        if (service == null) {
            synchronized (ExchangeDao.class) {
                service = sExchangeService;
                if (service == null) {
                    service = sExchangeService = getInstance().exchangeDao();
                }
            }
        }
        return service;
    }

}
