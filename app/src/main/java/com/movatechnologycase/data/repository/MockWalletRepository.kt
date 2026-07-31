package com.movatechnologycase.data.repository

import android.content.res.AssetManager
import com.movatechnologycase.domain.model.WalletDashboard
import com.movatechnologycase.domain.repository.WalletRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class MockWalletRepository @Inject constructor(
    private val assetManager: AssetManager,
    private val json: Json,
    private val scenarioController: WalletScenarioController
) : WalletRepository {

    override suspend fun getDashboard(): WalletDashboard =
        withContext(Dispatchers.IO) {
            delay(600)

            val scenario =
                scenarioController.getScenario()

            if (
                scenario == WalletScenario.ERROR &&
                scenarioController.consumeError()
            ) {
                throw IOException(
                    "Wallet data could not be loaded"
                )
            }

            val fileName = when (scenario) {
                WalletScenario.EMPTY ->
                    "mock_wallet_empty.json"

                WalletScenario.LOADED,
                WalletScenario.ERROR ->
                    "mock_wallet.json"
            }

            readWalletFile(fileName)
        }

    private fun readWalletFile(
        fileName: String
    ): WalletDashboard {
        val jsonContent = assetManager
            .open(fileName)
            .bufferedReader()
            .use { reader ->
                reader.readText()
            }

        return json.decodeFromString(
            string = jsonContent
        )
    }
}