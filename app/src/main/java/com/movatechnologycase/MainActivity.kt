package com.movatechnologycase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.movatechnologycase.presentation.wallet.WalletDashboardRoute
import com.movatechnologycase.presentation.wallet.WalletDashboardViewModel
import com.movatechnologycase.ui.theme.MovaTechnologyCaseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MovaTechnologyCaseTheme {
                val walletViewModel:
                        WalletDashboardViewModel = viewModel()

                WalletDashboardRoute(
                    viewModel = walletViewModel
                )
            }
        }
    }
}