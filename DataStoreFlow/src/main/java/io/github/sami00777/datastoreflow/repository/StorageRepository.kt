package io.github.sami00777.datastoreflow.repository

import io.github.sami00777.datastoreflow.domain.StorageResult
import kotlinx.coroutines.flow.Flow

/**
 * Defines a contract for a storage repository that provides
 * CRUD-like operations and reactive observation of stored values.
 *
 * This abstraction allows saving, retrieving, removing, and observing
 * key–value pairs in a type-safe and DI-friendly way, with all operations
 * returning a [StorageResult] to handle success, errors, or loading states.
 */
interface StorageRepository {
    suspend fun <T> saveValue(key: String, value: T): StorageResult<Unit>
    suspend fun <T> getValue(key: String, defaultValue: T): StorageResult<T>
    suspend fun removeValue(key: String): StorageResult<Unit>
    suspend fun clearAll(): StorageResult<Unit>
    fun <T> observeValue(key: String, defaultValue: T): Flow<StorageResult<T>>
}