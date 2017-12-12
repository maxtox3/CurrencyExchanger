package gusev.max.tinkoffexchanger.data.cache.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import gusev.max.tinkoffexchanger.data.model.vo.FilterVO;
import io.reactivex.Observable;

public class SharedPrefManager {

    private static SharedPrefManager sInstance;
    private SharedPreferences prefs;

    private final String LAST_DOWNLOAD_TIME = "LAST_DOWNLOAD_TIME";
    private final String FILTER = "FILTER";


    private SharedPrefManager(@NonNull Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void init(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new SharedPrefManager(context);
        }
    }

    public static SharedPrefManager getInstance() {
        return sInstance;
    }

    public long readLastTimeDownloaded() {
        return prefs.getLong(LAST_DOWNLOAD_TIME, 0L);
    }

    public Observable<Long> readLastTimeDownloadedObservable() {
        return Observable.just(prefs.getLong(LAST_DOWNLOAD_TIME, 0L));
    }

    public void writeLastTimeDownloaded(long value) {
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putLong(LAST_DOWNLOAD_TIME, value);
        prefsEditor.apply();
    }

    public void writeFilter(FilterVO viewObject) {
        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(viewObject);
        prefsEditor.putString(FILTER, json);
        prefsEditor.apply();
    }

    public FilterVO getFilter() {
        Gson gson = new Gson();
        String json = prefs.getString(FILTER, "");
        return gson.fromJson(json, FilterVO.class);
    }
}
