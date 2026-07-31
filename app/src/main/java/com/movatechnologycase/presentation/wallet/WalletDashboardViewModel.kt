package com.movatechnologycase.presentation.wallet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movatechnologycase.data.repository.WalletScenario
import com.movatechnologycase.data.repository.WalletScenarioController
import com.movatechnologycase.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.CancellationException
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class WalletDashboardViewModel @Inject constructor(
    private val repository: WalletRepository,
    private val scenarioController: WalletScenarioController,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialScenario =
        savedStateHandle
            .get<String>(SCENARIO_KEY)
            ?.let { savedValue ->
                runCatching {
                    WalletScenario.valueOf(savedValue)
                }.getOrNull()
            }
            ?: WalletScenario.LOADED

    private val _selectedScenario =
        MutableStateFlow(initialScenario)

    val selectedScenario: StateFlow<WalletScenario> =
        _selectedScenario.asStateFlow()

    private val _uiState =
        MutableStateFlow<WalletDashboardUiState>(
            WalletDashboardUiState.Loading
        )

    val uiState: StateFlow<WalletDashboardUiState> =
        _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        scenarioController.selectScenario(initialScenario)
        loadDashboard()
    }

    fun selectScenario(
        scenario: WalletScenario
    ) {
        _selectedScenario.value = scenario
        savedStateHandle[SCENARIO_KEY] = scenario.name

        scenarioController.selectScenario(scenario)
        loadDashboard()
    }

    fun retry() {
        loadDashboard()
    }

    private fun loadDashboard() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _uiState.value =
                WalletDashboardUiState.Loading

            try {
                val dashboard =
                    repository.getDashboard()

                _uiState.value =
                    if (
                        dashboard.recentTransactions
                            .isEmpty()
                    ) {
                        WalletDashboardUiState.Empty(
                            dashboard = dashboard
                        )
                    } else {
                        WalletDashboardUiState.Loaded(
                            dashboard = dashboard
                        )
                    }
            } catch (
                exception: CancellationException
            ) {
                throw exception
            } catch (
                exception: Exception
            ) {
                _uiState.value =
                    WalletDashboardUiState.Error(
                        message =
                            "Wallet bilgileri yüklenemedi."
                    )
            }
        }
    }

    private companion object {
        const val SCENARIO_KEY =
            "selected_wallet_scenario"
    }
}