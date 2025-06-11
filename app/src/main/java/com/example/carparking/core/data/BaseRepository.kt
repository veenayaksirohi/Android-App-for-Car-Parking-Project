package com.example.carparking.core.data

import kotlinx.coroutines.flow.Flow

interface BaseRepository<T> {
    suspend fun get(id: String): T?

    suspend fun getAll(): Flow<List<T>>

    suspend fun insert(item: T)

    suspend fun update(item: T)

    suspend fun delete(item: T)

    suspend fun deleteAll()
}
