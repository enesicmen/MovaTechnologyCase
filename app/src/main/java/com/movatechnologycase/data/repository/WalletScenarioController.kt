package com.movatechnologycase.data.repository

import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletScenarioController @Inject constructor() {

    @Volatile
    private var currentScenario: WalletScenario =
        WalletScenario.LOADED

    private val shouldFailNextRequest =
        AtomicBoolean(false)

    fun selectScenario(scenario: WalletScenario) {
        currentScenario = scenario
        shouldFailNextRequest.set(
            scenario == WalletScenario.ERROR
        )
    }

    fun getScenario(): WalletScenario {
        return currentScenario
    }

    fun consumeError(): Boolean {
        return shouldFailNextRequest.getAndSet(false)
    }
}