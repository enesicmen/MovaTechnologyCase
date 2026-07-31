package com.movatechnologycase.presentation.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movatechnologycase.domain.repository.WalletRepository
import java.util.concurrent.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WalletDashboardViewModel(
    private val repository: WalletRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<WalletDashboardUiState>(
            WalletDashboardUiState.Loading
        )

    val uiState: StateFlow<WalletDashboardUiState> =
        _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun retry() {
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = WalletDashboardUiState.Loading

            try {
                val dashboard = repository.getDashboard()

                _uiState.value =
                    if (dashboard.recentTransactions.isEmpty()) {
                        WalletDashboardUiState.Empty(
                            dashboard = dashboard
                        )
                    } else {
                        WalletDashboardUiState.Loaded(
                            dashboard = dashboard
                        )
                    }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = WalletDashboardUiState.Error(
                    message = "Wallet bilgileri yüklenemedi."
                )
            }
        }
    }
}