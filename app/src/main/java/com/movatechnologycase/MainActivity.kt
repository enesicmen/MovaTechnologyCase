package com.movatechnologycase

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.movatechnologycase.ui.theme.MovaTechnologyCaseTheme
import com.movatechnologycase.ui.wallet.WalletDashboardScreen
import com.movatechnologycase.ui.wallet.WalletDashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MovaTechnologyCaseTheme {
                val walletViewModel: WalletDashboardViewModel = viewModel()
                WalletDashboardScreen(viewModel = walletViewModel)
            }
        }
    }
}