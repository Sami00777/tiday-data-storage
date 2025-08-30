package io.github.sami00777.datastoreflow.domain

import io.github.sami00777.datastoreflow.repository.StorageRepository

/**
 * TidyStorage provides a simple API for saving, retrieving, observing,
 * and clearing key-value data in a storage backend.
 *
 * It delegates all operations to a [StorageRepository] implementation,
 *
 * @param repository The storage repository implementation used to persist values.
 */
class TidyStorage(private val repository: StorageRepository) {

    /**
     * Saves a key-value pair in persistent storage.
     *
     * Example:
     * ```
     * tidyStorage.saveValue("username", "Sam")
     * ```
     *
     * @param key Unique identifier for the value.
     * @param value The value to store. Can be any serializable type.
     */
    suspend fun <T> saveValue(key: String, value: T) = repository.saveValue(key, value)

    /**
     * Retrieves a value from storage or returns the default if not found.
     *
     * Example:
     * ```
     * tidyStorage.getValue("username", "Guest")
     *     .onSuccess { value ->
     *         println("Value is $value")
     *     }
     *     .onError { error ->
     *         println("Failed to get value: ${error.message}")
     *     }
     * ```
     *
     * @param key The key to look up.
     * @param defaultValue Value to return if the key does not exist.
     * @return [StorageResult] containing the stored value or default.
     */
    suspend fun <T> getValue(key: String, defaultValue: T) = repository.getValue(key, defaultValue)

    /**
     * Observes changes to a stored value as a [Flow].
     *
     * Example (in a ViewModel):
     * ```
     * val usernameFlow: StateFlow<StorageResult<String>> =
     *     tidyStorage.observeValue("username", "Guest")
     *         .stateIn(
     *             scope = viewModelScope,
     *             started = SharingStarted.Lazily,
     *             initialValue = StorageResult.Loading
     *         )
     * ```
     *
     * Example (collecting in an Activity/Fragment):
     * ```
     * lifecycleScope.launch {
     *     repeatOnLifecycle(Lifecycle.State.STARTED) {
     *         viewModel.usernameFlow.collect { result ->
     *             result.onSuccess { name ->
     *                 binding.textView.text = name
     *             }
     *             result.onError { error ->
     *                 // handle error, show snackbar/toast, etc.
     *             }
     *         }
     *     }
     * }
     * ```
     *
     * @param key The identifier for the stored value.
     * @param defaultValue A fallback value if no data exists.
     * @return A [Flow] emitting the current and future values for the given key.
     */
    fun <T> observeValue(key: String, defaultValue: T) = repository.observeValue(key, defaultValue)

    /**
     * Removes the stored value for the given [key].
     *
     * @param key The identifier of the value to remove.
     */
    suspend fun removeValue(key: String) = repository.removeValue(key)

    /**
     * Clears all stored values.
     */
    suspend fun clearAll() = repository.clearAll()
}
