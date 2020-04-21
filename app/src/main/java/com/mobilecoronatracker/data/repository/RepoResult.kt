package com.mobilecoronatracker.data.repository

sealed class RepoResult<out T> {
    class SuccessResult<out T>(val data: T) : RepoResult<T>()
    class FailureResult(val throwable: Throwable? = null) : RepoResult<Nothing>()
}
