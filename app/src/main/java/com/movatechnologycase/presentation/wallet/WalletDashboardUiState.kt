package com.movatechnologycase.presentation.wallet

import com.movatechnologycase.domain.model.WalletDashboard

sealed interface WalletDashboardUiState {

    data object Loading : WalletDashboardUiState
    data class Loaded(val dashboard: WalletDashboard) : WalletDashboardUiState
    data class Empty(val dashboard: WalletDashboard) : WalletDashboardUiState
    data class Error(val message: String) : WalletDashboardUiState
}