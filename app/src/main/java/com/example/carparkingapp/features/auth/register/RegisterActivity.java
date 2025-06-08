package com.example.carparkingapp.features.auth.register;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carparkingapp.R;
import com.example.carparkingapp.features.auth.login.LoginActivity;
import com.example.carparkingapp.models.RegisterRequest;
import com.example.carparkingapp.models.RegisterResponse;
import com.example.carparkingapp.core.network.ApiClient;
import com.example.carparkingapp.core.network.ApiInterface;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPassword, etPhoneNo, etAddress;
    TextView tvHeading, tv2, tv3;

    @SuppressLint("MissingInflatedId")
    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etName = findViewById(R.id.et1);
        etEmail = findViewById(R.id.et2);
        etPassword = findViewById(R.id.et3);
        etPhoneNo = findViewById(R.id.et4);
        etAddress = findViewById(R.id.et5);
        Button btnRegister = findViewById(R.id.b1);
        tvHeading = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);

        btnRegister.setOnClickListener(v -> registerUser());

        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phoneText = etPhoneNo.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phoneText.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();            return;
        }
        
        // Create registration request using model class
        RegisterRequest request = new RegisterRequest(name, email, password, phoneText, address);

        // Make API call
        ApiClient.getInstance(this).getService()
            .registerUser(request)
            .enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        RegisterResponse result = response.body();
                        Toast.makeText(RegisterActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        // Navigate to Login screen after successful registration
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = "Registration failed";
                        if (response.errorBody() != null) {
                            try {
                                RegisterResponse errorResponse = new Gson().fromJson(
                                    response.errorBody().string(), RegisterResponse.class);
                                errorMessage = errorResponse.getMessage();
                            } catch (Exception e) {
                                Log.e("RegisterActivity", "Error parsing error response", e);
                            }
                        }
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }
}