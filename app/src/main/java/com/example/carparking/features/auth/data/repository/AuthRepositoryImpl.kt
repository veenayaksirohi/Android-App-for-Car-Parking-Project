package com.example.carparking.features.auth.data.repository

import com.example.carparking.features.auth.domain.model.User
import com.example.carparking.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    private val _currentUser = MutableStateFlow<User?>(null)
    
    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            // TODO: Implement actual login logic with Firebase or your backend
            val user = User(
                id = "1",
                name = "Test User",
                email = email
            )
            _currentUser.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<User> {
        return try {
            // TODO: Implement actual registration logic with Firebase or your backend
            val user = User(
                id = "1",
                name = name,
                email = email
            )
            _currentUser.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        _currentUser.value = null
    }

    override fun getCurrentUser(): Flow<User?> = _currentUser

    override fun isUserLoggedIn(): Flow<Boolean> = MutableStateFlow(_currentUser.value != null)
} 