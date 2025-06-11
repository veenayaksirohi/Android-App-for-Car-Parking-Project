package com.example.carparking.core.architecture

/**
 * Base class for UI states in MVVM architecture.
 * Represents the state of a screen or feature.
 */
sealed class UiState<out T> {
    object Empty : UiState<Nothing>()

    object Loading : UiState<Nothing>()

    data class Success<T>(val data: T) : UiState<T>()

    data class Error(val message: String) : UiState<Nothing>()
}
