package com.movatechnologycase.ui.wallet

import com.movatechnologycase.data.repository.WalletScenario

class WalletDashboardContract {

    data class UiState(
        val isLoading: Boolean = true,
        val selectedScenario: WalletScenario = WalletScenario.LOADED,
        val dashboard: WalletDashboardUiModel? = null,
        val error: String? = null,
        val isBalanceVisible: Boolean = true
    )

    sealed interface WalletEvent {
        data class OnScenarioSelected(val scenario: WalletScenario) : WalletEvent
        data object OnRetryClick : WalletEvent
        data object OnBalanceVisibilityClick : WalletEvent
    }
}