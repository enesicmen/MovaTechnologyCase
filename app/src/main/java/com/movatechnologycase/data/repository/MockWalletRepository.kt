package com.movatechnologycase.data.repository

import android.content.Context
import android.content.res.AssetManager
import com.movatechnologycase.R
import com.movatechnologycase.domain.model.WalletDashboard
import com.movatechnologycase.domain.repository.WalletRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.milliseconds

class MockWalletRepository @Inject constructor(
    private val assetManager: AssetManager,
    private val json: Json,
    private val scenarioController: WalletScenarioController,
    @param:ApplicationContext private val context: Context
) : WalletRepository {

    override suspend fun getDashboard(): WalletDashboard =
        withContext(Dispatchers.IO) {
            delay(600.milliseconds)

            val scenario =
                scenarioController.getScenario()

            if (
                scenario == WalletScenario.ERROR &&
                scenarioController.consumeError()
            ) {
                throw IOException(context.getString(R.string.wallet_data_could_not_be_loaded))
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