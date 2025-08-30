package io.github.sami00777.datastoreflow.repository

import io.github.sami00777.datastoreflow.domain.KeyValueStorage
import io.github.sami00777.datastoreflow.domain.StorageResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class StorageRepositoryImpl(
    private val keyValueStorage: KeyValueStorage
) : StorageRepository {

    override suspend fun <T> saveValue(key: String, value: T): StorageResult<Unit> {
        return try {
            keyValueStorage.setValue(key, value)
            StorageResult.Success(Unit)
        } catch (e: Exception) {
            StorageResult.Error(e)
        }
    }

    override suspend fun <T> getValue(key: String, defaultValue: T): StorageResult<T> {
        return try {
            val value = keyValueStorage.getValue(key, defaultValue)
            StorageResult.Success(value)
        } catch (e: Exception) {
            StorageResult.Error(e)
        }
    }

    override suspend fun removeValue(key: String): StorageResult<Unit> {
        return try {
            keyValueStorage.removeValue(key)
            StorageResult.Success(Unit)
        } catch (e: Exception) {
            StorageResult.Error(e)
        }
    }

    override suspend fun clearAll(): StorageResult<Unit> {
        return try {
            keyValueStorage.clearAll()
            StorageResult.Success(Unit)
        } catch (e: Exception) {
            StorageResult.Error(e)
        }
    }

    override fun <T> observeValue(key: String, defaultValue: T): Flow<StorageResult<T>> {
        return keyValueStorage.observeValue(key, defaultValue)
            .onStart { StorageResult.Loading }
            .map { StorageResult.Success(it) as StorageResult<T> }
            .catch { emit(StorageResult.Error(it)) }
    }
}