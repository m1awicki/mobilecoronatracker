package com.mobilecoronatracker.data.repository

interface RepoInitStrategy {
    suspend fun execute()
}