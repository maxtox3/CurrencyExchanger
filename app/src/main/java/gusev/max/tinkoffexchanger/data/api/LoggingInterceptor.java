package gusev.max.tinkoffexchanger.data.api;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static okhttp3.logging.HttpLoggingInterceptor.Level;

public final class LoggingInterceptor implements Interceptor {

    private final Interceptor mLoggingInterceptor;

    private LoggingInterceptor() {
        mLoggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(Level.BODY);
    }

    @NonNull
    static Interceptor create() {
        return new LoggingInterceptor();
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        return mLoggingInterceptor.intercept(chain);
    }
}
