package com.example.carparking.features.auth.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String? = null,
    val role: String = "user"
) 