package ds.todoapp.clients;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ds.todoapp.BusProvider;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Duygu on 12/05/2017.
 */

public abstract class BaseRESTClient {
    protected Retrofit retrofit;
    protected String baseBackendUrl;
    protected Bus mBus;
    protected Context mContext;

    protected BaseRESTClient(String baseBackendUrl, Context mContext) {
        this.baseBackendUrl = baseBackendUrl;
        this.mContext = mContext;
        this.mBus = BusProvider.getInstance();
        configureRetrofit();
    }

    private void configureRetrofit() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(HttpUrl.parse(baseBackendUrl))
                .client(getOkClient())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .build();
    }

    private OkHttpClient getOkClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Content-Type", "application/json;charset=utf-8")
                                .addHeader("Accept", "application/json;charset=utf-8")
                                .addHeader("Accept-Language", "en")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .build();
        return client;
    }

    private Gson getGson() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.disableHtmlEscaping()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        return gson;
    }
}
