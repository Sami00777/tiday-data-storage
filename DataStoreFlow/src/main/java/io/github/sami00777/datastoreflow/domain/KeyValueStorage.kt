package io.github.sami00777.datastoreflow.domain

import kotlinx.coroutines.flow.Flow

/**
 * Generic interface for key-value storage operations
 */
interface KeyValueStorage {
    suspend fun <T> getValue(key: String, defaultValue: T): T
    suspend fun <T> setValue(key: String, value: T)
    suspend fun removeValue(key: String)
    suspend fun clearAll()
    fun <T> observeValue(key: String, defaultValue: T): Flow<T>
}