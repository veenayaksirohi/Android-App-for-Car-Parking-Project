package com.example.carparkingapp.core.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.carparkingapp.core.config.ApiConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static ApiClient instance;
    private final ApiInterface apiInterface;
    private final Context context;
    private static final int MAX_RETRIES = 3;

    private ApiClient(Context context) {
        this.context = context;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message ->
            Log.d("ApiClient", "OkHttp: " + message));
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new Interceptor() {
                    @NonNull
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                        String token = sharedPreferences.getString("jwt_token", null);

                        Request.Builder requestBuilder = chain.request().newBuilder();
                        if (token != null) {
                            requestBuilder.addHeader("Authorization", "Bearer " + token);
                        }

                        return chain.proceed(requestBuilder.build());
                    }
                })
                .addInterceptor(new Interceptor() {
                    @NonNull
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = null;
                        IOException exception = null;
                        int retryCount = 0;

                        while (retryCount < MAX_RETRIES && (response == null || !response.isSuccessful())) {
                            try {
                                response = chain.proceed(request);
                                if (response.isSuccessful()) {
                                    return response;
                                }
                            } catch (IOException e) {
                                exception = e;
                                Log.e("ApiClient", "Retry attempt " + (retryCount + 1) + " failed", e);
                            }
                            retryCount++;
                            if (retryCount < MAX_RETRIES) {
                                try {
                                    Thread.sleep(1000 * retryCount); // Exponential backoff
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    throw new IOException("Retry interrupted", e);
                                }
                            }
                        }

                        if (response != null) {
                            return response;
                        }
                        throw exception != null ? exception : new IOException("Failed after " + MAX_RETRIES + " retries");
                    }
                })
                .connectTimeout(ApiConfig.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    public ApiInterface getService() {
        return apiInterface;
    }
}
