package com.example.douplus.core.result

sealed interface AppResult<out T> {
    data class Success<T>(val value: T) : AppResult<T>
    data class Failure(val reason: FailReason, val throwable: Throwable? = null) : AppResult<Nothing>

    inline fun onSuccess(block: (T) -> Unit): AppResult<T> {
        if (this is Success<T>) block(value)
        return this
    }

    inline fun onFailure(block: (Failure) -> Unit): AppResult<T> {
        if (this is Failure) block(this)
        return this
    }

    fun getOrNull(): T? = (this as? Success<T>)?.value

    fun failureOrNull(): Failure? = this as? Failure

    companion object {
        inline fun <T> runCatchingApp(
            reasonOnFailure: FailReason = FailReason.Unknown,
            block: () -> T
        ): AppResult<T> {
            return try {
                Success(block())
            } catch (t: Throwable) {
                Failure(reasonOnFailure, t)
            }
        }
    }
}

sealed interface FailReason {
    data object Unknown : FailReason
    data class Message(val text: String) : FailReason
    data object InvalidInput : FailReason
    data object NotFound : FailReason
    data class StorageError(val source: String) : FailReason
    data class ParseError(val source: String) : FailReason
    data class Unsupported(val detail: String) : FailReason
}
