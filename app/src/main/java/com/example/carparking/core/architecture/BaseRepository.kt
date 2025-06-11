package com.example.carparking.core.architecture

import kotlinx.coroutines.flow.Flow

/**
 * Base repository interface that defines common operations for all repositories.
 *
 * @param T The type of data this repository handles
 * @param ID The type of identifier used to fetch/update/delete data
 */
interface BaseRepository<T, ID> {
    /**
     * Get all items
     */
    fun getAll(): Flow<List<T>>

    /**
     * Get an item by its ID
     */
    suspend fun getById(id: ID): T?

    /**
     * Insert or update an item
     */
    suspend fun save(item: T)

    /**
     * Delete an item
     */
    suspend fun delete(item: T)

    /**
     * Delete an item by its ID
     */
    suspend fun deleteById(id: ID)
} 

abstract class BaseApiRepository {
    protected suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return try {
            Result.success(apiCall.invoke())
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }
}
