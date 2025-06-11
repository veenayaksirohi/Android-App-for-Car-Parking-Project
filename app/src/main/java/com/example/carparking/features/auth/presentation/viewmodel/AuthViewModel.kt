package com.example.carparking.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carparking.features.auth.domain.model.User
import com.example.carparking.features.auth.domain.usecase.LoginUseCase
import com.example.carparking.features.auth.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
    @Inject
    constructor(
        private val loginUseCase: LoginUseCase,
        private val registerUseCase: RegisterUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
        val uiState: StateFlow<UiState> = _uiState

        fun login(
            email: String,
            password: String,
        ) {
            viewModelScope.launch {
                _uiState.value = UiState.Loading
                loginUseCase(email, password)
                    .onSuccess { user ->
                        _uiState.value = UiState.Success(user)
                    }
                    .onFailure { error ->
                        _uiState.value = UiState.Error(error.message ?: "Login failed")
                    }
            }
        }

        fun register(
            name: String,
            email: String,
            password: String,
        ) {
            viewModelScope.launch {
                _uiState.value = UiState.Loading
                registerUseCase(name, email, password)
                    .onSuccess { user ->
                        _uiState.value = UiState.Success(user)
                    }
                    .onFailure { error ->
                        _uiState.value = UiState.Error(error.message ?: "Registration failed")
                    }
            }
        }
    }

sealed class UiState {
    object Initial : UiState()

    object Loading : UiState()

    data class Success(val user: User) : UiState()

    data class Error(val message: String) : UiState()
}
