package io.github.sami00777.datastoreflow.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import io.github.sami00777.datastoreflow.data.DataStoreKeyValueStorage
import io.github.sami00777.datastoreflow.domain.KeyValueStorage
import io.github.sami00777.datastoreflow.domain.TidyStorage
import io.github.sami00777.datastoreflow.repository.StorageRepository
import io.github.sami00777.datastoreflow.repository.StorageRepositoryImpl

private const val DATA_STORE_FILE_NAME = "app_preferences"

val Context.preferenceDataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATA_STORE_FILE_NAME,
)

class DataStoreModule(context: Context) {

    // Backing DataStore
    private val appDataStore = context.preferenceDataStore

    // Provide KeyValueStorage
    private val keyValueStorage: KeyValueStorage by lazy {
        DataStoreKeyValueStorage(appDataStore)
    }

    // Provide Repository
    private val storageRepository: StorageRepository by lazy {
        StorageRepositoryImpl(keyValueStorage)
    }

    // Expose TidyStorage to end users
    val tidyStorage: TidyStorage by lazy {
        TidyStorage(storageRepository)
    }
}
