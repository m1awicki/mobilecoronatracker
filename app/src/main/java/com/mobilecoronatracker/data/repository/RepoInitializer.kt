package com.mobilecoronatracker.data.repository

interface RepoInitializer {
    fun setStrategy(strategy: RepoInitStrategy)
    suspend fun start()
}