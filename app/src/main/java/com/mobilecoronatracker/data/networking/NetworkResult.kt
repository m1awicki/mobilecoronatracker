package com.mobilecoronatracker.data.networking

sealed class NetworkResult<out T> {
    class ResponseResult<out T>(val data: T) : NetworkResult<T>()
    class ErrorResult(val throwable: Throwable) : NetworkResult<Nothing>()
}
