package kolsa.test.core.common

sealed interface ApiResult<out T> {
    data object Loading : ApiResult<Nothing>
    data class Success<out T>(val data: T) : ApiResult<T>
    data class Error(
        val type: ErrorType,
        val error: Throwable? = null,
        val messageId: Int? = null
    ) : ApiResult<Nothing>
}