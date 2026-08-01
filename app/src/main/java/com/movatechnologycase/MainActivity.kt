package com.movatechnologycase

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.movatechnologycase.ui.wallet.WalletDashboardScreen
import com.movatechnologycase.ui.wallet.WalletDashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                val walletViewModel: WalletDashboardViewModel = viewModel()
                WalletDashboardScreen(viewModel = walletViewModel)
            }
        }
    }
}