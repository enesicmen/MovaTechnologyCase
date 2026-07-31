package com.movatechnologycase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.movatechnologycase.data.repository.MockWalletRepository
import com.movatechnologycase.data.repository.WalletScenario
import com.movatechnologycase.presentation.wallet.WalletDashboardRoute
import com.movatechnologycase.presentation.wallet.WalletViewModelFactory
import com.movatechnologycase.ui.theme.MovaTechnologyCaseTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MovaTechnologyCaseTheme {
                val repository = remember {
                    MockWalletRepository(
                        context = applicationContext,
                        scenario = WalletScenario.LOADED
                    )
                }

                val factory = remember {
                    WalletViewModelFactory(
                        repository = repository
                    )
                }

                WalletDashboardRoute(
                    viewModel = viewModel(
                        factory = factory
                    )
                )
            }
        }
    }
}