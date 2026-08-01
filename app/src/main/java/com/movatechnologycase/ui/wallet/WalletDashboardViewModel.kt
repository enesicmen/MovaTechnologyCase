package com.movatechnologycase.ui.wallet

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movatechnologycase.data.repository.WalletScenario
import com.movatechnologycase.data.repository.WalletScenarioController
import com.movatechnologycase.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.CancellationException
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.movatechnologycase.R

@HiltViewModel
class WalletDashboardViewModel @Inject constructor(
    private val repository: WalletRepository,
    private val scenarioController: WalletScenarioController,
    private val savedStateHandle: SavedStateHandle,
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private val initialScenario = savedStateHandle
            .get<String>(SCENARIO_KEY)
            ?.let { savedValue ->
                runCatching {
                    WalletScenario.valueOf(savedValue)
                }.getOrNull()
            }
            ?: WalletScenario.LOADED

    private val _uiState = MutableStateFlow(value = WalletDashboardContract.UiState(selectedScenario = initialScenario))
    val uiState = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        scenarioController.selectScenario(initialScenario)
        loadDashboard()
    }

    fun onEvent(event: WalletDashboardContract.WalletEvent) {
        when (event) {
            is WalletDashboardContract.WalletEvent.OnScenarioSelected -> { selectScenario(event.scenario) }
            is WalletDashboardContract.WalletEvent.OnRetryClick -> loadDashboard()
            is WalletDashboardContract.WalletEvent.OnBalanceVisibilityClick -> toggleBalanceVisibility()
        }
    }

    private fun selectScenario(scenario: WalletScenario) {
        savedStateHandle[SCENARIO_KEY] = scenario.name
        scenarioController.selectScenario(scenario)
        _uiState.update { it.copy(selectedScenario = scenario)
        }

        loadDashboard()
    }

    private fun toggleBalanceVisibility() {
        _uiState.update { it.copy(isBalanceVisible = !it.isBalanceVisible) }
    }

    private fun loadDashboard() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val dashboard = repository.getDashboard().toUiModel()
                _uiState.update { it.copy(isLoading = false, dashboard = dashboard, error = null) }
            } catch (
                exception: CancellationException
            ) {
                throw exception
            } catch (
                exception: Exception
            ) {
                _uiState.update { it.copy(isLoading = false, dashboard = null, error = exception.message ?: context.getString(R.string.wallet_information_could_not_be_uploaded)) }
            }
        }
    }

    private companion object {
        const val SCENARIO_KEY = "selected_wallet_scenario"
    }
}