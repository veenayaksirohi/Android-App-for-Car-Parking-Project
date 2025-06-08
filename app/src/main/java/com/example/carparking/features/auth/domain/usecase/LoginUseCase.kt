package com.example.carparking.features.auth.domain.usecase

import com.example.carparking.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) = 
        repository.login(email, password)
} 