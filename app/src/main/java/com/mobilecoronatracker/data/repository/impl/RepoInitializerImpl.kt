package com.mobilecoronatracker.data.repository.impl

import com.mobilecoronatracker.data.repository.RepoInitStrategy
import com.mobilecoronatracker.data.repository.RepoInitializer

class RepoInitializerImpl(private var strategy: RepoInitStrategy) : RepoInitializer {
    override fun setStrategy(strategy: RepoInitStrategy) {
        this.strategy = strategy
    }

    override suspend fun start() {
        strategy.execute()
    }
}