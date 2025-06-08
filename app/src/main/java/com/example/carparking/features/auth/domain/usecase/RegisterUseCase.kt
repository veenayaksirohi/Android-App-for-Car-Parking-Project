package com.example.carparking.features.auth.domain.usecase

import com.example.carparking.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String) =
        repository.register(name, email, password)
} 