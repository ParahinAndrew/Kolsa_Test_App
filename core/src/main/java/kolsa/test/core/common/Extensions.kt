package kolsa.test.core.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

fun <T> Flow<T>.asApiResult(): Flow<ApiResult<T>> = flow {
    emit(ApiResult.Loading)
    try {
        collect { value ->
            emit(ApiResult.Success(value))
        }
    } catch (e: Exception) {
        val errorType = when (e) {
            is IOException -> ErrorType.NETWORK
            is HttpException -> {
                when (e.code()) {
                    in 400..499 -> ErrorType.VALIDATION
                    in 500..599 -> ErrorType.SERVER
                    else -> ErrorType.UNKNOWN
                }
            }
            else -> ErrorType.UNKNOWN
        }
        emit(ApiResult.Error(
            type = errorType,
            error = e,
            messageId = errorType.messageId
        ))
    }
}