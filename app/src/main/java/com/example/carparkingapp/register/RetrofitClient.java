package com.example.carparkingapp.register;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.carparkingapp.api.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2:5000";

    // Ensure thread-safe singleton instance
    public static synchronized Retrofit getRetrofitInstance(final Context context) {
        if (retrofit == null) {
            // Set up detailed logging for debugging
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> 
                Log.d("RetrofitClient", "OkHttp: " + message));
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
                    }).build();

            // Create Gson instance with custom configuration
            Gson gson = new GsonBuilder()
                    .setLenient() // This helps with malformed JSON
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }


}
