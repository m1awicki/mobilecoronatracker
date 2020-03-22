package com.mobilecoronatracker.data.networking

suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.ResponseResult(apiCall.invoke())
    } catch (e: Exception) {
        //TODO: handle different types of errors
        NetworkResult.ErrorResult(Throwable(e))
    }
}
