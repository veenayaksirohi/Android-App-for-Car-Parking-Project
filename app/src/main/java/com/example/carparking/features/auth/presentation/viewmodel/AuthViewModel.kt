package com.example.carparking.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carparking.core.architecture.UiState
import com.example.carparking.features.auth.domain.model.User
import com.example.carparking.features.auth.domain.usecase.LoginUseCase
import com.example.carparking.features.auth.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<UiState<User>>(UiState.Empty)
    val authState: StateFlow<UiState<User>> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = UiState.Loading
            loginUseCase(email, password)
                .onSuccess { user ->
                    _authState.value = UiState.Success(user)
                }
                .onFailure { error ->
                    _authState.value = UiState.Error(error.message ?: "Login failed")
                }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = UiState.Loading
            registerUseCase(name, email, password)
                .onSuccess { user ->
                    _authState.value = UiState.Success(user)
                }
                .onFailure { error ->
                    _authState.value = UiState.Error(error.message ?: "Registration failed")
                }
        }
    }
} 