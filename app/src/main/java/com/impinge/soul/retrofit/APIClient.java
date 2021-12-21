package com.impinge.soul.retrofit;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.impinge.soul.util.PreferenceData;
import com.impinge.soul.util.SharedPreference;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author PA1810.
 */
public class APIClient {

    private static Retrofit retrofit = null;

    public static final String BASE_URL = "http://163.47.212.61:8000/api/v1/";

    public static final String MEDIA_URL = "http://163.47.212.61:8000/api/v1";

    public static Retrofit getClient(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);


        httpClient.addNetworkInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {

                Request.Builder requestBuilder = chain.request().newBuilder();
                SharedPreference.fetchPrefenceData(context, PreferenceData.TOKEN);

                if (!TextUtils.isEmpty(SharedPreference.fetchPrefenceData(context, PreferenceData.TOKEN))) {
                    requestBuilder.header("Authorization", "token " + SharedPreference.fetchPrefenceData(context, PreferenceData.TOKEN));
                }
                Log.e("TOKEN",""+SharedPreference.fetchPrefenceData(context, PreferenceData.TOKEN));
                return chain.proceed(requestBuilder.build());
            }
        });

        httpClient.connectTimeout(2, TimeUnit.MINUTES);
        httpClient.readTimeout(2, TimeUnit.MINUTES);
        httpClient.writeTimeout(2, TimeUnit.MINUTES);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(/*BuildConfig.BASE_URL*/BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
}
