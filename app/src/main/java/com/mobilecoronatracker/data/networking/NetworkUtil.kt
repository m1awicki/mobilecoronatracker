package com.mobilecoronatracker.data.networking

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): NetworkResult<T> {
    return withContext(dispatcher) {
        try {
            NetworkResult.ResponseResult(apiCall.invoke())
        } catch (e: Exception) {
            NetworkResult.ErrorResult(Throwable(e))
        }
    }
}
