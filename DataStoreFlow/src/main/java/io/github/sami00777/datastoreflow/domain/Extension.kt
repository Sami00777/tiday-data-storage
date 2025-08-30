package io.github.sami00777.datastoreflow.domain

/**
 * Executes the given [action] if this [StorageResult] is [StorageResult.Success].
 *
 * @param action Lambda invoked with the success [data].
 * @return Returns the same [StorageResult] for chaining.
 *
 * Example:
 * ```
 * storageResult
 *     .onSuccess { value -> println("Saved: $value") }
 *     .onError { e -> println("Failed: ${e.message}") }
 * ```
 */
inline fun <T> StorageResult<T>.onSuccess(action: (value: T) -> Unit): StorageResult<T> {
    if (this is StorageResult.Success) action(data)
    return this
}

/**
 * Executes the given [action] if this [StorageResult] is [StorageResult.Error].
 *
 * @param action Lambda invoked with the [Throwable] cause of failure.
 * @return Returns the same [StorageResult] for chaining.
 *
 * Example:
 * ```
 * storageResult
 *     .onSuccess { println("Data saved!") }
 *     .onError { e -> println("Error: ${e.localizedMessage}") }
 * ```
 */
inline fun <T> StorageResult<T>.onError(action: (exception: Throwable) -> Unit): StorageResult<T> {
    if (this is StorageResult.Error) action(exception)
    return this
}
