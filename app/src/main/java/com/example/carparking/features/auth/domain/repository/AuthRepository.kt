package com.example.carparking.features.auth.domain.repository

import com.example.carparking.features.auth.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String,
    ): Result<User>

    suspend fun register(
        name: String,
        email: String,
        password: String,
    ): Result<User>

    suspend fun logout()

    fun getCurrentUser(): Flow<User?>

    fun isUserLoggedIn(): Flow<Boolean>
}
