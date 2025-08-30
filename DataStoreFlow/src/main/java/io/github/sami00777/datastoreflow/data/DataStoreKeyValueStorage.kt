package io.github.sami00777.datastoreflow.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import io.github.sami00777.datastoreflow.domain.KeyValueStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DataStoreKeyValueStorage(
    private val dataStore: DataStore<Preferences>
): KeyValueStorage {

    override suspend fun <T> getValue(key: String, defaultValue: T): T {
        return withContext(Dispatchers.IO) {
            try {
                val preferences = dataStore.data.first()
                getValueFromPreferences(preferences, key, defaultValue)
            } catch (_: Exception) {
                defaultValue
            }
        }
    }

    override suspend fun <T> setValue(key: String, value: T) {
        withContext(Dispatchers.IO) {
            try {
                dataStore.edit { preferences ->
                    setValueInPreferences(preferences, key, value)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun removeValue(key: String) {
        withContext(Dispatchers.IO) {
            try {
                dataStore.edit { preferences ->
                    when {
                        preferences.contains(booleanPreferencesKey(key)) -> preferences.remove(
                            booleanPreferencesKey(key)
                        )

                        preferences.contains(intPreferencesKey(key)) -> preferences.remove(
                            intPreferencesKey(key)
                        )

                        preferences.contains(floatPreferencesKey(key)) -> preferences.remove(
                            floatPreferencesKey(key)
                        )

                        preferences.contains(longPreferencesKey(key)) -> preferences.remove(
                            longPreferencesKey(key)
                        )

                        preferences.contains(doublePreferencesKey(key)) -> preferences.remove(
                            doublePreferencesKey(key)
                        )

                        preferences.contains(stringSetPreferencesKey(key)) -> preferences.remove(
                            stringSetPreferencesKey(key)
                        )

                        preferences.contains(stringPreferencesKey(key)) -> preferences.remove(
                            stringPreferencesKey(key)
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            try {
                dataStore.edit { preferences ->
                    preferences.clear()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun <T> observeValue(
        key: String,
        defaultValue: T
    ): Flow<T> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                getValueFromPreferences(preferences, key, defaultValue)
            }.flowOn(Dispatchers.IO)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getValueFromPreferences(
        preferences: Preferences,
        key: String,
        defaultValue: T
    ): T {
        return when (defaultValue) {
            is String -> preferences[stringPreferencesKey(key)] ?: defaultValue
            is Int -> preferences[intPreferencesKey(key)] ?: defaultValue
            is Boolean -> preferences[booleanPreferencesKey(key)] ?: defaultValue
            is Float -> preferences[floatPreferencesKey(key)] ?: defaultValue
            is Long -> preferences[longPreferencesKey(key)] ?: defaultValue
            is Double -> preferences[doublePreferencesKey(key)] ?: defaultValue
            is Set<*> -> preferences[stringSetPreferencesKey(key)] ?: defaultValue
            else -> throw UnsupportedOperationException("This type can't be saved into DataStore ${defaultValue?.javaClass?.name}")
        } as T
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> setValueInPreferences(preferences: MutablePreferences, key: String, value: T) {
        when (value) {
            is String -> preferences[stringPreferencesKey(key)] = value
            is Int -> preferences[intPreferencesKey(key)] = value
            is Boolean -> preferences[booleanPreferencesKey(key)] = value
            is Float -> preferences[floatPreferencesKey(key)] = value
            is Long -> preferences[longPreferencesKey(key)] = value
            is Double -> preferences[doublePreferencesKey(key)] = value
            is Set<*> -> preferences[stringSetPreferencesKey(key)] = value as Set<String>
            else -> throw UnsupportedOperationException("This type can't be saved into DataStore ${value!!::class.java}")
        }
    }


}