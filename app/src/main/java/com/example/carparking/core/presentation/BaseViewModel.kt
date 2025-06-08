package com.example.carparking.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Event> : ViewModel() {
    private val _state = MutableStateFlow<State?>(null)
    val state: StateFlow<State?> = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    protected fun setState(state: State) {
        viewModelScope.launch {
            _state.emit(state)
        }
    }

    protected fun setError(error: String?) {
        viewModelScope.launch {
            _error.emit(error)
        }
    }

    protected fun setLoading(loading: Boolean) {
        viewModelScope.launch {
            _loading.emit(loading)
        }
    }

    abstract fun handleEvent(event: Event)
} 