package YandexLinguisticsAPI;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Brom on 29.12.2016.
 */
public interface API {
    @GET("api/v1.5/tr.json/translate")
    Call<Resp> Translate(@QueryMap Map<String, String> parameters);

}