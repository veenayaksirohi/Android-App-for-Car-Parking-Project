package com.example.carparkingapp.core.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.carparkingapp.config.AppConfig;
import com.example.carparkingapp.utils.EnvConfig;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static ApiClient instance;
    private final ApiInterface apiService;
    private final Context context;
    private static final int MAX_RETRIES = 3;

    private ApiClient(Context context) {
        this.context = context.getApplicationContext();
        
        // Executor for background deserialization
        Executor backgroundExecutor = Executors.newSingleThreadExecutor();
        
        // Create a custom Gson instance
        Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

        // Add logging interceptor for debugging
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .protocols(Collections.singletonList(Protocol.HTTP_1_1)) // Force HTTP/1.1 to prevent protocol negotiation issues
            .addInterceptor(new Interceptor() {
                @NonNull
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    return response;
                }
            })
            .build();

        String baseUrl = AppConfig.getInstance(context).getApiBaseUrl();
        try {
            // Create Retrofit instance
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callbackExecutor(backgroundExecutor)
                .build();

            apiService = retrofit.create(ApiInterface.class);
        } catch (IllegalArgumentException e) {
            Log.e("ApiClient", "Invalid base URL: " + baseUrl, e);
            throw new IllegalStateException("Invalid API base URL. Please check your configuration.", e);
        }
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    public ApiInterface getService() {
        return apiService;
    }
}
