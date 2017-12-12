package gusev.max.tinkoffexchanger;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.facebook.stetho.Stetho;

import gusev.max.tinkoffexchanger.data.api.ApiFactory;
import gusev.max.tinkoffexchanger.data.cache.database.DatabaseFactory;
import gusev.max.tinkoffexchanger.data.cache.prefs.SharedPrefManager;
import gusev.max.tinkoffexchanger.data.repository.RepositoryProvider;

public class ExchangerApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        Stetho.initializeWithDefaults(this);

        ApiFactory.recreate();
        SharedPrefManager.init(getContext());
        DatabaseFactory.getInstance();
        RepositoryProvider.init();
    }

    @NonNull
    public static Context getContext(){
        return context;
    }
}
