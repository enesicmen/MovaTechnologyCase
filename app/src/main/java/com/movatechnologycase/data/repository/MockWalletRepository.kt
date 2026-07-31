package com.movatechnologycase.data.repository

import android.content.Context
import androidx.compose.ui.res.stringResource
import com.movatechnologycase.R
import com.movatechnologycase.data.dto.WalletResponseDto
import com.movatechnologycase.data.mapper.toDomain
import com.movatechnologycase.domain.model.WalletDashboard
import com.movatechnologycase.domain.repository.WalletRepository
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.milliseconds

class MockWalletRepository(
    context: Context,
    private val scenario: WalletScenario = WalletScenario.LOADED
) : WalletRepository {

    private val applicationContext = context.applicationContext

    private val shouldFailNextRequest = AtomicBoolean(scenario == WalletScenario.ERROR)

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getDashboard(): WalletDashboard =
        withContext(Dispatchers.IO) {
            delay(duration = 800.milliseconds)

            if (shouldFailNextRequest.getAndSet(false)) throw IOException(
                applicationContext.getString(
                    R.string.wallet_data_could_not_be_loaded
                )
            )

            when (scenario) {
                WalletScenario.EMPTY -> readWalletFile(fileName = "mock_wallet_empty.json")
                WalletScenario.LOADED,
                WalletScenario.ERROR -> readWalletFile(fileName = "mock_wallet.json")
            }
        }

    private fun readWalletFile(
        fileName: String
    ): WalletDashboard {
        val jsonContent = applicationContext.assets
            .open(fileName)
            .bufferedReader()
            .use { reader ->
                reader.readText()
            }

        return json
            .decodeFromString<WalletResponseDto>(jsonContent)
            .toDomain()
    }
}