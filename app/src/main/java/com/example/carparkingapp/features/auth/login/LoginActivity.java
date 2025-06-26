package com.example.carparkingapp.features.auth.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carparkingapp.R;
import com.example.carparkingapp.core.network.ApiClient;
import com.example.carparkingapp.core.network.ApiInterface;
import com.example.carparkingapp.utils.TokenManager;
import com.example.carparkingapp.features.maps.DashboardActivity;
import com.example.carparkingapp.features.auth.register.RegisterActivity;
import com.example.carparkingapp.models.LoginRequest;
import com.example.carparkingapp.models.LoginResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView registerLink;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        emailInput = findViewById(R.id.etLoginEmail);
        passwordInput = findViewById(R.id.etLoginPassword);
        loginButton = findViewById(R.id.btnLogin);
        registerLink = findViewById(R.id.tvRegisterLink);

        // Set click listeners
        loginButton.setOnClickListener(v -> loginUser());

        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        
        Log.d("LoginActivity", "Attempting login with email: " + email);

        // Validate inputs
        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter email and password");
            return;
        }

        showProgressDialog();
        
        // Create login request using model class
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Make API call
        ApiInterface apiInterface = ApiClient.getInstance(this).getService();
        apiInterface.loginUser(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                runOnUiThread(() -> {
                    hideProgressDialog();
                    Log.d("LoginActivity", "Received login response: " + response.code());
                    
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse result = response.body();
                        String message = result.getMessage();
                        String token = result.getToken();
                        
                        // Log response details for debugging
                        Log.d("LoginActivity", "Response message: " + message);
                        Log.d("LoginActivity", "Token present: " + (token != null && !token.isEmpty()));
                        
                        // Check for failure message first
                        if (message != null && message.toLowerCase().contains("failed")) {
                            showError(message);
                            return;
                        }
                        
                        // Process successful login
                        if (token != null && !token.isEmpty()) {
                            // Save token
                            TokenManager.saveToken(LoginActivity.this, token);
                            Log.d("LoginActivity", "Token saved successfully");
                            
                            // Show success message
                            Toast.makeText(LoginActivity.this, 
                                message != null ? message : "Login successful", 
                                Toast.LENGTH_SHORT).show();

                            try {
                                // Navigate to dashboard
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                Log.d("LoginActivity", "Started DashboardActivity");
                                finish();
                            } catch (Exception e) {
                                Log.e("LoginActivity", "Navigation failed", e);
                                showError("Error navigating to dashboard: " + e.getMessage());
                            }
                        } else {
                            // Handle missing token
                            showError("Login failed: Invalid credentials");
                        }
                    } else {
                        // Handle unsuccessful response
                        String errorMessage = "Authentication failed";
                        if (response.errorBody() != null) {
                            try {
                                LoginResponse errorResponse = new Gson().fromJson(
                                    response.errorBody().string(), LoginResponse.class);
                                if (errorResponse != null && errorResponse.getMessage() != null) {
                                    errorMessage = errorResponse.getMessage();
                                }
                            } catch (Exception e) {
                                Log.e("LoginActivity", "Error parsing error response", e);
                            }
                        }
                        showError(errorMessage);
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                runOnUiThread(() -> {
                    hideProgressDialog();
                    Log.e("LoginActivity", "Network error", t);
                    if (!isNetworkAvailable()) {
                        showError("No internet connection");
                    } else {
                        showError("Network error. Please try again.");
                    }
                });
            }
        });
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.progress_dialog);
            builder.setCancelable(false);
            progressDialog = builder.create();
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
