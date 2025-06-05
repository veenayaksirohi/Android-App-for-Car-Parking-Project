package com.example.carparkingapp.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.carparkingapp.models.ParkingLocation;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:5000/";
    private static ApiClient instance;
    private final ApiInterface apiInterface;
    private final Context context;

    private ApiClient(Context context) {
        this.context = context;

        // Set up logging interceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message ->
            Log.d("ApiClient", "OkHttp: " + message));
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Set up auth interceptor
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
                }).build();

        // Create Gson instance with custom configuration
        Gson gson = new GsonBuilder()
                .setLenient() // This helps with malformed JSON
                .create();

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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
    }    public ApiInterface getService() {
        return apiInterface;
    }
}
