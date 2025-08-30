package io.github.sami00777.datastoreflow.domain

/**
 * Represents the result of a storage operation.
 *
 * This sealed class provides a unified way to handle the states of asynchronous
 * storage operations, including loading, success, and error.
 *
 * @param T The type of the stored value.
 */
sealed class StorageResult<out T> {

    /**
     * Represents a loading state.
     *
     * This can be used to indicate that a storage operation is in progress.
     */
    object Loading : StorageResult<Nothing>()

    /**
     * Represents a successful operation.
     *
     * @param data The value returned by the storage operation.
     */
    data class Success<T>(val data: T) : StorageResult<T>()

    /**
     * Represents a failed operation.
     *
     * @param exception The [Throwable] describing the cause of the failure.
     */
    data class Error(val exception: Throwable) : StorageResult<Nothing>()
}
