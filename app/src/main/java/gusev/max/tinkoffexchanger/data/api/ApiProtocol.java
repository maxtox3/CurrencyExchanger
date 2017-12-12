package gusev.max.tinkoffexchanger.data.api;

import gusev.max.tinkoffexchanger.data.model.dto.Response;
import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiProtocol {

    @GET("/latest")
    Flowable<Response> loadCurrencies();

    @GET("/{date}")
    Single<Response> getCurrencyByDate(@Path("date") String date);
}
